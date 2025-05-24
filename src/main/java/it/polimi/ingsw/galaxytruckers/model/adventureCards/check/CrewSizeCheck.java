package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Comparator;
import java.util.Optional;

public class CrewSizeCheck implements CombatZoneCheck {
    private static CrewSizeCheck instance = null;

    private CrewSizeCheck() {}

    public static CrewSizeCheck getInstance() {
        if (instance == null) {
            instance = new CrewSizeCheck();
        }
        return instance;
    }

    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getCrewSize)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.empty();
    }
}
