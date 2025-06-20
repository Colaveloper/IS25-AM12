package it.polimi.ingsw.galaxytruckers.network.server;

public interface ClientHandler extends VirtualClient {
    void pause();
    void stop();
    void resume();
}
