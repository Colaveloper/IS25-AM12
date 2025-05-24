package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StarDustCardTest {

    Game game;
    StarDustCard starDustCard;
    FlightBoard flightBoard;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    GameState testState;

    List<ShipBoard> displacedShips;


    @BeforeEach
    void setUp() {

        displacedShips = new ArrayList<>();

        ships = new ArrayList<>();

        ship1 = new SecondShipBoard(GameColor.BLUE) {
            @Override
            public int getExposedConnectorsNumber() {
                return 0;
            }
        };

        ship2 = new SecondShipBoard(GameColor.RED) {
            @Override
            public int getExposedConnectorsNumber() {
                return 2;
            }
        };

        ships.add(ship1);
        ships.add(ship2);

        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }


        flightBoard = new FlightBoard() {
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
                if (displacement < 0) {
                    displacedShips.add(shipBoard);
                }
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

        starDustCard = new StarDustCard(game, Level.SECOND,1);
        starDustCard.initialize();
    }



    @Test
    void getNextStateIsDrawStateAndDisplaceShips() {

        testState = starDustCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
        assertTrue(displacedShips.contains(ship2));
    }

}