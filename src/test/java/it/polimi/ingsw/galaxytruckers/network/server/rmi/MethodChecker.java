package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

public interface MethodChecker {
    void notifyEvent(Event event);
}
