package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HandleProjectileStateTest {
    HandleProjectileState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Projectile projectile;
    Dice dice;
    Game game;
    Deck deck;
    AdventureCard adventureCard;


    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public int getNumBatteries() {
                return 2;
            }
            @Override
            public boolean activateComponent(Point pos){
                return true;
            }
            @Override
            public void useBatteries(Point pos, int amount){
                // mock
            }
            @Override
            public List<Set<Point>> getConnectedSets(){
                return List.of(Set.of(new Point(7,7)), Set.of(new Point(1,1)));
            }
        };
        dice = new Dice(){};
        projectile = new SmallMeteor(dice, 1){
            @Override
            public boolean fireAt(ShipBoard ship){
                return true;
            }
        };
        testState = new HandleProjectileState(ship1, projectile){
            @Override
            public void activateComponent(ShipBoard shipBoard, Point position) {
                availablePositions = new HashSet<>();
                availablePositions.add(position);
                super.activateComponent(shipBoard, position);
            }
        };
        game = new Game(Level.SECOND);
        game.setEventListener(new GameEventListenerStub());
        testState.setGame(game);
    }

    @Test
    void activateComponentThrowsExceptionWhenAlreadyActivatedAComponent(){
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.batteriesToSpend);
        assertThrows(IllegalStateException.class, () -> testState.activateComponent(ship1, new Point(7,7)));
    }

    @Test
    void activateComponentActivatesComponent(){
        testState.activateComponent(ship1, new Point(7,7));
        assertEquals(1, testState.batteriesToSpend);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException. class, () -> testState.goNext(ship2));
    }

    @Test
    void goNextThrowsExceptionIfShipStillHasBatteriesToSpend(){
        testState.activateComponent(ship1, new Point(7,7));
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship1));
    }

    @Test
    void goNextChangesAdventureStateToChooseShipPieceState(){
        game = new Game(Level.SECOND);
        testState.setGame(game);
        testState.goNext(ship1);
        assertEquals(ChooseShipPieceState.class, game.getCurrentState().getClass());
    }

    @Test
    void goNextChangesAdventureStateWithNextStep() throws IOException {
        projectile = new SmallMeteor(dice, 1){
            @Override
            public boolean fireAt(ShipBoard ship){
                return false;
            }
        };
        game = new Game(Level.SECOND){
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
        testState = new HandleProjectileState(ship1, projectile);
        testState.setGame(game);
        testState.goNext(ship1);
        assertNotEquals(testState, game.getCurrentState());
    }

    @Test
    void goNextDoesNotSetAdventureStateToChooseShipPieceWithInsufficientShipPieces() throws IOException{
        ship1 = new SecondShipBoard(GameColor.RED){
            @Override
            public List<Set<Point>> getConnectedSets(){
                return List.of(Set.of(new Point(7,7)));
            }
        };
        game = new Game(Level.SECOND){
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
        testState = new HandleProjectileState(ship1, projectile);
        testState.setGame(game);
        testState.goNext(ship1);
        assertNotEquals(testState, game.getCurrentState());

    }

}