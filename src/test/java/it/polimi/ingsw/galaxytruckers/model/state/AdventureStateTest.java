package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.SecondFlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdventureStateTest {
    AdventureState testAdventureState;
    Game game;
    ShipBoard ship1;
    FlightBoard flightBoard;

    @BeforeEach
    void setup(){
        testAdventureState = new AdventureStateStub();
        ship1 = new SecondShipBoard(GameColor.RED);
    }

    @Test
    void giveUpThrowsExceptionWhenNotInSecondLevel(){
        game = new Game(Level.TEST);
        game.setEventListener(new GameEventListenerStub());
        testAdventureState.setGame(game);
        assertThrows(UnsupportedOperationException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpThrowsExceptionIfShipHasAlreadyGivenUp(){
        game = new Game(Level.SECOND){
            @Override
            public Set<ShipBoard> getGivenUpShips(){
                return Set.of(ship1);
            }
        };
        game.setEventListener(new GameEventListenerStub());
        testAdventureState.setGame(game);
        assertThrows(IllegalStateException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpAddsShipToGivenUpShips() throws IOException {
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        flightBoard = new SecondFlightBoard(1);
        game.setFlightBoard(flightBoard);
        //game.start();
        testAdventureState.setGame(game);
        testAdventureState.giveUp(ship1);
        assertEquals(Set.of(ship1), game.getGivenUpShips());
    }

}