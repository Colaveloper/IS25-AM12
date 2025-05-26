package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;

import java.util.Optional;

public class CrewLoss implements  Penalty {
    int crewToLose;

    public CrewLoss(int crewToLose) {
        this.crewToLose = crewToLose;
    }

    @Override
    public Optional<AdventureState> inflictPenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (crewToLose == 0) {
            return Optional.empty();
        } else {
            int crewToLose = this.crewToLose;
            this.crewToLose = 0;
            return Optional.of(new RemoveCrewState(crewToLose, shipBoard));
        }
    }
}
