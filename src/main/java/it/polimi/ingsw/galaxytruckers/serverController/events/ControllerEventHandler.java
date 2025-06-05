package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.server.ClientHandler;
import it.polimi.ingsw.galaxytruckers.network.server.SessionManager;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.AddActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.ControllerEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.RemoveActiveLobbyEvent;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.SetActiveLobbiesEvent;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

public class ControllerEventHandler extends EventQueueHandler<ControllerEvent> {

    public ControllerEventHandler(EventQueue<ControllerEvent> queue) {
        super(queue);
    }

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

    private void broadcastEvent(ControllerEvent event) {
        for (Player player : Player.getAllPlayers()) {
            ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
            if (clientHandler != null) {
                clientHandler.notifyEvent(event);
            }
        }
    }

    private void sendEvent(ControllerEvent event, String playerName) {
        Player player = Player.getPlayer(playerName);
        ClientHandler clientHandler = SessionManager.getInstance().getClient(player);
        if (clientHandler != null) {
            clientHandler.notifyEvent(event);
        }
    }
}
