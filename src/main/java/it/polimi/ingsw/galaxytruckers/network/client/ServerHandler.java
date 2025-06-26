package it.polimi.ingsw.galaxytruckers.network.client;

/**
 * Interface for handling server connections in the Galaxy Truckers game.
 * It extends the VirtualServer interface to include methods for reconnecting
 * and dropping connections.
 */
public interface ServerHandler extends VirtualServer {
    /**
     * Attempts to reconnect to the server.
     * @return true if the reconnection was successful, false otherwise.
     */
    boolean reconnect();

    /**
     * Drops the current connection to the server.
     * This method should be called to test that the system can handle
     * disconnections and reconnections gracefully.
     */
    void dropConnection();
}
