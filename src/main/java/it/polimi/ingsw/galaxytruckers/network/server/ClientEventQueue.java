package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.LobbyEvent;

/**
 * A thread-safe event queue for client events that can be paused, i.e.
 * when waiting for a game snapshot to be sent to the client.
 */
public class ClientEventQueue extends EventQueue<Event> {
    private boolean pausedController = false;
    private boolean pausedLobby = false;

    private final Object controllerLock = new Object();
    private final Object lobbyLock = new Object();

    /**
     * Pauses the event queue for controller events and lobby events.
     */
    public synchronized void pause() {
        pausedController = true;
        pausedLobby = true;
    }

    /**
     * Enqueues an event to the queue if it is not paused. If an event
     * should resume the queue, the queue will be resumed.
     *
     * @param event the event to enqueue
     */
    @Override
    public synchronized void notifyEvent(Event event) {
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
