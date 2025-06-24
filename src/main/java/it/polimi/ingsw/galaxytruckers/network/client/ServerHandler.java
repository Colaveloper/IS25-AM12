package it.polimi.ingsw.galaxytruckers.network.client;

public interface ServerHandler extends VirtualServer {
    boolean reconnect();
    void dropConnection();
}
