package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;

public class NoSurrenderPolicy implements SurrenderPolicy{

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
