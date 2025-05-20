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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GrabRewardStateTest {
    GrabRewardState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Runnable rewardMethod;
    Game game;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(FourColors.BLUE);
        ship2 = new SecondShipBoard(FourColors.RED);
        rewardMethod = () ->{
            // mock
        };
        testState = new GrabRewardState(ship1, rewardMethod);
    }

    @Test
    void grabRewardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.grabReward(ship2));
    }

    @Test
    void grabRewardRunsRewardMethodAndChangesGameState() throws IOException {
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND) {
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
        testState.grabReward(ship1);
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.goNext(ship2));
    }

    @Test
    void goNextChangesGameState() throws  IOException{
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        adventureCard = new AdventureCard(game, Level.SECOND) {
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
        testState.goNext(ship1);
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

}