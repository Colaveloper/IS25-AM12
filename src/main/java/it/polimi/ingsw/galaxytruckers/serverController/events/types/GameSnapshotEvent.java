package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;

import java.util.Optional;

public record GameSnapshotEvent(
        String playerName,
        LobbyDetailsDTO lobbyDetails,
        GameSnapshot gameSnapshot
) implements LobbyEvent {

    @Override
    public boolean shouldResume() {
        return true;
    }

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
