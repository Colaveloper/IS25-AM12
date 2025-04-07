package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersCardTest {

    SmugglersCard smugglersCard;
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
    Map<GoodsType, Integer> goodsWon;


    List<ShipBoard> displacedShips;
//    boolean creditGained;


    @BeforeEach
    void setUp() {

        displacedShips = new ArrayList<>();

        ships = new ArrayList<>();
        loserShips = new ArrayList<>();

        goodsWon = new HashMap<>();

        ship1 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return 0;
            }

        };

        ship2 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return 2;
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

        smugglersCard = new SmugglersCard(null, Level.FIRST, flightBoard, 1, 1, goodsWon, 1);
    }




    @Test
    void secondPlayerHasChooseState() {
        smugglersCard.nextStep();
        smugglersCard.nextStep();
        smugglersCard.nextStep();
        testState = smugglersCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);
    }

    @Test
    void nextStepIsRemoveGoodsState() {
        smugglersCard.nextStep();
        testState = smugglersCard.nextStep();
        assertInstanceOf(RemoveGoodsState.class, testState);
    }

    @Test
    void eachPlayerHasDeclareFirePowerState() {
        testState = smugglersCard.nextStep();
        assertInstanceOf(DeclareFirePowerState.class, testState);
        smugglersCard.nextStep();//removeGoodsState
        testState = smugglersCard.nextStep();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void winnerAddsGoodsAndCardEnds() {
        smugglersCard.nextStep();//activate
        smugglersCard.nextStep();//lose goods
        smugglersCard.nextStep();//activate
        smugglersCard.nextStep();//choose to get goods = true
        testState = smugglersCard.nextStep();//add goods
        assertInstanceOf(AddGoodsState.class, testState);
        testState = smugglersCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void noneWins() {
        smugglersCard = new SmugglersCard(null, Level.FIRST, flightBoardOfLosers, 1, 1, goodsWon, 1);
        smugglersCard.nextStep();//activate (but doesn't lose)
        smugglersCard.nextStep();//activate
        smugglersCard.nextStep();//lose goods
        testState = smugglersCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void chooseToGetGoods() {
        smugglersCard.nextStep();//when state is GrabRewardState
        smugglersCard.nextStep();
        smugglersCard.nextStep();
        testState = smugglersCard.nextStep();
        assertInstanceOf(GrabRewardState.class, testState);

        smugglersCard.getReward();
        assertTrue(displacedShips.contains(ship2));
        assertFalse(displacedShips.contains(ship1));
    }
}