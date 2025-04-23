package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public class TestShipCorrectionState extends ShipCorrectionState {
    @Override
    protected void removeAt(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
    }

    @Override
    protected void tryStateTransition() {
        if (validShipBoards.size() == game.getShipBoards().size() && shipPieces.isEmpty()) {
            for (ShipBoard shipBoard : game.getShipBoards()) {
                for (Point p : shipBoard.getCabins().keySet()) {
                    shipBoard.initializeCabin(p, CrewType.HUMAN);
                }
            }
            game.setCurrentState(new DrawCardState());
        }
    }
}
