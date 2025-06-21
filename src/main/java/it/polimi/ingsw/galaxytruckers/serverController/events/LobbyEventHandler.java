package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;

public class LobbyEventHandler extends EventQueueHandler<LobbyEvent> {
    private final Lobby lobby;

    public LobbyEventHandler(EventQueue<LobbyEvent> eventQueue, Lobby lobby) {
        super(eventQueue);
        this.lobby = lobby;
    }

    @Override
    public void handleEvent(LobbyEvent event) {
        event.getReceiverName().ifPresentOrElse(
                (playerName) -> sendEvent(playerName,event),
                () -> broadcastEvent(event)
        );
        event.runLobbyAction(lobby);
    }

    private List<Player> getPlayers() {
        return lobby.getPlayers();
    }

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

    private void sendEvent(String playerName, LobbyEvent event) {
        Player player = Player.getPlayer(playerName);
        ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
        if (clientHandler != null) {
            if (event.shouldResume()) clientHandler.resume();
            clientHandler.notifyEvent(event);
        } else {
            lobby.skip(player);
        }
    }
}
