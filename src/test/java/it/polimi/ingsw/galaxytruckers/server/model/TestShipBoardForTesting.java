package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.TestShipBoard;

public class TestShipBoardForTesting extends TestShipBoard {
    public TestShipBoardForTesting(GameColor color) {
        super(color, GameEventListenerForTesting.getMock());
    }

    public GameEventListener getEventListener() {
        return eventListener;
    }
}
