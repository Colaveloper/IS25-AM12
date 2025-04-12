package it.polimi.ingsw.galaxytruckers.controller.lobby;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.*;

public class Lobby {
    private static final Map<UUID, Lobby> idToLobby = new HashMap<>();
    private static GameModelInterface model;
    // TODO: model = new GameModel();

    private LobbyState state;

    private final UUID id;
    private final Level level;
    private final int numPlayers;
    private Game game;
    private final Object gameLock = new Object();
    private final List<Player> players;
    private final Set<Colors> chosenColors;

    public Lobby(Player creator, Level level, int numPlayers) {
        this.level = level;
        this.numPlayers = numPlayers;
        this.id = UUID.randomUUID();
        this.players = new ArrayList<>();
        this.players.add(creator);
        this.chosenColors = new HashSet<>();
        this.state = LobbyState.PREPARATION;
        synchronized (idToLobby) {
            idToLobby.put(id, this);
        }
    }

    public static Lobby getLobby(UUID id) {
        synchronized (idToLobby) {
            if (!idToLobby.containsKey(id)) {
                throw new IllegalArgumentException("There is no lobby for this id");
            }
            return idToLobby.get(id);
        }
    }

    public UUID getId() {
        return id;
    }

    public Level getLevel() {
        return level;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public List<Player> getPlayers() {
        synchronized (players) {
            return players;
        }
    }

    public LobbyState getState() {
        synchronized (this) {
            return state;
        }
    }

    public Optional<Game> getGame() {
        synchronized (gameLock) {
            return Optional.ofNullable(game);
        }
    }

    public Set<Colors> getChosenColors() {
        synchronized (chosenColors) {
            return chosenColors;
        }
    }

    public synchronized void addPlayer(Player player) {
        if (this.state != LobbyState.PREPARATION) {
            throw new IllegalStateException("The lobby is not in preparation");
        }
        synchronized (players) {
            player.setLobby(this);
            players.add(player);
        }
    }

    public void setState(LobbyState state) {
        synchronized (this) {
            this.state = state;
        }
    }

    public void setGame(Game game) {
        synchronized (gameLock) {
            this.game = game;
        }
    }

    public synchronized void chooseColor(Player player, Colors color) {
        if (state != LobbyState.PREPARATION) {
            throw new IllegalStateException("The lobby is not in preparation");
        }
        boolean mustStart = false;
        synchronized (chosenColors) {
            if (player.getColor().isPresent()) {
                throw new IllegalStateException("You have already chosen a color");
            }
            if (chosenColors.contains(color)) {
                throw new IllegalArgumentException("This color is not available");
            }
            player.setColor(color);
            chosenColors.add(color);
            if (chosenColors.size() == numPlayers) {
                mustStart = true;
            }
        }
        if (mustStart) {
            startGame();
        }
    }

    private synchronized void startGame() {
        Game game = model.createGame(this.level);
        for (Player player : players) {
            ShipBoard ship = model.addShip(game, player.getColor().orElseThrow(
                    () -> new IllegalStateException("Player " + player.getNickname() + " has not chosen a color")
            ));
            player.setShipBoard(ship);
        }
        setState(LobbyState.INGAME);
        synchronized (gameLock) {
            setGame(game);
        }
    }

}
