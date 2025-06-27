package it.polimi.ingsw.galaxytruckers.server.controller.lobby;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.server.model.GameInterface;
import it.polimi.ingsw.galaxytruckers.server.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.server.controller.events.LobbyEventHandler;
import it.polimi.ingsw.galaxytruckers.server.controller.utils.SetupUtils;
import it.polimi.ingsw.galaxytruckers.shared.utils.JsonUtils;
import it.polimi.ingsw.galaxytruckers.shared.utils.LockUtils;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class Lobby implements LobbyInterface {
    private static final long REMOVAL_DELAY = 15000;
    private static final String demoPath = "src/main/resources/demoShips_%s.json";

    private final Object paramLock = new Object();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final UUID id;
    private final Level level;
    private final int numPlayers;
    private final Player host;

    private final boolean demoMode;
    private final boolean editScenario;

    private LobbyState state;
    private boolean pending = false;
    private final GameInterface game;
    private final List<Player> players;
    private final Set<GameColor> chosenColors;
    private final Map<Player, GameColor> playerColors;

    private EventQueue<LobbyEvent> eventQueue;
    private final LobbyEventHandler lobbyEventHandler;

    private final Set<Player> disconnectedPlayers = new HashSet<>();

    private final Consumer<Lobby> removeLobby;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> scheduledFuture;
    private long removalDelay = REMOVAL_DELAY;

    /**
     * Creates a new Lobby instance.
     *
     * @param model       the game model interface to use for creating the game
     * @param creator     the player who created the lobby
     * @param level       the level of the game
     * @param numPlayers  the number of players allowed in the lobby
     * @param removeLobby the method to call when the lobby is removed
     */
    public Lobby(GameModelInterface model, Player creator, Level level, int numPlayers, Consumer<Lobby> removeLobby) {
        this(false, false, model, creator, level, numPlayers, removeLobby);
    }

    /**
     * Creates a new Lobby instance with additional parameters for demo mode and scenario editing.
     *
     * @param demoMode     true if the lobby should skip building and load a demo scenario, false otherwise
     * @param editScenario true if the lobby should edit the scenario by saving
     *                     ships to a file at the end of the building phase,
     *                     false otherwise
     * @param model        the game model interface to use for creating the game
     * @param creator      the player who created the lobby
     * @param level        the level of the game
     * @param numPlayers   the number of players allowed in the lobby
     * @param removeLobby  the method to call when the lobby is removed
     */
    public Lobby(boolean demoMode, boolean editScenario, GameModelInterface model, Player creator, Level level, int numPlayers, Consumer<Lobby> removeLobby) {
        this.demoMode = demoMode;
        this.editScenario = editScenario;

        this.id = UUID.randomUUID();
        this.level = level;
        this.numPlayers = numPlayers;
        this.host = creator;
        this.removeLobby = removeLobby;

        this.state = LobbyState.PREPARATION;
        this.players = new ArrayList<>();
        this.chosenColors = new HashSet<>();
        this.playerColors = new HashMap<>();

        this.eventQueue = new EventQueue<>();
        this.lobbyEventHandler = new LobbyEventHandler(this.eventQueue, this);

        this.game = model.createGame(level, numPlayers, new GameEventListener(this.eventQueue, () -> DtoConverter.getLobbyDetails(this)));

        lobbyEventHandler.start();
        addPlayer(creator);
    }

    /**
     * @return the host player of the lobby
     */
    public Player getHost() {
        return host;
    }

    /**
     * @return the unique identifier of the lobby
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the level of the game in this lobby
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return the number of players allowed in this lobby
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * @return the players currently in the lobby
     */
    public List<Player> getPlayers() {
        List<Player> res;
        synchronized (paramLock) {
            res = new ArrayList<>(players);
        }
        return res;
    }

    /**
     * @return a map of players and their assigned colors in the lobby
     */
    public Map<Player, GameColor> getPlayerColors() {
        Map<Player, GameColor> res;
        synchronized (paramLock) {
            res = new HashMap<>(playerColors);
        }
        return res;
    }

    /**
     * Adds a player to the lobby, assigning a color among the remaining ones.
     * If {@link Lobby#numPlayers} is not reached, enqueues the joining event,
     * otherwise starts the game. If the lobby is in demo mode it skips
     * building.
     *
     * @param player the player to add
     */
    public boolean addPlayer(Player player) {
        boolean shouldStart = LockUtils.withLock(lock, () -> {
            checkLobbyState(LobbyState.PREPARATION);
            boolean res;
            synchronized (paramLock) {
                GameColor chosenColor = Arrays.stream(GameColor.values())
                        .filter(c -> !chosenColors.contains(c))
                        .findAny().orElseThrow();
                chosenColors.add(chosenColor);
                players.add(player);
                playerColors.put(player, chosenColor);
                player.setLobby(this);
                eventQueue.notifyEvent(new LobbyDetailsEvent(
                        player.getNickname(),
                        DtoConverter.getLobbyDetails(this)
                ));
                eventQueue.notifyEvent(new JoinLobbyEvent(player.getNickname(), playerColors.get(player)));
                res = players.size() == numPlayers;
            }
            return res;
        }, false);
        updatePending();
        if (shouldStart) {
            startGame();
        }
        return shouldStart;
    }

    /**
     * Notifies the lobby and its players that a player has disconnected. If
     * all players have disconnected it schedules the removal of the lobby
     *
     * @param player the player who has disconnected
     */
    public void notifyPlayerDisconnection(Player player) {
        LockUtils.withLock(lock, () -> {
            synchronized (paramLock) {
                if (disconnectedPlayers.add(player)) {
                    eventQueue.notifyEvent(new PlayerDisconnectionEvent(player.getNickname()));
                }
            }
        }, false);
        updatePending();

    }

    private boolean shouldSchedule() {
        boolean res;
        synchronized (paramLock) {
            res = !disconnectedPlayers.isEmpty() && disconnectedPlayers.size() >= players.size() - 1;
        }
        return res;
    }

    private void updatePending() {
        LockUtils.withLock(lock, () -> {
            synchronized (paramLock) {
                if (pending && !shouldSchedule()) {
                    pending = false;
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                        scheduledFuture = null;
                    }
                }
                if (!pending && shouldSchedule()) {
                    pending = true;
                    scheduledFuture = scheduler.schedule(() -> {
                        synchronized (paramLock) {
                            eventQueue.notifyEvent(new EndByDisconnectionEvent(
                                    players.stream()
                                            .filter(p -> !disconnectedPlayers.contains(p))
                                            .findFirst()
                                            .map(Player::getNickname)
                                            .orElse(null)
                            ));
                        }
                        remove();
                    }, removalDelay, TimeUnit.MILLISECONDS);
                    System.out.println("Scheduled lobby " + getId() + " removal");
                }
            }
        }, true);
    }

    /**
     * Notifies the lobby and its players that a player has reconnected. Cancels
     * the scheduled removal of the lobby if necessary.
     *
     * @param player the player who has reconnected
     */
    public void notifyPlayerReconnection(Player player) {
        synchronized (paramLock) {
            disconnectedPlayers.remove(player);
        }
        updatePending();
        LockUtils.withLock(lock, () -> {
            if (state == LobbyState.INGAME) {
                game.requestSnapshot(player.getShipBoard().orElseThrow());
            } else {
                eventQueue.notifyEvent(new GameSnapshotEvent(
                        player.getNickname(),
                        DtoConverter.getLobbyDetails(this),
                        null
                ));
            }
        }, false);
    }

    /**
     * Notifies the lobby and its players that a player has exited the lobby.
     *
     * @param player the player who has exited
     */
    public void notifyPlayerExit(Player player) {
        eventQueue.notifyEvent(new PlayerExitEvent(player.getNickname()));
    }

    /**
     * Removes the lobby from the server, notifying all players
     */
    public void remove() {
        this.removeLobby.accept(this);
        this.lobbyEventHandler.stop();
    }

    private void checkInGame() {
        LockUtils.withLock(lock, () -> {
            checkLobbyState(LobbyState.INGAME);
            if (pending) {
                throw new IllegalStateException("All other players have disconnected, the lobby is paused");
            }
        },false);
    }

    private void checkLobbyState(LobbyState lobbyState) {
        LockUtils.withLock(lock, () -> {
            if (state != lobbyState) {
                throw new IllegalStateException("The lobby is not in " + lobbyState.toString());
            }
        }, false);
    }

    /**
     * Sets the state of the lobby to the specified state.
     *
     * @param state the new state of the lobby
     */
    public void setState(LobbyState state) {
        LockUtils.withLock(lock, () -> {
            this.state = state;
        }, true);
    }

    /**
     * @return the current state of the lobby
     */
    public LobbyState getState() {
        return state;
    }

    private void startGame() {
        LockUtils.withLock(lock, () -> {
            for (Player player : getPlayers()) {
                ShipBoard ship = game.addShipBoard(playerColors.get(player));
                player.setShipBoard(ship);
            }
            if (demoMode) {
                loadScenario();
            } else {
                if (editScenario) game.setStartAdventureCallback(this::saveShips);
                game.start();
            }
            setState(LobbyState.INGAME);
        }, true);
    }

    /**
     * This method is used to get the path to the scenario file when loading and
     * saving ship boards in demo mode. It is visible only to allow test classes
     * to override the path and test without modifying the main file.
     *
     * @return the path to the scenario file
     */
    @VisibleForTesting
    protected String getScenarioPath() {
        return String.format(demoPath, level.toString());
    }

    private void loadScenario() {
        ObjectMapper mapper = new ObjectMapper();
        File file = Paths.get(getScenarioPath()).toFile();
        try {
            JsonNode root = mapper.readTree(file);
            for (int i = 0; i < numPlayers; i++) {
                ShipBoardDTO shipBoardDTO = JsonUtils.deserializeShipBoardDTO(root.get(i));
                ShipBoard shipBoard = getPlayers().get(i).getShipBoard().orElseThrow();
                SetupUtils.setupShipBoard(shipBoard, shipBoardDTO);
            }
            game.skipBuilding();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Calls {@link GameInterface#skip(ShipBoard)} on the game. Should be
     * used when a player is disconnected and the game needs to skip their turn.
     *
     * @param player the player who is skipping their turn
     */
    public void skip(Player player) {
        LockUtils.withLock(lock, () -> {
            if (!pending && state == LobbyState.INGAME) {
                game.skip(player.getShipBoard().orElseThrow());
            }
        }, false);

    }

    /**
     * Saves the players ships to the scenario file. The method is visible
     * for testing purposes but should not be called outside the class.
     */
    @VisibleForTesting
    protected void saveShips() {
        List<JsonNode> nodes = new ArrayList<>();
        checkLobbyState(LobbyState.INGAME);
        for (Player player : getPlayers()) {
            ShipBoard shipBoard = player.getShipBoard().orElseThrow();
            ShipBoardDTO shipBoardDTO = DtoConverter.getShipBoard(shipBoard);
            nodes.add(JsonUtils.serializeShipBoardDTO(shipBoardDTO));
        }
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode fileNode = mapper.createArrayNode();
        fileNode.addAll(nodes);
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(
                            Paths.get(getScenarioPath()).toFile(),
                            fileNode
                    );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void requestRandComponent(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.requestRandComponent(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void requestComponent(Player player, int componentID) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.requestComponent(player.getShipBoard().orElseThrow(), componentID);
        }, false);
    }

    @Override
    public void rejectComponent(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.rejectComponent(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void stashComponent(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.stashComponent(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void grabPlacedComponent(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.grabPlacedComponent(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void grabStashedComponent(Player player, int index) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.grabStashedComponent(player.getShipBoard().orElseThrow(), index);
        }, false);
    }

    @Override
    public void placeComponent(Player player, Point point, Direction orientation) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.placeComponent(player.getShipBoard().orElseThrow(), point, orientation);
        }, false);
    }

    @Override
    public void flipHourglass(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.flipHourglass(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void placeShipOnFlightBoard(Player player, int startingPosition) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.placeShipOnFlightBoard(player.getShipBoard().orElseThrow(), startingPosition);
        }, false);
    }

    @Override
    public void placeShipOnFlightBoard(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.placeShipOnFlightBoard(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void acquireForecast(Player player, int deckIndex) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.acquireForecast(player.getShipBoard().orElseThrow(), deckIndex);
        }, false);
    }

    @Override
    public void releaseForecast(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.releaseForecast(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void removeComponent(Player player, Point point) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.removeComponent(player.getShipBoard().orElseThrow(), point);
        }, false);
    }

    @Override
    public void chooseShipPiece(Player player, int pieceIndex) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.chooseShipPiece(player.getShipBoard().orElseThrow(), pieceIndex);
        }, false);
    }

    @Override
    public void initializeCabin(Player player, Point point, CrewType crewType) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.initializeCabin(player.getShipBoard().orElseThrow(), point, crewType);
        }, false);
    }

    @Override
    public void drawCard(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.drawCard(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void activateComponent(Player player, Point point) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.activateComponent(player.getShipBoard().orElseThrow(), point);
        }, false);
    }

    @Override
    public void loseCrew(Player player, Point point) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.loseCrew(player.getShipBoard().orElseThrow(), point);
        }, false);
    }

    @Override
    public void grabReward(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.grabReward(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void placeGoods(Player player, Point point, GoodsType goodsType) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.placeGoods(player.getShipBoard().orElseThrow(), point, goodsType);
        }, false);
    }

    @Override
    public void removeGoods(Player player, Point point, GoodsType goodsType) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.removeGoods(player.getShipBoard().orElseThrow(), point, goodsType);
        }, false);
    }

    @Override
    public void loseGoods(Player player, Point point) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.loseGood(player.getShipBoard().orElseThrow(), point);
        }, false);
    }

    @Override
    public void useBattery(Player player, Point point) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.useBattery(player.getShipBoard().orElseThrow(), point);
        }, false);
    }

    @Override
    public void choosePlanet(Player player, int choice) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.choosePlanet(player.getShipBoard().orElseThrow(), choice);
        }, false);
    }

    @Override
    public void goNext(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.goNext(player.getShipBoard().orElseThrow());
        }, false);
    }

    @Override
    public void giveUp(Player player) {
        LockUtils.withLock(lock, () -> {
            checkInGame();
            game.giveUp(player.getShipBoard().orElseThrow());
        }, false);
    }

    /**
     * Sets the delay for the removal of the lobby.
     * This method is used for testing only.
     *
     * @param delay the delay in milliseconds before the lobby is removed
     */
    @VisibleForTesting
    public void setRemovalDelay(long delay) {
        this.removalDelay = delay;
    }

    /**
     * Sets the event queue for the lobby.
     * This method is used for testing only.
     *
     * @param eventQueue the event queue to set for the lobby
     */
    @VisibleForTesting
    public void setEventQueue(EventQueue<LobbyEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    /**
     * Stops the event handler for the lobby.
     * This method is visible for testing purposes only.
     */
    @VisibleForTesting
    public void stopEventHandler() {
        this.lobbyEventHandler.stop();
    }
}
