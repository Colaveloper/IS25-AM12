package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;

public interface EventHandler<T extends Event> {
    void handleEvent(T event);
}
