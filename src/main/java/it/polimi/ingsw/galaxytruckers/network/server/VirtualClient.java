package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.events.Event;

public interface VirtualClient {
    void notifyEvent(Event event);
}
