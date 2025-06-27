package it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.server.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DeclareFirePowerState;

import java.util.Comparator;
import java.util.Optional;

/**
 * Singleton class that implements the CombatZoneCheck interface to determine the weakest player based on fire-power.
 * It provides a method to get the weakest player's ShipBoard and an AdventureState for declaring fire-power.
 */
public class FirePowerCheck implements CombatZoneCheck {
    private static FirePowerCheck instance = null;

    private FirePowerCheck() {}

    public static FirePowerCheck getInstance() {
        if (instance == null) {
            instance = new FirePowerCheck();
        }
        return instance;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation determines the weakest player based on fire-power.
     */
    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getFirePower)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns an AdventureState for declaring fire-power.
     */
    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.of(new DeclareFirePowerState(shipBoard));
    }
}
