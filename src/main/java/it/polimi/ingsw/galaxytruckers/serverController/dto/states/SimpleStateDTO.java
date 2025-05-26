package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

public record SimpleStateDTO(String playerName, Type type) implements StateDTO{
    enum Type {
        DECLARE_ENGINE_POWER,
        DECLARE_FIRE_POWER,
        DRAW_CARD,
        GRAB_REWARD
    }
}
