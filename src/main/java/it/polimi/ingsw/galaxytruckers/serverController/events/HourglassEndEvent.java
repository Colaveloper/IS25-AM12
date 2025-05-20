package it.polimi.ingsw.galaxytruckers.serverController.events;

public record HourglassEndEvent() implements Event {
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
