package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ChooseShipPieceStateTest {
    ShipBoard ship1;
    ShipBoard ship2;
    ChooseShipPieceState testChooseShipPieceState;
    List<Set<Point>> shipPieces;
    Game game;
    AdventureCard adventureCard;
    Deck deck;
    CountDownLatch latch;

    @BeforeEach
    void setup() throws IOException {
        ship1 = new SecondShipBoardForTesting(GameColor.BLUE){
            @Override
            public void discardComponent(Point p){
                // mock
            }
        };
        ship2 = new SecondShipBoardForTesting(GameColor.RED);
        shipPieces = new ArrayList<>();
        shipPieces.add(Set.of(new Point(7,7)));
        testChooseShipPieceState = new ChooseShipPieceState(shipPieces, ship1);
        game = new GameStub(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public AdventureState getNextState() {
                return new AdventureStateStub();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        latch = StateTransitionUtils.setupLatch(game);
        game.setCurrentState(testChooseShipPieceState);
    }

    @Test
    void gettersTest() {
        assertEquals(shipPieces,testChooseShipPieceState.getShipPieces());
        assertEquals(ship1,testChooseShipPieceState.getShipBoard());
    }

    @Test
    void skipDoesNothingWhenExpired() {
        testChooseShipPieceState.expired = true;
        testChooseShipPieceState.skip(ship1);
        StateTransitionUtils.assertNoTransition(latch,game,testChooseShipPieceState);
    }

    @Test
    void skipDoesNothingWhenOutOfTurn() {
        testChooseShipPieceState.skip(ship2);
        StateTransitionUtils.assertNoTransition(latch,game,testChooseShipPieceState);
    }

    @Test
    void skipChangesState() {
        testChooseShipPieceState.skip(ship1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void chooseShipPieceThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChooseShipPieceState.chooseShipPiece(ship2, 2));
    }

    @Test
    void chooseShipPieceRemovesPieceAndChangesAdventureState() {
        testChooseShipPieceState.chooseShipPiece(ship1, 0);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

}