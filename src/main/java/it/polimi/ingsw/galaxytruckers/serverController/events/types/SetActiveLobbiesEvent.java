package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDTO;

import java.util.List;
import java.util.Optional;

public record SetActiveLobbiesEvent(String playerName, List<LobbyDTO> activeLobbies) implements ControllerEvent {
    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
