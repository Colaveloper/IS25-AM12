package it.polimi.ingsw.galaxytruckers.serverController.events;

public interface EventHandler {
    /**
     * Handles the {@code Event} passed as the argument, updating all
     * affected classes
     * @param event the event to be handled
     */
    void handleEvent(Event event);

    //TODO : add methods for specific types of events
}
