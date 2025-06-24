package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.RemoveActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.SetActiveLobbiesEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

/**
 * Event handler for controller-related events in the Galaxy Truckers game.
 * Processes events related to lobby management, such as adding or removing lobbies
 * and setting active lobbies for players. Extends the EventQueueHandler to handle
 * the specific type of ControllerEvent.
 */
public class ControllerEventHandler extends EventQueueHandler<ControllerEvent> {

    /**
     * Creates a new ControllerEventHandler with the specified event queue.
     *
     * @param queue the event queue containing controller events to be handled
     */
    public ControllerEventHandler(EventQueue<ControllerEvent> queue) {
        super(queue);
    }

    /**
     * Handles controller events based on their specific type.
     * - For AddActiveLobbyEvent: broadcasts the event to all connected players
     * - For RemoveActiveLobbyEvent: broadcasts the event to all connected players
     * - For SetActiveLobbiesEvent: sends the event only to the specified player
     *
     * @param event the controller event to be handled
     */
    @Override
    public void handleEvent(ControllerEvent event) {
        switch (event) {
            case AddActiveLobbyEvent addActiveLobbyEvent -> {
                broadcastEvent(addActiveLobbyEvent);
            }
            case RemoveActiveLobbyEvent removeActiveLobbyEvent -> {
                broadcastEvent(removeActiveLobbyEvent);
            }
            case SetActiveLobbiesEvent setActiveLobbiesEvent -> {
                sendEvent(setActiveLobbiesEvent, setActiveLobbiesEvent.playerName());
            }
        }
    }

    /**
     * Broadcasts an event to all registered players in the game.
     * The event is sent through each player's associated client handler if available.
     *
     * @param event the controller event to broadcast to all players
     */
    private void broadcastEvent(ControllerEvent event) {
        for (Player player : Player.getAllPlayers()) {
            ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
            if (clientHandler != null) {
                clientHandler.notifyEvent(event);
            }
        }
    }

    /**
     * Sends an event to a specific player identified by their name.
     * The event is sent through the player's associated client handler if available.
     *
     * @param event the controller event to send
     * @param playerName the name of the player to receive the event
     */
    private void sendEvent(ControllerEvent event, String playerName) {
        Player player = Player.getPlayer(playerName);
        ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
        if (clientHandler != null) {
            clientHandler.notifyEvent(event);
        }
    }
}
