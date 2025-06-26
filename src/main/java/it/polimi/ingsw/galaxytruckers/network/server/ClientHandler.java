package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public interface ClientHandler extends VirtualClient {
    /**
     * Sets the player associated with this client handler.
     * @param player the player to associate with this client handler
     */
    void setPlayer(Player player);

    /**
     * Pauses the event queue for this client handler.
     */
    void pauseEvents();

    /**
     * Stops the client handler, releasing any resources it holds.
     */
    void stop();
}
