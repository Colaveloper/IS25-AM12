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

public class ServerController implements ServerControllerInterface {

    private final GameModelInterface model;
    private final EventQueue<ControllerEvent> eventQueue;

    private final Map<UUID, Lobby> idToLobby = new HashMap<>();
    private final Set<Lobby> activeLobbies = new HashSet<>();

    private final Object lock = new Object();

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
            newLobby = new Lobby(model, creator, level, numPlayers, this::removeLobby);
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
            }
            if (activeLobbies.remove(lobby)) {
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobby.getId()));
            }
        }
    }

    @VisibleForTesting
    public Map<UUID, Lobby> getIdToLobby() {
        Map<UUID, Lobby> res;
        synchronized (lock) {
            res = new HashMap<>(idToLobby);
        }
        return res;
    }
}
