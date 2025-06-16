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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
            @Override
            public SurrenderPolicy getSurrenderPolicy() {
                return new NoSurrenderPolicy();
            }

            @Override
            public Set<ShipBoard> getShipBoards() {
                return Set.of(ship1,ship2);
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
    void drawCardEndsGameIfNoCardsRemain() throws IOException, InterruptedException {
        deck = new SecondDeck(game){
            @Override
            public boolean isEmpty() {
                return true;
            }
        };
        game.setDeck(deck);
        testState = new DrawCardState();
        game.setCurrentState(testState);
        assertTrue(game.isGameOver());
    }

    @Test
    void goNextChangesState() throws IOException, InterruptedException {
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public void initialize(){
                // mock
            }
            @Override
            public AdventureState getNextState() {
                return new AdventureStateStub();
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
            @Override
            public boolean isEmpty(){
                return false;
            }
        };
        game.setDeck(deck);
        CountDownLatch latch = new CountDownLatch(1);
        game.setAfterEach(latch::countDown);
        testState.drawCard(ship1);
        testState.goNext(ship1);
        if (latch.await(1,TimeUnit.SECONDS)) assertInstanceOf(AdventureStateStub.class,game.getCurrentState());
        else throw new RuntimeException("Latch timed out");
    }

}