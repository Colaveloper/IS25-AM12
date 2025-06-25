package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareFirePowerState;

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

    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getFirePower)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.of(new DeclareFirePowerState(shipBoard));
    }
}
