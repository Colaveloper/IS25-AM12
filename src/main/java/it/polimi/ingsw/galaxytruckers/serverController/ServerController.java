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

    @Override
    public <T extends ClientHandler> T registerNickname(String nickname, Function<Player, T> handlerFunction) {
        Player player;
        T clientHandler;
        synchronized (lock) {
            boolean reconnect = false;
            player = Player.getPlayer(nickname);
            if (player != null && !SessionManager.getInstance().isPlayerActive(player)) {
                clientHandler = handlerFunction.apply(player);
                clientHandler.pauseEvents();
                reconnect = true;
            } else {
                player = Player.addPlayer(nickname);
                clientHandler = handlerFunction.apply(player);
            }
            SessionManager.getInstance().registerClient(player, clientHandler);
            clientHandler.start();
            requestActiveLobbies(player, reconnect);
            if (reconnect) {
                Player finalPlayer = player;
                player.getLobby().ifPresent(lobby -> lobby.notifyPlayerReconnection(finalPlayer));
            }
        }
        return clientHandler;
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
        synchronized (lock) {
            if (creator.getLobby().isPresent()) {
                throw new IllegalStateException("You are already in a lobby!");
            }
            GameInterface game = model.createGame(level, numPlayers);
            Lobby newLobby = new Lobby(game, creator, level, numPlayers, this::removeLobby);
            idToLobby.put(newLobby.getId(), newLobby);
            activeLobbies.add(newLobby);
            eventQueue.notifyEvent(new AddActiveLobbyEvent(DtoConverter.getActiveLobby(newLobby)));
            System.out.println(creator.getNickname() + " has created a new lobby: " + newLobby.getId());
            return newLobby;
        }
    }

    @Override
    public LobbyInterface joinLobby(Player player, UUID lobbyID) {
        synchronized (lock) {
            if (player.getLobby().isPresent()) {
                throw new IllegalStateException("You are already in a lobby!");
            }
            if (!idToLobby.containsKey(lobbyID)) {
                throw new IllegalArgumentException("Lobby with ID " + lobbyID + " does not exist");
            }
            Lobby lobby = idToLobby.get(lobbyID);
            if (lobby.addPlayer(player)) {
                activeLobbies.remove(lobby);
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobbyID));
            }
            System.out.println(player.getNickname() + " joined the lobby " + lobbyID);
            return lobby;
        }
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
                lobby.getPlayers().forEach(Player::leaveLobby);
            }
            if (activeLobbies.remove(lobby)) {
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobby.getId()));
            }
        }
    }

    @VisibleForTesting
    public Map<UUID, Lobby> getIdToLobby() {
        synchronized (lock) {
            return new HashMap<>(idToLobby);
        }
    }
}
