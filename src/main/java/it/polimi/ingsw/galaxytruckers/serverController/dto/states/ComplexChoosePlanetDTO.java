package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

public record ComplexChoosePlanetDTO(
        ChoosePlanetDTO baseData,
        String[] choices
) implements ComplexStateDTO {
}
