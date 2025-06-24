package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.TestShipBoard;

public class TestShipBoardForTesting extends TestShipBoard {
    public TestShipBoardForTesting(GameColor color) {
        super(color, GameEventListenerForTesting.getMock());
    }

    public GameEventListener getEventListener() {
        return eventListener;
    }
}
