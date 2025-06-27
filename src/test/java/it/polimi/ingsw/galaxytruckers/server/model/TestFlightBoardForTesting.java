package it.polimi.ingsw.galaxytruckers.server.model;

public class TestFlightBoardForTesting extends TestFlightBoard {
    public TestFlightBoardForTesting(int shipsN) {
        super(shipsN, GameEventListenerForTesting.getMock());
    }

    public GameEventListener getEventListener() {
        return gameEventListener;
    }
}
