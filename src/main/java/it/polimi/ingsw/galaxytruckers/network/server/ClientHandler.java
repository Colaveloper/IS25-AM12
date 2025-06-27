package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.network.VirtualClient;
import it.polimi.ingsw.galaxytruckers.server.controller.lobby.Player;

/**
 * Interface representing a client handler in the Galaxy Truckers game.
 */
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
