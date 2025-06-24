package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class AdventureStateTest {
    AdventureState testAdventureState;
    Game game;
    ShipBoard ship1;
    FlightBoard flightBoard;

    @BeforeEach
    void setup() {
        testAdventureState = new AdventureStateStub();
        ship1 = new SecondShipBoardForTesting(GameColor.RED);
    }

    @Test
    void giveUpThrowsExceptionWhenNotInSecondLevel() {
        game = new GameStub(Level.TEST);
        testAdventureState.setGame(game);
        assertThrows(UnsupportedOperationException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpThrowsExceptionIfShipHasAlreadyGivenUp() {
        game = new GameStub(Level.SECOND);
        testAdventureState.setGame(game);
        testAdventureState.giveUp(ship1);
        assertThrows(IllegalStateException.class, () -> testAdventureState.giveUp(ship1));
    }

    @Test
    void giveUpAddsShipToGivenUpShips() {
        game = new GameStub(Level.SECOND);
        flightBoard = new SecondFlightBoardForTesting(1);
        game.setFlightBoard(flightBoard);
        //game.connect();
        testAdventureState.setGame(game);
        testAdventureState.giveUp(ship1);
        assertEquals(Set.of(ship1), ((EnabledSurrenderPolicy) game.getSurrenderPolicy()).getRequests());
    }

    @Test
    void getNextStateUpdatesGameStateAndExpired() {
        game = new GameStub(Level.SECOND);
        try {
            game.setDeck(new Deck(game) {
                /**
                 * Returns the current card to be played.
                 *
                 * @return the deck's current card
                 */
                @Override
                public AdventureCard getCurrentCard() {
                    return new AdventureCard(game, Level.TEST, 0) {
                        @Override
                        public AdventureState getNextState() {
                            return new AdventureStateStub();
                        }
                    };
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        game.setCurrentState(testAdventureState);
        testAdventureState.getNextState();
        assertThrows(IllegalStateException.class, () -> testAdventureState.checkIfExpired());
        StateTransitionUtils.assertTransition(latch, game, AdventureStateStub.class);
    }

    @Test
    void getNextStateWhenExpiredDoesNothing() throws IOException {
        game = new GameStub(Level.SECOND);
        game.setDeck(new Deck(game) {
                         /**
                          * Returns the current card to be played.
                          *
                          * @return the deck's current card
                          */
                         @Override
                         public AdventureCard getCurrentCard() {
                             return new AdventureCard(game, Level.TEST, 0) {
                                 @Override
                                 public AdventureState getNextState() {
                                     return new AdventureStateStub();
                                 }
                             };
                         }
                     }
        );
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        game.setCurrentState(testAdventureState);
        testAdventureState.expired = true;
        testAdventureState.getNextState();
        StateTransitionUtils.assertNoTransition(latch, game, testAdventureState);
    }

    @Test
    void checkExpiredThrowsExceptionWhenExpired() {
        testAdventureState.expired = true;
        assertThrows(IllegalStateException.class, () -> testAdventureState.checkIfExpired());
    }

    @Test
    void checkExpiredWhenNotExpiredDoesNotThrow() {
        testAdventureState.expired = false;
        assertDoesNotThrow(() -> testAdventureState.checkIfExpired());
    }
}