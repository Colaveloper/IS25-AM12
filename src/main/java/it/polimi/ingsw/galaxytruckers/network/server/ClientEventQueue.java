package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;

public class ClientEventQueue extends EventQueue<Event> {
    private boolean pausedController = false;
    private boolean pausedLobby = false;

    private final Object controllerLock = new Object();
    private final Object lobbyLock = new Object();

    public void pause() {
        pausedController = true;
        pausedLobby = true;
    }

    @Override
    public void notifyEvent(Event event) {
        switch (event) {
            case ControllerEvent controllerEvent -> {
                synchronized (controllerLock) {
                    pausedController = pausedController && !controllerEvent.shouldResume();
                    if (!pausedController) super.notifyEvent(controllerEvent);
                }
            }
            case LobbyEvent lobbyEvent -> {
                synchronized (lobbyLock) {
                    pausedLobby = pausedLobby && !lobbyEvent.shouldResume();
                    if (!pausedLobby) super.notifyEvent(lobbyEvent);
                }
            }
        }
    }
}
