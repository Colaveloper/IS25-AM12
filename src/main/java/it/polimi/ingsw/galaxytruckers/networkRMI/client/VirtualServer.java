package it.polimi.ingsw.galaxytruckers.networkRMI.client;

public interface VirtualServer {
    void drawCard() throws Exception;
    void reportError(String error) throws Exception;
}
