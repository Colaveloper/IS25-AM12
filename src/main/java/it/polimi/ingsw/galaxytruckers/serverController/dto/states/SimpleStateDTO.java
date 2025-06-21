package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

public record SimpleStateDTO(
        String playerName,
        StateDTOType type
) implements StateDTO, ComplexStateDTO{
}
