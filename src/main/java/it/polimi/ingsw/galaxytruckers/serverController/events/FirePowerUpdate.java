package it.polimi.ingsw.galaxytruckers.serverController.events;

public class FirePowerUpdate extends Event {

    @Override
    public void accept(EventHandler eventHandler) {
        eventHandler.handleEvent(this);
    }
}
