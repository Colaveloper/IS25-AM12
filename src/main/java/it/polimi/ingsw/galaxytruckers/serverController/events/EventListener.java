package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

public interface EventListener<T extends Event> {
    void notifyEvent(T event);
}
