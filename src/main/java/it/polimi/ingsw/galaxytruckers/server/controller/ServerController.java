package it.polimi.ingsw.galaxytruckers.server.controller;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ActiveLobbyDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.events.ControllerEventHandler;
import it.polimi.ingsw.galaxytruckers.server.controller.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.RemoveActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.server.controller.events.types.SetActiveLobbiesEvent;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;

import java.util.*;

/**
 * Server controller that manages game lobbies, player connections, and game sessions.
 * Handles player registration, lobby creation, player joining/leaving, and disconnections.
 * Acts as an intermediary between the client handlers and the game model.
 */
public class ServerController implements ServerControllerInterface {

    private final GameModelInterface model;
    private final EventQueue<ControllerEvent> eventQueue;

    private final boolean demoMode;
    private final boolean editScenario;

    private final Map<UUID, Lobby> idToLobby = new HashMap<>();
    private final Set<Lobby> activeLobbies = new HashSet<>();

    private final Object lock = new Object();

    /**
     * Creates a new server controller with the specified game model.
     * Initializes the event queue and starts the controller event handler.
     *
     * @param model The game model interface to be used by this controller
     */
    public ServerController(GameModelInterface model) {
        this(model, false, false);
    }

    /**
     * Creates a new server controller with the specified game model, and with
     * the given flags
     *
     * @param model        the game model interface to be used by this controller
     * @param demoMode     boolean indicating if the server is in demo mode
     * @param editScenario boolean indicating if the server is in edit scenario mode
     */
    public ServerController(GameModelInterface model, boolean demoMode, boolean editScenario) {
        this.model = model;
        this.demoMode = demoMode;
        this.editScenario = editScenario;
        this.eventQueue = new EventQueue<>();
        ControllerEventHandler evenQueueHandler = new ControllerEventHandler(eventQueue);
        evenQueueHandler.start();
    }

    /**
     * Constructor for testing purposes only.
     *
     * @param model      the game model interface to be used by this controller
     * @param eventQueue the event queue to be used by this controller
     */
    @VisibleForTesting
    public ServerController(GameModelInterface model, EventQueue<ControllerEvent> eventQueue) {
        this.model = model;
        this.eventQueue = eventQueue;
        this.demoMode = false;
        this.editScenario = false;
    }

    /**
     * {@inheritDoc}
     * <p>Calls {@link Player#addPlayer(String)} to add a new player</p>
     */
    @Override
    public void registerNickname(String nickname, ClientHandler clientHandler) {
        Player player;
        synchronized (lock) {
            boolean reconnect = false;
            player = Player.getPlayer(nickname);
            if (player != null && !SessionManager.getInstance().isPlayerActive(player)) {
                clientHandler.pauseEvents();
                reconnect = true;
            } else {
                player = Player.addPlayer(nickname);
            }
            clientHandler.setPlayer(player);
            SessionManager.getInstance().registerClient(player, clientHandler);
            requestActiveLobbies(player, reconnect);
            if (reconnect) {
                Player finalPlayer = player;
                player.getLobby().ifPresent(lobby -> lobby.notifyPlayerReconnection(finalPlayer));
            }
        }
    }

    private void requestActiveLobbies(Player player, boolean reconnect) {
        synchronized (idToLobby) {
            List<ActiveLobbyDTO> activeLobbyDTOS = idToLobby.values().stream()
                    .filter(activeLobbies::contains)
                    .map(DtoConverter::getActiveLobby).toList();
            this.eventQueue.notifyEvent(new SetActiveLobbiesEvent(player.getNickname(), activeLobbyDTOS, reconnect));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The lobby is created with the ServerController demoMode and
     * editScenario parameters
     * </p>
     * @throws IllegalStateException if the player is already in a lobby
     */
    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        Lobby newLobby;
        synchronized (lock) {
            if (creator.getLobby().isPresent()) {
                throw new IllegalStateException("You are already in a lobby!");
            }
            newLobby = new Lobby(demoMode, editScenario, model, creator, level, numPlayers, this::removeLobby);
            idToLobby.put(newLobby.getId(), newLobby);
            activeLobbies.add(newLobby);
            eventQueue.notifyEvent(new AddActiveLobbyEvent(DtoConverter.getActiveLobby(newLobby)));
            System.out.println(creator.getNickname() + " has created a new lobby: " + newLobby.getId());
        }
        return newLobby;
    }


    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if the player is already in a lobby
     * @throws IllegalArgumentException if the lobby with the given ID does not exist
     */
    @Override
    public LobbyInterface joinLobby(Player player, UUID lobbyID) {
        Lobby lobby;
        synchronized (lock) {
            if (player.getLobby().isPresent()) {
                throw new IllegalStateException("You are already in a lobby!");
            }
            if (!idToLobby.containsKey(lobbyID)) {
                throw new IllegalArgumentException("Lobby with ID " + lobbyID + " does not exist");
            }
            lobby = idToLobby.get(lobbyID);
            if (lobby.addPlayer(player)) {
                activeLobbies.remove(lobby);
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobbyID));
            }
            System.out.println(player.getNickname() + " joined the lobby " + lobbyID);
        }
        return lobby;
    }

    @Override
    public void handlePlayerDisconnection(Player player) {
        synchronized (lock) {
            if (SessionManager.getInstance().isPlayerActive(player)) {
                System.out.println("Player " + player.getNickname() + " has disconnected");
                player.getLobby().ifPresentOrElse(
                        lobby -> {
                            lobby.notifyPlayerDisconnection(player);
                        },
                        () -> {
                            Player.removePlayer(player.getNickname());
                            System.out.println("The player " + player.getNickname() + " has been removed");
                        });
                SessionManager.getInstance().unregisterClient(player);
            }
        }
    }

    @Override
    public void leaveLobby(Player player) {
        synchronized (lock) {
            player.getLobby().ifPresent(lobby -> {
                System.out.println("Player " + player.getNickname() + " has left the lobby " + lobby.getId());
                lobby.notifyPlayerExit(player);
                removeLobby(lobby);
            });
        }
    }

    private void removeLobby(Lobby lobby) {
        synchronized (lock) {
            if (idToLobby.remove(lobby.getId()) != null) {
                SessionManager sessionManager = SessionManager.getInstance();
                lobby.getPlayers().forEach(p -> {
                    p.leaveLobby();
                    if (!sessionManager.isPlayerActive(p)) {
                        Player.removePlayer(p.getNickname());
                        System.out.println("The player " + p.getNickname() + " has been removed");
                    }
                });
                System.out.println("Lobby " + lobby.getId() + " has been removed");
            }
            if (activeLobbies.remove(lobby)) {
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobby.getId()));
            }
        }
    }

    /**
     * Returns a copy of the map containing all lobbies indexed by their UUID.
     * This method is primarily used for testing purposes.
     *
     * @return A copy of the map associating lobby IDs to lobby instances
     */
    @VisibleForTesting
    public Map<UUID, Lobby> getIdToLobby() {
        Map<UUID, Lobby> res;
        synchronized (lock) {
            res = new HashMap<>(idToLobby);
        }
        return res;
    }
}
