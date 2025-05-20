package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.SecondDeck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RemoveCrewStateTest {
    RemoveCrewState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(FourColors.RED){
            @Override
            public int getCrewSize(){
                return 1;
            }
            @Override
            public void loseCrew(Point pos, int num){
                // mock
            }
        };
        testState = new RemoveCrewState(2, ship1);
    }

    @Test
    void loseCrewThrowsExceptionWhenOutOfTurn(){
        ship2 = new SecondShipBoard(FourColors.BLUE);
        assertThrows(IllegalStateException.class, () -> testState.loseCrew(ship2, new Point(7,7)));
    }

    @Test
    void loseCrewSacrificesCrew(){
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(1, testState.crewSacrifice);
    }

    @Test
    void loseCrewChangesGameState() throws IOException {
        testState = new RemoveCrewState(1, ship1);
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public GameState nextStep() {
                return new AdventureState();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        testState.setGame(game);
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

    @Test
    void loseCrewChangesGameStateWhenShipHasNoCrew() throws IOException{
        ship1 = new SecondShipBoard(FourColors.RED){
            @Override
            public int getCrewSize(){
                return 0;
            }
            @Override
            public void loseCrew(Point pos, int num){
                // mock
            }
        };
        testState = new RemoveCrewState(2, ship1);
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public GameState nextStep() {
                return new AdventureState();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        testState.setGame(game);
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

    @Test
    void loseCrewDoesNotSacrificeCrewIfNoCrewToSacrificeAndChangesGameState() throws IOException{
        testState = new RemoveCrewState(0, ship1);
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public GameState nextStep() {
                return new AdventureState();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        testState.setGame(game);
        testState.loseCrew(ship1, new Point(7,7));
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }
}