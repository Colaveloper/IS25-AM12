package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Optional;

public class FlightDaysLoss implements Penalty {
    int flightDays;

    public FlightDaysLoss(int flightDays) {
        this.flightDays = flightDays;
    }

    @Override
    public Optional<AdventureState> givePenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        flightBoard.displaceShip(shipBoard, -flightDays);
        return Optional.empty();
    }
}
