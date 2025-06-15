package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;

public record PlayerDisconnectionEvent(String playerName) implements LobbyEvent {
    @Override
    public void runLobbyAction(Lobby lobby) {
        lobby.remove();
    }
}
