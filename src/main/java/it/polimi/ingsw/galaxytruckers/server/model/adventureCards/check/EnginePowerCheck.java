package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DeclareEnginePowerState;

import java.util.Comparator;
import java.util.Optional;

/**
 * Singleton class that implements the CombatZoneCheck interface to determine the weakest player based on engine power.
 * It provides a method to get the weakest player's ShipBoard and an AdventureState for declaring engine power.
 */
public class EnginePowerCheck implements CombatZoneCheck {
    private static EnginePowerCheck instance;

    private EnginePowerCheck() {}

    public static EnginePowerCheck getInstance() {
        if (instance == null) {
            instance = new EnginePowerCheck();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation determines the weakest player based on engine power.
     */
    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getEnginePower)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns an AdventureState for declaring engine power.
     */
    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.of(new DeclareEnginePowerState(shipBoard));
    }
}
