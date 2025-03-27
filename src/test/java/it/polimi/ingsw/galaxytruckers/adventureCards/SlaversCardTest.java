package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlaversCardTest {

    SlaversCard slaversCard;
    FlightBoard flightBoard;
    FlightBoard flightBoardOfLosers;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    List<ShipBoard> ships;
    List<ShipBoard> loserShips;
    Map<ShipBoard, Integer> shipPlaces;
    Map<ShipBoard, Integer> loserShipPlaces;
    GameState testState;

    int goodsLoss;
    int firePowerThreshold;
    int creditPrize;
    int flightDaysLoss;

    boolean displaced;
    boolean creditGained;


    @BeforeEach
    void setUp() {

        displaced = false;

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

        ships.add(ship1);
        ships.add(ship2);

        loserShips.add(ship1);
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
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                displaced = true;
//                testShip = shipBoard;
//                testDisplacement = displacement;
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
            public Map<ShipBoard, Integer> getShipToPlace() {
                return loserShipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return loserShips;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                displaced = true;
//                testShip = shipBoard;
//                testDisplacement = displacement;
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



        slaversCard = new SlaversCard(null, Level.FIRST, flightBoard, 1, 1, 1, 1);
    }




    @Test
    void secondPlayerHasChooseState() {
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(ChoiceState.class, testState);
    }

    @Test
    void nextStepIsRemoveGoodsState() {
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(RemoveGoodsState.class, testState);
    }

    @Test
    void eachPlayerHasActivateState() {
        testState = slaversCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
        slaversCard.nextStep();//removeGoodsState
        testState = slaversCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
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
        slaversCard = new SlaversCard(null, Level.FIRST, flightBoardOfLosers, 1, 1, 1, 1);
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }





    @Test
    void chooseToGetCredits() {
        slaversCard.nextStep();//when state is ChoiceState
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(ChoiceState.class, testState);

        slaversCard.choose(true);
        assertTrue(creditGained);
        assertTrue(displaced);
    }

    @Test
    void chooseToNOTGetCredits() {
        slaversCard.nextStep();//when state is ChoiceState
        slaversCard.nextStep();
        slaversCard.nextStep();
        testState = slaversCard.nextStep();
        assertInstanceOf(ChoiceState.class, testState);

        slaversCard.choose(false);
        assertFalse(creditGained);
        assertFalse(displaced);
    }
}