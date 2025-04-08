package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SabotageCardTest {

    SabotageCard sabotageCard;
    FlightBoard flightBoard;
    FlightBoard largerFlightBoard;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    SecondShipBoard ship3;

    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;

    List<ShipBoard> largerShips;
    Map<ShipBoard, Integer> largerShipPlaces;

    GameState testState;

    List<ShipBoard> shipExploded;


    @BeforeEach
    void setUp() {

        shipExploded = new ArrayList<>();

        ships = new ArrayList<>();
        largerShips = new ArrayList<>();

        ship1 = new SecondShipBoard(Colors.BLUE) {
            @Override
            public int getCrewSize() {
                return 2;
            }

            @Override
            public void removeComponent(Point position) {
                shipExploded.add(ship1);
            }
        };

        ship2 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getCrewSize() {
                return 3;
            }

            @Override
            public void removeComponent(Point position) {
                shipExploded.add(ship2);
            }
        };

        ship3 = new SecondShipBoard(Colors.GREEN) {
            @Override
            public int getCrewSize() {
                return 1;
            }

            @Override
            public void removeComponent(Point position) {
                shipExploded.add(ship3);
            }


        };

        ships.add(ship1);
        ships.add(ship2);

        largerShips.add(ship2);
        largerShips.add(ship1);
        largerShips.add(ship3);

        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }

        largerShipPlaces = new HashMap<>();
        for (int i = 0; i < largerShips.size(); i++) {
            largerShipPlaces.put(largerShips.get(i), 10-i);
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
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };

        largerFlightBoard = new FlightBoard(null) {
            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return largerShipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return largerShips;
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

        sabotageCard = new SabotageCard (null, Level.FIRST);
        sabotageCard.initialize(flightBoard);
    }



    @Test
    void correctShipExplodes() {
        testState = sabotageCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertTrue(shipExploded.contains(ship1));
        assertFalse(shipExploded.contains(ship2));
    }

    @Test
    void otherOrderToCheck() {
        sabotageCard = new SabotageCard(null, Level.FIRST);
        sabotageCard.initialize(largerFlightBoard);
        testState = sabotageCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertTrue(shipExploded.contains(ship3));
        assertFalse(shipExploded.contains(ship1));
        assertFalse(shipExploded.contains(ship2));
    }
}