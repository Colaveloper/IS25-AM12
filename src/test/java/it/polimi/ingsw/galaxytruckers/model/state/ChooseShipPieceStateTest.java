package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListenerStub;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
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

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.BLUE){
            @Override
            public void discardComponent(Point p){
                // mock
            }
        };
        ship2 = new SecondShipBoard(GameColor.RED);
        shipPieces = new ArrayList<>();
        shipPieces.add(Set.of(new Point(7,7)));
        testChooseShipPieceState = new ChooseShipPieceState(shipPieces, ship1);
    }

    @Test
    void chooseShipPieceThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChooseShipPieceState.chooseShipPiece(ship2, 2));
    }

    @Test
    void chooseShipPieceRemovesPieceAndChangesAdventureState() throws IOException {
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        game.setEventListener(new GameEventListenerStub());
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
        CountDownLatch latch = StateTransitionUtils.setupLatch(game);
        game.setEventListener(new GameEventListenerStub());
        game.setCurrentState(testChooseShipPieceState);
        testChooseShipPieceState.chooseShipPiece(ship1, 0);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

}