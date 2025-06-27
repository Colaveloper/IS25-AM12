package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;

public class SecondShipBoardForTesting extends SecondShipBoard {

    public SecondShipBoardForTesting() {
        super(GameColor.RED, GameEventListenerForTesting.getMock());
    }

    public SecondShipBoardForTesting(GameColor color) {
        super(color, GameEventListenerForTesting.getMock());
    }

    public GameEventListener getEventListener() {
        return this.eventListener;
    }
}