package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.shared.enums.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class DrawCardStateTest {
    DrawCardState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    FlightBoard flightBoard;
    Deck deck;
    AdventureCard adventureCard;
    CountDownLatch latch;
    SurrenderPolicyStub surrenderPolicy;

    @BeforeEach
    void setup() throws IOException{
        surrenderPolicy = new SurrenderPolicyStub();
        surrenderPolicy.surrenderEnabled = false;
        game = new GameStub(Level.SECOND) {
            @Override
            public SurrenderPolicy getSurrenderPolicy() {
                return surrenderPolicy;
            }

            @Override
            public Set<ShipBoard> getShipBoards() {
                return Set.of(ship1,ship2);
            }
        };
        latch = StateTransitionUtils.setupLatch(game);
        ship1 = new SecondShipBoardForTesting(GameColor.RED);
        ship2 = new SecondShipBoardForTesting(GameColor.BLUE);
        flightBoard = new SecondFlightBoardForTesting(2){
            @Override
            public List<ShipBoard> getOrderedShips(){
                return List.of(ship1, ship2);
            }
        };
        game.setFlightBoard(flightBoard);
        testState = new DrawCardState();
        game.setCurrentState(testState);
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
            public void drawCard(){
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
    }

    @Test
    void getShipBoardReturnsLeader() {
        assertEquals(ship1, testState.getShipBoard());
    }

    @Test
    void setGameConfirmsSurrenderIfEnabled() {
        assertFalse(surrenderPolicy.surrenderConfirmed);
        surrenderPolicy.surrenderEnabled = true;
        testState.setGame(game);
        assertTrue(surrenderPolicy.surrenderConfirmed);
    }

    @Test
    void skipDoesNothingWhenExpired() {
        testState.expired = true;
        testState.skip(ship1);
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @Test
    void skipDoesNothingWhenOutOfTurn() {
        testState.skip(ship2);
        StateTransitionUtils.assertNoTransition(latch,game,testState);
    }

    @Test
    void skipDrawsAndChangesState() {
        testState.skip(ship1);
        Mockito.verify(game.getEventListener()).notifyNewCardEvent(adventureCard);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void skipAfterDrawChangesState() {
        testState.drawCard(ship1);
        testState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void drawCardUpdatesState() {
        testState.drawCard(ship1);
        assertTrue(testState.hasDrawn());
        Mockito.verify(game.getEventListener()).notifyNewCardEvent(adventureCard);
    }

    @Test
    void drawCardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.drawCard(ship2));
    }

    @Test
    void drawCardThrowsExceptionWhenAlreadyDrawn() {
        testState.drawCard(ship1);
        assertThrows(IllegalStateException.class, () -> testState.drawCard(ship1));
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
    void goNextThrowsIfOutOfTurn() {
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship2));
    }

    @Test
    void goNextThrowsIfNotDrawn() {
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship1));
    }

    @Test
    void goNextChangesState() {
        testState.drawCard(ship1);
        testState.goNext(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }
}

class SurrenderPolicyStub implements SurrenderPolicy {
    boolean surrenderEnabled = false;
    boolean surrenderConfirmed = false;

    @Override
    public boolean isSurrenderEnabled() {
        return surrenderEnabled;
    }

    @Override
    public boolean requestSurrender(ShipBoard shipBoard, SurrenderCause cause) {
        return false;
    }

    @Override
    public Set<ShipBoard> confirmSurrender(FlightBoard flightBoard) {
        surrenderConfirmed = true;
        return Set.of();
    }

    @Override
    public Set<ShipBoard> getSurrenderedShips() {
        return Set.of();
    }
}