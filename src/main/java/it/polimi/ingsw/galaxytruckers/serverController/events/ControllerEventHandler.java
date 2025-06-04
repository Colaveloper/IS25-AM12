package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;

public class ControllerEventHandler extends EventQueueHandler<ControllerEvent> {

    public ControllerEventHandler(EventQueue<ControllerEvent> queue) {
        super(queue);
    }

    @Override
    public void handleEvent(ControllerEvent event) {

    }
}
