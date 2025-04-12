package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;

public class FirePowerUpdate extends Event {


    @Override
    public void accept(EventHandler eventHandler) {
        eventHandler.handleEvent(this);
    }
}
