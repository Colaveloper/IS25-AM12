package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {

    Game game;
    SlaversCard slaversCard;
    FlightBoard flightBoard;
    FlightBoard flightBoardOfLosers;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    SecondShipBoard ship3;
    List<ShipBoard> ships;
    List<ShipBoard> loserShips;
    Map<ShipBoard, Integer> shipPlaces;
    Map<ShipBoard, Integer> loserShipPlaces;
    GameState testState;

    List<ShipBoard> displacedShips;
    boolean creditGained;


    @BeforeEach
    void setUp() {

        displacedShips = new ArrayList<>();

        ships = new ArrayList<>();
        loserShips = new ArrayList<>();

        ship1 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 0;
            }

            @Override
            public void gainCredits(int credits) {
                creditGained = true;
            }
        };

        ship2 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 2;
            }

            @Override
            public void gainCredits(int credits) {
                creditGained = true;
            }
        };

        ship3 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 1;
            }

        };

        ships.add(ship1);
        ships.add(ship2);

        loserShips.add(ship3);
        loserShips.add(ship1);

        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }

        loserShipPlaces = new HashMap<>();
        for (int i = 0; i < loserShips.size(); i++) {
            loserShipPlaces.put(loserShips.get(i), 10-i);
        }

        flightBoard = new FlightBoard(null) {
            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                displacedShips.add(shipBoard);
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }
        };

        flightBoardOfLosers = new FlightBoard(null) {
            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return loserShipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return loserShips;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                displacedShips.add(shipBoard);
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }
        };
        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        slaversCard = new SlaversCard(game, Level.SECOND, 1, 1, 1, 1,1);
        slaversCard.initialize();
    }


    @Test
    void secondPlayerWinsSoChooseState() {
        slaversCard.getNextState();
        slaversCard.getNextState();
        slaversCard.getNextState();
        testState = slaversCard.getNextState();
        assertInstanceOf(GrabRewardState.class, testState);
    }

    @Test
    void firstPlayerLoses() {
        slaversCard.getNextState();
        testState = slaversCard.getNextState();
        assertInstanceOf(RemoveCrewState.class, testState);
    }

    @Test
    void eachPlayerHasDeclareFirePowerState() {
        testState = slaversCard.getNextState();
        assertInstanceOf(DeclareFirePowerState.class, testState);
        slaversCard.getNextState();//removeGoodsState
        testState = slaversCard.getNextState();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void getNextStateIsDrawState() {
        slaversCard.getNextState();
        slaversCard.getNextState();
        slaversCard.getNextState();
        slaversCard.getNextState();
        testState = slaversCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void noneWins() {
        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoardOfLosers;
            }
        };
        slaversCard = new SlaversCard(game, Level.SECOND, 1, 1, 1, 1,1);
        slaversCard.initialize();
        slaversCard.getNextState();
        slaversCard.getNextState();
        slaversCard.getNextState();
        testState = slaversCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
        assertFalse(displacedShips.contains(ship1));
        assertFalse(displacedShips.contains(ship2));
    }



    // Tests on Choose()

    @Test
    void chooseToGetCredits() {
        slaversCard.getNextState();//activate
        slaversCard.getNextState();//lose crew
        slaversCard.getNextState();//activate
        testState = slaversCard.getNextState();//choose
        assertInstanceOf(GrabRewardState.class, testState);

        slaversCard.getReward();
        assertTrue(creditGained);
        assertTrue(displacedShips.contains(ship2));
        assertFalse(displacedShips.contains(ship1));
    }
}