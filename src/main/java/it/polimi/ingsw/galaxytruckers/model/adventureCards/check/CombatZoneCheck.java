package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Optional;

public interface CombatZoneCheck {
    public ShipBoard getWeakestPlayer(FlightBoard flightBoard);
    public abstract Optional<GameState> getAvailableAction(ShipBoard shipBoard);
}
