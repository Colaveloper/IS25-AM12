package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

import java.util.Set;

/**
 * A surrender policy that disables surrender functionality.
 * This class implements the SurrenderPolicy interface and provides
 * methods that always return false or empty sets, indicating that
 * surrender is not allowed in the game.
 */
public class NoSurrenderPolicy implements SurrenderPolicy {

    @Override
    public boolean isSurrenderEnabled() {
        return false;
    }

    @Override
    public boolean requestSurrender(ShipBoard shipBoard, SurrenderCause cause) {
        System.err.println("Requesting surrender when surrender is disabled");
        return false;
    }

    @Override
    public Set<ShipBoard> confirmSurrender(FlightBoard flightBoard) {
        System.err.println("Confirming surrender when surrender is disabled");
        return Set.of();
    }

    @Override
    public Set<ShipBoard> getSurrenderedShips() {
        System.err.println("Getting surrendered ships when surrender is disabled");
        return Set.of();
    }
}
