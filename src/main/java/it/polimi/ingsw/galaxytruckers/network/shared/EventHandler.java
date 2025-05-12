package it.polimi.ingsw.galaxytruckers.network.shared;

import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.NewCardEvent;

import java.io.IOException;

public interface EventHandler {
    /**
     * Handles the {@code Event} passed as the argument, updating all
     * affected classes
     * @param event the event to be handled
     */
    void handleEvent(Event event); // TODO:

    void handleEvent(NewCardEvent newCardEvent) throws IOException;

    //TODO : add overloads for all types of events
}
