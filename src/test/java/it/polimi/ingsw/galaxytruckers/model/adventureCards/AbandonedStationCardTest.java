package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.model.state.GrabRewardState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedStationCardTest {

    AbandonedStationCard abandonedStationCard;
    FlightBoard flightBoard;
    FlightBoard flightBoardOfLosers;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    SecondShipBoard ship3;
    SecondShipBoard ship4;
    List<ShipBoard> ships;
    List<ShipBoard> loserShips;
    Map<ShipBoard, Integer> shipPlaces;
    Map<ShipBoard, Integer> loserShipPlaces;
    Map<GoodsType, Integer> goodsWon;
    GameState testState;

    List<ShipBoard> displacedShips;

    @BeforeEach
    void setUp() {
        displacedShips = new ArrayList<>();

        goodsWon = new HashMap<>();
        ships = new ArrayList<>();
        loserShips = new ArrayList<>();

        ship1 = new SecondShipBoard(FourColors.RED) {

            @Override
            public int getCrewSize() {
                return 1;
            }
        };

        ship2 = new SecondShipBoard(FourColors.GREEN) {
            @Override
            public int getCrewSize() {
                return 3;
            }
        };

        ship3 = new SecondShipBoard(FourColors.BLUE) {
            @Override
            public int getCrewSize() {
                return 1;
            }
        };

        ship4 = new SecondShipBoard(FourColors.BLUE) {
            @Override
            public int getCrewSize() {
                return 5;
            }
        };

        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship4);

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

        abandonedStationCard = new AbandonedStationCard(Level.FIRST, goodsWon, 2, 1);
        abandonedStationCard.initialize(flightBoard);
    }

    @Test
    void noneCanLand() {
        abandonedStationCard = new AbandonedStationCard(Level.FIRST, goodsWon, 2, 1);
        abandonedStationCard.initialize(flightBoardOfLosers);
        testState = abandonedStationCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void firstStepSkipToFirstWhoCanLand() {
        testState = abandonedStationCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);
    }


    //Test on Choose()

    @Test
    void chooseToTakeAndProcessUntilEndCard() {
        testState = abandonedStationCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);

        abandonedStationCard.getReward();
        testState = abandonedStationCard.nextStep();
        assertInstanceOf(AddGoodsState.class, testState);

        testState = abandonedStationCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);

        assertTrue(displacedShips.contains(ship2));
    }

    @Test
    void ship2DoestTakeButShip4DoesToEnd() {
        testState = abandonedStationCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);

        // abandonedStationCard.getReward();

        testState = abandonedStationCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);

        abandonedStationCard.getReward();

        testState = abandonedStationCard.nextStep();
        assertInstanceOf(AddGoodsState.class, testState);

        testState = abandonedStationCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);

        assertTrue(displacedShips.contains(ship4));
        assertFalse(displacedShips.contains(ship2));
    }
}