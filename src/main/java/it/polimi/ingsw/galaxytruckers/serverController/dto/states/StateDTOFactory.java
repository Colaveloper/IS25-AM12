package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.state.GameState;

public class StateDTOFactory {
    public static StateDTO createStateDTO(GameState gameState) {
        //TODO: define sealed class for GameStates
        return new SimpleStateDTO("capbros", SimpleStateDTO.Type.DECLARE_ENGINE_POWER);
    }
}
