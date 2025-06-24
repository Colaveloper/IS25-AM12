package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

/**
 * Event handler for lobby-related events in the Galaxy Truckers game.
 * Manages distribution of events to players within a specific lobby and
 * executes lobby actions associated with events. Extends the EventQueueHandler
 * to process events of type LobbyEvent.
 */
public class LobbyEventHandler extends EventQueueHandler<LobbyEvent> {
    private final Lobby lobby;

    /**
     * Creates a new LobbyEventHandler with the specified event queue and lobby.
     *
     * @param eventQueue the event queue containing lobby events to be handled
     * @param lobby the lobby instance associated with this handler
     */
    public LobbyEventHandler(EventQueue<LobbyEvent> eventQueue, Lobby lobby) {
        super(eventQueue);
        this.lobby = lobby;
    }

    /**
     * Handles lobby events by sending them to the appropriate players and executing
     * any associated lobby actions. If the event has a specific receiver, it's sent
     * only to that player; otherwise, it's broadcast to all players in the lobby.
     *
     * @param event the lobby event to be handled
     */
    @Override
    public void handleEvent(LobbyEvent event) {
        event.getReceiverName().ifPresentOrElse(
                (playerName) -> sendEvent(playerName,event),
                () -> broadcastEvent(event)
        );
        event.runLobbyAction(lobby);
    }

    /**
     * Gets the list of players currently in the lobby.
     *
     * @return a list of players in the lobby
     */
    private List<Player> getPlayers() {
        return lobby.getPlayers();
    }

    /**
     * Broadcasts an event to all players in the lobby.
     * If a player doesn't have an associated client handler (likely disconnected),
     * the player's turn is skipped.
     *
     * @param event the event to broadcast to all players
     */
    private void broadcastEvent(LobbyEvent event) {
        for (Player player : getPlayers()) {
            ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
            if (clientHandler != null) {
                clientHandler.notifyEvent(event);
            } else {
                lobby.skip(player);
            }
        }
    }

    /**
     * Sends an event to a specific player identified by their name.
     * If the player doesn't have an associated client handler (likely disconnected),
     * the player's turn is skipped.
     *
     * @param playerName the name of the player to receive the event
     * @param event the event to send to the player
     */
    private void sendEvent(String playerName, LobbyEvent event) {
        Player player = Player.getPlayer(playerName);
        ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
        if (clientHandler != null) {
            clientHandler.notifyEvent(event);
        } else {
            lobby.skip(player);
        }
    }
}
