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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ChoosePlanetStateTest {
    ShipBoard ship1;
    ShipBoard ship2;
    ChoosePlanetState testChoosePlanetState;
    Consumer<Integer> choosePlanetMethod;
    Set<Integer> options;
    Game game;
    AdventureCard adventureCard;
    Deck deck;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED);
        ship2 = new SecondShipBoard(GameColor.BLUE);
        options = new HashSet<>();
        options.add(1);
        options.add(2);
        choosePlanetMethod = (num) ->{
            // mock
        };
        testChoosePlanetState = new ChoosePlanetState(ship1, choosePlanetMethod, options);
    }

    @Test
    void choosePlanetThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChoosePlanetState.choosePlanet(ship2, 2));
    }

    @Test
    void choosePlanetThrowsExceptionIfInvalidChoise(){
        assertThrows(IllegalArgumentException.class, () -> testChoosePlanetState.choosePlanet(ship1, 3));
    }

    @Test
    void choosePlanetAcceptsChoiceAndChangesAdventureState() throws IOException {
        game = new Game(Level.SECOND){
            @Override
            public Deck getDeck(){
                return deck;
            }
        };
        game.setEventListener(new GameEventListenerStub());
        adventureCard = new AdventureCard(game, Level.SECOND,1) {
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
        game.setEventListener(new GameEventListenerStub());
        testChoosePlanetState.setGame(game);
        testChoosePlanetState.choosePlanet(ship1, 2);
        assertNotEquals(testChoosePlanetState, game.getCurrentState());
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChoosePlanetState.goNext(ship2));
    }

    @Test
    void goNextChangesAdventureState() throws IOException {
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
        testChoosePlanetState.setGame(game);
        testChoosePlanetState.goNext(ship1);
        assertNotEquals(testChoosePlanetState, game.getCurrentState());
    }

}