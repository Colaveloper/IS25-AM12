package it.polimi.ingsw.galaxytruckers.serverController.events;

/**
 * ModelEvent signaling that the ship building phase has started
 */
public record StartBuildingEvent() implements Event {

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
