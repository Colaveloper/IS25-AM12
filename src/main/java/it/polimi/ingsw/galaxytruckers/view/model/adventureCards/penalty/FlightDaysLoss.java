package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.Optional;

public final class FlightDaysLoss implements Penalty {
    int flightDays;

    public FlightDaysLoss(int flightDays) {
        this.flightDays = flightDays;
    }
}
