package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceCardTest {
    /*TODO: fix this test, it can be greatly simplified;
       no need to mock all of these game components */
    OpenSpaceCard openSpaceCard;
    List<ShipBoard> ships;
    Game game;
    GameEventListener listener;


    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        // non-zero engine power required, otherwise the ships are required to give up
        ShipBoard ship1 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getEnginePower(){
                return 1;
            }
        };
        ShipBoard ship2 = new SecondShipBoard(GameColor.BLUE) {
            @Override
            public int getEnginePower(){
                return 1;
            }
        };
        ships.addAll(List.of(ship1, ship2));
        FlightBoard flightBoardStub = new FlightBoard() {
            @Override
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {

            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return Map.of(ships.get(0),0,ships.get(1),0);
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }
        };

        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoardStub;
            }
        };

        openSpaceCard = new OpenSpaceCard(game, Level.SECOND, 1);
        openSpaceCard.initialize();
    }

    void setupShipWithNoEnginePower(){
        ships = new ArrayList<>();
        // non-zero engine power required, otherwise the ships are required to give up
        ShipBoard ship1 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getEnginePower(){
                return 1;
            }
        };
        ShipBoard ship2 = new SecondShipBoard(GameColor.BLUE) {
            @Override
            public int getEnginePower(){
                return 0;
            }
        };
        ships.addAll(List.of(ship1, ship2));
        FlightBoard flightBoardStub = new FlightBoard() {
            @Override
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {

            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return Map.of(ships.get(0),0,ships.get(1),0);
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }
        };
        listener = new GameEventListener();
        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoardStub;
            }
            @Override
            public GameEventListener getEventListener(){
                return listener;
            }
        };

        openSpaceCard = new OpenSpaceCard(game, Level.SECOND, 1);
        openSpaceCard.initialize();
    }

    @Test
    void getNextStateWhenThereArePlayersLeftReturnsActivate() {
        GameState testState = openSpaceCard.getNextState();
        assertInstanceOf(ActivateState.class, testState);
        testState = openSpaceCard.getNextState();
        assertInstanceOf(ActivateState.class, testState);
    }

    @Test
    void getNextStateWhenPlayersAreOverReturnsDrawCard() {
        openSpaceCard.getNextState();
        openSpaceCard.getNextState();
        GameState testState = openSpaceCard.getNextState();

        assertInstanceOf(DrawCardState.class, testState);
    }

    // TODO: add test for when a ship doesn't have any engine power
//    @Test
//    void shipWithNoEnginePowerIsForcedToGiveUp() throws IOException {
//        setupShipWithNoEnginePower();
//        game.connect();
//        openSpaceCard.setNextState();
//        openSpaceCard.setNextState();
//        openSpaceCard.setNextState();
//        assertEquals(Set.of(ships.get(1)), game.getGivenUpShips());
//    }
}