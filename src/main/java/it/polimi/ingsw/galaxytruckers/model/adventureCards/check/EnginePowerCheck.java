package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareEnginePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareFirePowerState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Comparator;
import java.util.Optional;

public class EnginePowerCheck implements CombatZoneCheck {
    private static EnginePowerCheck instance;

    private EnginePowerCheck() {}

    public static EnginePowerCheck getInstance() {
        if (instance == null) {
            instance = new EnginePowerCheck();
        }
        return instance;
    }

    @Override
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard) {
        return flightBoard.getShipToPlace().keySet().stream()
                .min(Comparator
                        .comparingInt(ShipBoard::getEnginePower)
                        .thenComparing(flightBoard.getShipToPlace()::get, Comparator.reverseOrder()))
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public Optional<AdventureState> getAvailableAction(ShipBoard shipBoard) {
        return Optional.of(new DeclareEnginePowerState(shipBoard));
    }
}
