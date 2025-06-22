package it.polimi.ingsw.galaxytruckers.network.server;

public interface ClientHandler extends VirtualClient {
    void start();
    void pauseEvents();
    void stop();
}
