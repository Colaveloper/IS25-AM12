package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public interface ClientHandler extends VirtualClient {
    void setPlayer(Player player);
    void pauseEvents();
    void stop();
}
