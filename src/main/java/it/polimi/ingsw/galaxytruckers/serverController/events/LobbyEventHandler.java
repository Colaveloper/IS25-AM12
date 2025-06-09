package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.List;
import java.util.function.Supplier;

public class LobbyEventHandler extends EventQueueHandler<LobbyEvent> {
    private final Supplier<List<Player>> supplier;

    public LobbyEventHandler(EventQueue<LobbyEvent> eventQueue, Supplier<List<Player>> supplier) {
        super(eventQueue);
        this.supplier = supplier;
    }

    @Override
    public void handleEvent(LobbyEvent event) {
        event.getReceiverName().ifPresentOrElse(
                (playerName) -> sendEvent(playerName,event),
                () -> broadcastEvent(event)
        );
    }

    private List<Player> getPlayers() {
        return supplier.get();
    }

    private void broadcastEvent(Event event) {
        for (Player player : getPlayers()) {
            ClientHandler virtualClient = SessionManager.getInstance().getClient(player);
            if (virtualClient != null) {
                virtualClient.notifyEvent(event);
            }
        }
    }

    private void sendEvent(String playerName, Event event) {
        ClientHandler virtualClient = SessionManager.getInstance().getClient(Player.getPlayer(playerName));
        if (virtualClient != null) {
            virtualClient.notifyEvent(event);
        }
    }
}
