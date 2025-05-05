package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public class SecondShipCorrectionState extends ShipCorrectionState {
    @Override
    protected void removeAt(ShipBoard shipBoard, Point point) {
        shipBoard.discardComponent(point);
    }

    @Override
    protected void tryStateTransition() {
        if (validShipBoards.size() == game.getShipBoards().size() && shipPieces.isEmpty()) {
            game.setCurrentState(new ShipInitializationState());
        }
    }
}
