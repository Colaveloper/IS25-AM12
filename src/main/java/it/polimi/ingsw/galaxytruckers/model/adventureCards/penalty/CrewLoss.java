package it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;

import java.util.Optional;

public class CrewLoss implements  Penalty {
    int crew;

    public CrewLoss(int crew) {
        this.crew = crew;
    }

    @Override
    public Optional<GameState> givePenalty(ShipBoard shipBoard, FlightBoard flightBoard) {
        if (crew == 0) {
            return Optional.empty();
        } else {
            int crewLoss = this.crew;
            this.crew = 0;
            return Optional.of(new RemoveCrewState(crewLoss, shipBoard));
        }
    }
}
