package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.GameModelInterface;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.LobbyInterface;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ServerController implements ServerControllerInterface {
    private GameModelInterface model;
    private static final ConcurrentMap<UUID, Lobby> idToLobby = new ConcurrentHashMap<>();

    public ServerController(GameModelInterface model) {
        this.model = model;
    }

    @Override
    public Player registerNickname(String nickname) {
        return Player.addPlayer(nickname);
    }

    @Override
    public LobbyInterface newGame(Player creator, Level level, int numPlayers) {
        Lobby newLobby = new Lobby(model, creator, level, numPlayers);
        idToLobby.put(newLobby.getId(), newLobby);
        System.out.println(creator.getNickname() + " has created a new lobby: " + newLobby.getId());
        return newLobby;
    }

    @Override
    public LobbyInterface joinLobby(Player player, UUID lobbyID) {
        if (!idToLobby.containsKey(lobbyID)) {
            throw new IllegalArgumentException("Lobby with ID " + lobbyID + " does not exist");
        }
        Lobby lobby = idToLobby.get(lobbyID);
        lobby.addPlayer(player);
        System.out.println(player.getNickname() + " joined the lobby " + lobbyID);
        return lobby;
    }

    @Override
    public void leaveLobby(String nickname) {
        //TODO : decide whether to implement this method
    }
}
