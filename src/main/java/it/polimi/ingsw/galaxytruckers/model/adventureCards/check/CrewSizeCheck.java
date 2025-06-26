package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Comparator;
import java.util.Optional;

/**
 * Singleton class that implements the CombatZoneCheck interface to determine the weakest player based on crew size.
 * It provides a method to get the weakest player's ShipBoard and an empty optional of AdventureState.
 */
public class CrewSizeCheck implements CombatZoneCheck {
    private static CrewSizeCheck instance = null;

    private CrewSizeCheck() {}

    public static CrewSizeCheck getInstance() {
        if (instance == null) {
            instance = new CrewSizeCheck();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation determines the weakest player based on crew size.
     */
    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getCrewSize)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns an empty Optional, as no specific state is required.
     */
    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.empty();
    }
}
