package it.polimi.ingsw.galaxytruckers.serverController;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.GameInterface;
import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ActiveLobbyDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.ControllerEventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.RemoveActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.SetActiveLobbiesEvent;
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

    public ServerController(GameModelInterface model) {
        this.model = model;
        this.eventQueue = new EventQueue<>();
        ControllerEventHandler evenQueueHandler = new ControllerEventHandler(eventQueue);
        evenQueueHandler.start();
    }

    @Override
    public void requestActiveLobbies(Player player) {
        List<ActiveLobbyDTO> activeLobbyDTOS;
        synchronized (idToLobby) {
            activeLobbyDTOS = idToLobby.values().stream()
                    .map(DtoConverter::getActiveLobby).toList();
            this.eventQueue.notifyEvent(new SetActiveLobbiesEvent(player.getNickname(), activeLobbyDTOS));
        }

    }

    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        synchronized (idToLobby) {
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
        synchronized (idToLobby) {
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

    @Override
    public void notifyPlayerReconnection(Player player) {
        System.out.println("The player " +  player.getNickname() + " has been reconnected");
        player.getLobby().ifPresent(lobby -> {
            lobby.notifyPlayerReconnection(player);
        });
    }

    @Override
    public void leaveLobby(Player player) {
        synchronized (idToLobby){
            player.getLobby().ifPresent(lobby -> {
                System.out.println("Player " + player.getNickname() + " has left the lobby");
                lobby.notifyPlayerExit(player);
                if (activeLobbies.contains(lobby)) {
                    activeLobbies.remove(lobby);
                    eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobby.getId()));
                }
                idToLobby.remove(lobby.getId());
            });
        }
    }

    private void removeLobby(Lobby lobby) {
        synchronized (idToLobby) {
            idToLobby.remove(lobby.getId());
            if (activeLobbies.remove(lobby)) {
                eventQueue.notifyEvent(new RemoveActiveLobbyEvent(lobby.getId()));
            }
            lobby.getPlayers().forEach(Player::leaveLobby);
        }
    }

    @VisibleForTesting
    protected Map<UUID, Lobby> getIdToLobby() {
        synchronized (idToLobby) {
            return new HashMap<>(idToLobby);
        }
    }
}
