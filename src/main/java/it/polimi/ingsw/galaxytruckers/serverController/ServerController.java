package it.polimi.ingsw.galaxytruckers.serverController;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.GameInterface;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.ControllerEventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.util.*;
import java.util.function.Function;

/**
 * Server controller that manages game lobbies, player connections, and game sessions.
 * Handles player registration, lobby creation, player joining/leaving, and disconnections.
 * Acts as an intermediary between the client handlers and the game model.
 */
public class ServerController implements ServerControllerInterface {

    private final GameModelInterface model;
    private EventQueue<ControllerEvent> eventQueue;

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
        this.model = model;
        this.eventQueue = new EventQueue<>();
        ControllerEventHandler evenQueueHandler = new ControllerEventHandler(eventQueue);
        evenQueueHandler.start();
    }

    @VisibleForTesting
    public ServerController(GameModelInterface model, EventQueue<ControllerEvent> eventQueue) {
        this.model = model;
        this.eventQueue = eventQueue;
    }

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

    /**
     * Sends a list of active lobbies to the specified player.
     * Used when a player first connects or reconnects to the server.
     *
     * @param player The player to send the active lobbies list to
     * @param reconnect Flag indicating if this is a reconnection attempt
     */
    private void requestActiveLobbies(Player player, boolean reconnect) {
        synchronized (idToLobby) {
            List<ActiveLobbyDTO> activeLobbyDTOS = idToLobby.values().stream()
                    .filter(activeLobbies::contains)
                    .map(DtoConverter::getActiveLobby).toList();
            this.eventQueue.notifyEvent(new SetActiveLobbiesEvent(player.getNickname(), activeLobbyDTOS, reconnect));
        }
    }

    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        Lobby newLobby;
        synchronized (lock) {
            if (creator.getLobby().isPresent()) {
                throw new IllegalStateException("You are already in a lobby!");
            }
            GameInterface game = model.createGame(level, numPlayers);
            newLobby = new Lobby(game, creator, level, numPlayers, this::removeLobby);
            idToLobby.put(newLobby.getId(), newLobby);
            activeLobbies.add(newLobby);
            eventQueue.notifyEvent(new AddActiveLobbyEvent(DtoConverter.getActiveLobby(newLobby)));
            System.out.println(creator.getNickname() + " has created a new lobby: " + newLobby.getId());
        }
        return newLobby;
    }

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
        synchronized (lock){
            player.getLobby().ifPresent(lobby -> {
                System.out.println("Player " + player.getNickname() + " has left the lobby");
                lobby.notifyPlayerExit(player);
                removeLobby(lobby);
            });
        }
    }

    /**
     * Removes a lobby from the server controller.
     * Cleans up by removing the lobby from all collections and notifies
     * all related players that they are no longer in the lobby.
     * Triggers a RemoveActiveLobbyEvent to update connected clients.
     *
     * @param lobby The lobby to be removed
     */
    private void removeLobby(Lobby lobby) {
        synchronized (lock) {
            if (idToLobby.remove(lobby.getId()) != null) {
                lobby.getPlayers().forEach(Player::leaveLobby);
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
