package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
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
        testAdventureState = new AdventureState();
        ship1 = new SecondShipBoard(GameColor.RED);
    }

    @Test
    void giveUpThrowsExceptionWhenNotInSecondLevel(){
        game = new Game(Level.TEST);
        testAdventureState.setGame(game);
        assertThrows(IllegalStateException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpThrowsExceptionIfShipHasAlreadyGivenUp(){
        game = new Game(Level.SECOND){
            @Override
            public Set<ShipBoard> getGivenUpShips(){
                return Set.of(ship1);
            }
        };
        testAdventureState.setGame(game);
        assertThrows(IllegalStateException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpAddsShipToGivenUpShips() throws IOException {
        game = new Game(Level.SECOND);
        flightBoard = new SecondFlightBoard(Set.of(ship1));
        game.setFlightBoard(flightBoard);
        //game.start();
        testAdventureState.setGame(game);
        testAdventureState.giveUp(ship1);
        assertEquals(Set.of(ship1), game.getGivenUpShips());
    }

}