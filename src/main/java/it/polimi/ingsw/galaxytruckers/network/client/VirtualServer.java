package it.polimi.ingsw.galaxytruckers.network.client;

public interface VirtualServer {
    void drawCard() throws Exception;
    void reportError(String error) throws Exception;
}
