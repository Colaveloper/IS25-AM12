package it.polimi.ingsw.galaxytruckers.serverController.events;

public record PlayerDisconnectionEvent(String playerName) implements Event{
    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
