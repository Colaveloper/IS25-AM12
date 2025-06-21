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
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class ChoosePlanetStateTest {
    ShipBoard ship1;
    ShipBoard ship2;
    ChoosePlanetState testChoosePlanetState;
    BiConsumer<ShipBoard,Integer> choosePlanetMethod;
    Set<Integer> options;
    Game game;
    AdventureCard adventureCard;
    Deck deck;
    int choice;
    ShipBoard methodShipBoard;
    CountDownLatch latch;

    @BeforeEach
    void setup(){
        ship1 = new SecondShipBoard(GameColor.RED);
        ship2 = new SecondShipBoard(GameColor.BLUE);
        options = new HashSet<>();
        options.add(1);
        options.add(2);
        choosePlanetMethod = (ship,num) ->{
            methodShipBoard = ship;
            choice = num;
        };
        this.game = new Game(Level.SECOND);
        this.game.setFlightBoard(new FlightBoard() {
            @Override
            protected int getLoopLength() {
                return 0;
            }

            /**
             * @return a {@link List} of shipboards in the order that they appear
             * on the flightboard
             */
            @Override
            public List<ShipBoard> getOrderedShips() {
                return List.of(ship1, ship2);
            }
        });
        game.setEventListener(new GameEventListenerStub());
        latch = StateTransitionUtils.setupLatch(game);
        testChoosePlanetState = new ChoosePlanetState(choosePlanetMethod, options.size());
        game.setCurrentState(testChoosePlanetState);
    }

    @Test
    void choosePlanetThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChoosePlanetState.choosePlanet(ship2, 2));
    }

    @Test
    void choosePlanetThrowsExceptionIfInvalidChoice(){
        assertThrows(IllegalArgumentException.class, () -> testChoosePlanetState.choosePlanet(ship1, 3));
        assertThrows(IllegalArgumentException.class, () -> testChoosePlanetState.choosePlanet(ship1, -1));
    }

    @Test
    void choosePlanetAcceptsChoiceAndChangesShip() {
        testChoosePlanetState.choosePlanet(ship1, 0);
        assertEquals(0,choice);
        assertEquals(ship1,methodShipBoard);
        assertEquals(ship2, testChoosePlanetState.getCurrentShip());
        assertEquals(testChoosePlanetState,game.getCurrentState());
    }

    @Test
    void choosePlanetThrowsIfPlanerAlreadyChosen() {
        testChoosePlanetState.choosePlanet(ship1, 0);
        assertThrows(IllegalArgumentException.class, () -> testChoosePlanetState.choosePlanet(ship2, 0));
    }

    @Test
    void lastChoosePlanetChangesState() throws IOException, InterruptedException {
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
        game.setDeck(deck);
        testChoosePlanetState.choosePlanet(ship1, 0);
        testChoosePlanetState.choosePlanet(ship2,1);
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

    @Test
    void goNextThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testChoosePlanetState.goNext(ship2));
    }

    @Test
    void goNextChangesAdventureState() throws IOException, InterruptedException {
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
        game.setDeck(deck);
        testChoosePlanetState.goNext(ship1);
        testChoosePlanetState.goNext(ship2);
        for (int i = 0; i < testChoosePlanetState.getChosenPlanets().length; i++) {
            assertNull(testChoosePlanetState.getChosenPlanets()[i]);
        }
        StateTransitionUtils.assertTransition(latch,game,AdventureStateStub.class);
    }

}