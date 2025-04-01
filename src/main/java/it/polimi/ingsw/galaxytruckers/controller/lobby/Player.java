package it.polimi.ingsw.galaxytruckers.controller.lobby;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Player {
    private final static Map<String, Player> nicknameToPlayer = new HashMap<>();
    private final static Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final String nickname;
    private Colors color;
    private final Object colorLock = new Object();
    private Lobby lobby;
    private final Object lobbyLock = new Object();
    private ShipBoard shipBoard;
    private final Object shipBoardLock = new Object();

    public static void addPlayer(String nickname) {
        synchronized (nicknameToPlayer) {
            if (nicknameToPlayer.containsKey(nickname)) {
                throw new IllegalArgumentException("Nickname already exists");
            }
            nicknameToPlayer.put(nickname, new Player(nickname));
        }
    }

    public static Player getPlayer(String nickname) {
        synchronized (nicknameToPlayer) {
            if (!nicknameToPlayer.containsKey(nickname)) {
                throw new IllegalArgumentException("Nickname does not exist");
            }
            return nicknameToPlayer.get(nickname);
        }
    }

    public static Player getPlayer(ShipBoard shipBoard) {
        synchronized (shipToPlayer) {
            if (!shipToPlayer.containsKey(shipBoard)) {
                throw new IllegalArgumentException("There is no player for this ship board");
            }
            return shipToPlayer.get(shipBoard);
        }
    }

    private Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public Optional<Lobby> getLobby() {
        synchronized (lobbyLock) {
            return Optional.ofNullable(lobby);
        }
    }

    public Optional<ShipBoard> getShipBoard() {
        synchronized (shipBoardLock) {
            return Optional.ofNullable(shipBoard);
        }
    }

    public Optional<Colors> getColor() {
        synchronized (colorLock) {
            return Optional.ofNullable(color);
        }
    }

    protected void setLobby(Lobby lobby) {
        synchronized (lobbyLock) {
            this.lobby = lobby;
        }
    }

    protected void setColor(Colors color) {
        synchronized (colorLock) {
            this.color = color;
        }
    }

    protected void setShipBoard(ShipBoard shipBoard) {
        synchronized (shipBoardLock) {
            this.shipBoard = shipBoard;
            synchronized (shipToPlayer) {
                shipToPlayer.put(shipBoard, this);
            }
        }
    }
}
