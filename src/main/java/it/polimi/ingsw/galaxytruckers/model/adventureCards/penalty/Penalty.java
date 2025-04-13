package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Optional;

public interface Penalty {
    public Optional<GameState> givePenalty(ShipBoard shipBoard, FlightBoard flightBoard);
}
