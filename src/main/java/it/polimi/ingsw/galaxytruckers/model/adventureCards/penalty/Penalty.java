package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;

import java.util.Optional;

public interface Penalty {
    Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard);
}
