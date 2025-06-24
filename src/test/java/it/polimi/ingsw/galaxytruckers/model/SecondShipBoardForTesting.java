package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;

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