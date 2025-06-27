package it.polimi.ingsw.galaxytruckers.server.model;

public class SecondFlightBoardForTesting extends SecondFlightBoard {
    public SecondFlightBoardForTesting(int shipsN) {
        super(shipsN, GameEventListenerForTesting.getMock());
    }

    public GameEventListener getEventListener() {
        return gameEventListener;
    }
}
