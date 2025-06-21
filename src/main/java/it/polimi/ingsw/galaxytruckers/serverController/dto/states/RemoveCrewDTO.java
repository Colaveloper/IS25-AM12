package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

public record RemoveCrewDTO(
        String playerName,
        int crewLoss
) implements StateDTO, ComplexStateDTO {
}
