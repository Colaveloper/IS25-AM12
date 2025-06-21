package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import java.util.Set;

public enum StateDTOType {
    DECLARE_ENGINE_POWER,
    DECLARE_FIRE_POWER,
    DRAW_CARD,
    GRAB_REWARD;

    private final static Set<StateDTOType> complexTypes = Set.of(DECLARE_ENGINE_POWER, DECLARE_FIRE_POWER, GRAB_REWARD);

    public boolean isComplex() {
        StateDTOType type = this;
        return complexTypes.contains(type);
    }
}
