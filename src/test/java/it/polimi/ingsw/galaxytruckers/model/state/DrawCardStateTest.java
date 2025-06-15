package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrawCardStateTest {
    DrawCardState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    FlightBoard flightBoard;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup() throws IOException{
        game = new Game(Level.SECOND) {
            /**
             * Sets game state to the end game state if there are no
             * more ships playing
             */
            @Override
            public boolean endGameIfAllShipsHaveGivenUp() {
                //mock
                return false;
            }
        };
        game.setEventListener(new GameEventListenerStub());
        ship1 = new SecondShipBoard(GameColor.RED);
        ship2 = new SecondShipBoard(GameColor.BLUE);
        flightBoard = new SecondFlightBoard(2){
            @Override
            public List<ShipBoard> getOrderedShips(){
                return List.of(ship1, ship2);
            }
        };
        game.setFlightBoard(flightBoard);
        testState = new DrawCardState();
        game.setCurrentState(testState);
    }

    @Test
    void drawCardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.drawCard(ship2));
    }

    @Test
    void drawCardEndsGameIfNoCardsRemain() throws IOException{
        deck = new SecondDeck(game){
            @Override
            public boolean tryDrawCard(){
                return false;
            }
        };
        game.setDeck(deck);
        testState.drawCard(ship1);
        //TODO: assert something here
    }

    @Test
    void goNextChangesState() throws IOException{
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public void initialize(){
                // mock
            }
            @Override
            public AdventureState getNextState() {
                return new DeclareFirePowerState(ship1);
            }
        };
        deck = new SecondDeck(game){
            @Override
            public boolean tryDrawCard(){
                return true;
            }
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        game.setDeck(deck);
        testState.drawCard(ship1);
        testState.goNext(ship1);
        assertInstanceOf(DeclareFirePowerState.class,game.getCurrentState());
    }

}