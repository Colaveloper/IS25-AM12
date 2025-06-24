package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.util.Set;

public interface SurrenderPolicy {
    boolean isSurrenderEnabled();
    boolean requestSurrender(ShipBoard shipBoard, SurrenderCause cause);
    Set<ShipBoard> confirmSurrender(FlightBoard flightBoard);
    Set<ShipBoard> getSurrenderedShips();
}
