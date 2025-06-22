package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public record LobbyDetailsEvent(
        String playerName,
        LobbyDetailsDTO details
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
