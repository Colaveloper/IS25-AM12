package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {

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

        ship1 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return 0;
            }

            @Override
            public void gainCredits(int credits) {
                creditGained = true;
            }
        };

        ship2 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return 2;
            }

            @Override
            public void gainCredits(int credits) {
                creditGained = true;
            }
        };

        ship3 = new SecondShipBoard(Colors.RED) {
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

            @Override
            public Image getImage() {
                return null;
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

            @Override
            public Image getImage() {
                return null;
            }
        };

        slaversCard = new SlaversCard(null, Level.FIRST, 1, 1, 1, 1);
        slaversCard.initialize(flightBoard);
    }


    @Test
    void secondPlayerWinsSoChooseState() {
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);
    }

    @Test
    void firstPlayerLoses() {
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(RemoveCrewState.class, testState);
    }

    @Test
    void eachPlayerHasDeclareFirePowerState() {
        testState = slaversCard.nextStep();
        assertInstanceOf(DeclareFirePowerState.class, testState);
        slaversCard.nextStep();//removeGoodsState
        testState = slaversCard.nextStep();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void nextStepIsDrawState() {
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void noneWins() {
        slaversCard = new SlaversCard(null, Level.FIRST, 1, 1, 1, 1);
        slaversCard.initialize(flightBoardOfLosers);
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertFalse(displacedShips.contains(ship1));
        assertFalse(displacedShips.contains(ship2));
    }



    // Tests on Choose()

    @Test
    void chooseToGetCredits() {
        slaversCard.nextStep();//activate
        slaversCard.nextStep();//lose crew
        slaversCard.nextStep();//activate
        testState = slaversCard.nextStep();//choose
        assertInstanceOf(GrabRewardState.class, testState);

        slaversCard.getReward();
        assertTrue(creditGained);
        assertTrue(displacedShips.contains(ship2));
        assertFalse(displacedShips.contains(ship1));
    }

//    @Test
//    void chooseToNOTGetCredits() {
//        slaversCard.nextStep();//activate
//        slaversCard.nextStep();//lose crew
//        slaversCard.nextStep();//activate
//        testState = slaversCard.nextStep();//choose
//        assertInstanceOf(GrabRewardState.class, testState);
//
//        assertFalse(creditGained);
//        assertFalse(displacedShips.contains(ship1));
//        assertFalse(displacedShips.contains(ship2));
//    }
}