package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.io.Serializable;
import java.util.Map;

public interface ComponentPayload extends Serializable {
    //Cabin
    default CrewType crewType() {
        return null;
    }
    default int numResidents() {
        return 0;
    }

    //Activatables
    default boolean active() {
        return false;
    }

    //Cargo Holds
    default Map<GoodsType, Integer> goods() {
        return null;
    }

    //Batteries
    default int numBatteries() {
        return 0;
    }
}
