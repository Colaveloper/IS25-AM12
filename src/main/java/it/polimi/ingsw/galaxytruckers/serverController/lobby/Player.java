package it.polimi.ingsw.galaxytruckers.serverController.lobby;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Player {
    private final static Map<String, Player> nicknameToPlayer = new HashMap<>();
    private final static Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final String nickname;
    private final AtomicReference<GameColor> color = new AtomicReference<>();
    private final AtomicReference<Lobby> lobby = new AtomicReference<>();
    private final AtomicReference<ShipBoard> shipBoard = new AtomicReference<>();

    public static Player addPlayer(String nickname) {
        synchronized (nicknameToPlayer) {
            if (nicknameToPlayer.containsKey(nickname)) {
                throw new IllegalArgumentException("Nickname already exists");
            }
            Player player = new Player(nickname);
            nicknameToPlayer.put(nickname, player);
            return player;
        }
    }

    public static void removePlayer(String nickname) {
        synchronized (nicknameToPlayer) {
            nicknameToPlayer.remove(nickname);
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
        return Optional.ofNullable(this.lobby.get());
    }

    public Optional<ShipBoard> getShipBoard() {
        return Optional.ofNullable(this.shipBoard.get());
    }

    public Optional<GameColor> getColor() {
        return Optional.ofNullable(this.color.get());
    }

    protected void setLobby(Lobby lobby) {
        this.lobby.set(lobby);
    }

    protected void setColor(GameColor color) {
        this.color.set(color);
    }

    protected void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard.set(shipBoard);
        synchronized (shipToPlayer) {
            shipToPlayer.put(shipBoard, this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player player)) return false;
        return Objects.equals(nickname, player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nickname);
    }

    public void leaveLobby() {
        this.lobby.set(null);
        this.shipBoard.set(null);
        this.color.set(null);
    }
}
