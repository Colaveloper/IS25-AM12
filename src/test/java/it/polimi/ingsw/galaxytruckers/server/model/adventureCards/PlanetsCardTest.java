package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.server.model.state.ChoosePlanetState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.server.model.state.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsCardTest {
    Game game;
    PlanetsCard planetsCard;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;


    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        ships.add(new SecondShipBoardForTesting(GameColor.BLUE));
        ships.add(new SecondShipBoardForTesting(GameColor.RED));
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }
        FlightBoard flightBoard = new FlightBoard(new GameEventListenerForTesting()) {
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
                testShip = shipBoard;
                testDisplacement = displacement;
            }

            @Override
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

        };
        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };
        List<Map<GoodsType, Integer>> planets = new ArrayList<>();
        Map<GoodsType, Integer> goodsMap = new HashMap<>();
        goodsMap.put(GoodsType.RED, 2);
        planets.add(goodsMap);
        goodsMap = new HashMap<>();
        goodsMap.put(GoodsType.YELLOW, 3);
        planets.add(goodsMap);
        goodsMap = new HashMap<>();
        goodsMap.put(GoodsType.BLUE, 4);
        planets.add(goodsMap);
        planetsCard = new PlanetsCard(game, Level.SECOND,  planets, 1, 1);
        planetsCard.initialize();
    }


    @Test
    void choosePlanetUpdatesPlanetChoicesRelatedAttributes() {
        planetsCard.choosePlanet(ships.getFirst(),0);

        assertTrue(planetsCard.getPlanetChoices().entrySet().contains(Map.entry(ships.getFirst(), 0)));
        assertEquals(1,planetsCard.getPlanetChoices().size());
        assertTrue(planetsCard.getLandedShips().contains(ships.getFirst()));
        assertEquals(1,planetsCard.getLandedShips().size());
    }

    @Test
    void getNextStateReturnsChoiceStateWhenPlanetsCanBeChosen() {
        GameState testState = planetsCard.getNextState();
        assertInstanceOf(ChoosePlanetState.class, testState);
        assertFalse(planetsCard.isPlanetChoiceAllowed());
    }

    @Test
    void getNextStateWhenNoOneLandedReturnsDrawState() {
        planetsCard.getNextState();
        GameState testState = planetsCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void getNextStateWhenSomeoneLandedReturnsGoodsState() {
        planetsCard.getNextState();
        planetsCard.choosePlanet(ships.getFirst(),0);
        GameState testState = planetsCard.getNextState();
        assertInstanceOf(AddGoodsState.class, testState);
        assertEquals(ships.getFirst(), planetsCard.getCurrentShipBoard());
    }

    @Test
    void getNextStateUpdatesLandedShipPositions() {
        planetsCard.getNextState();
        planetsCard.choosePlanet(ships.getFirst(),0);
        planetsCard.getNextState();
        testDisplacement = 0;
        testShip = null;
        ShipBoard expShipBoard =  planetsCard.getCurrentShipBoard();
        planetsCard.getNextState();
        assertEquals(-planetsCard.getFlightDaysLoss(), testDisplacement);
        assertEquals(expShipBoard, testShip);
    }
}
