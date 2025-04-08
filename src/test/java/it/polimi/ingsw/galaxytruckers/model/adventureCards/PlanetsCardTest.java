package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.model.state.ChoosePlanetState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlanetsCardTest {
    PlanetsCard planetsCard;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;


    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        ships.add(new SecondShipBoard(Colors.BLUE));
        ships.add(new SecondShipBoard(Colors.RED));
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }
        FlightBoard flightBoard = new FlightBoard(null) {
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
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Image getImage() {
                return null;
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
        planetsCard = new PlanetsCard(null, Level.SECOND, planets, 1);
        planetsCard.initialize(flightBoard);
    }


    @Test
    void choosePlanetUpdatesPlanetChoicesRelatedAttributes() {
        planetsCard.nextStep();
        ShipBoard expCurrentShipBoard = planetsCard.getCurrentShipBoard();

        List<Map<GoodsType, Integer>> expPlanets = new ArrayList<>(planetsCard.getPlanets());

        Set<Integer> expRemainingChoices = new HashSet<>(planetsCard.getRemainingChoices());
        expRemainingChoices.remove(0);

        Map<ShipBoard, Integer> expPlanetChoices = new HashMap<>(planetsCard.getPlanetChoices());
        expPlanetChoices.put(expCurrentShipBoard, 0);

        List<ShipBoard> expLandedShips = new ArrayList<>(planetsCard.getLandedShips());
        expLandedShips.add(expCurrentShipBoard);

        planetsCard.choosePlanet(0);

        assertEquals(expCurrentShipBoard, planetsCard.getCurrentShipBoard());
        assertEquals(expPlanets, planetsCard.getPlanets());
        assertEquals(expRemainingChoices, planetsCard.getRemainingChoices());
        assertEquals(expPlanetChoices, planetsCard.getPlanetChoices());
        assertEquals(expLandedShips, planetsCard.getLandedShips());
    }

    @Test
    void nextStepReturnsChoiceStateWhenPlanetsCanBeChosen() {
        for (ShipBoard ship : ships) {
            Set<Integer> expRemainingChoices = new HashSet<>(planetsCard.getRemainingChoices());
            GameState testState = planetsCard.nextStep();
            assertInstanceOf(ChoosePlanetState.class, testState);
            assertTrue(planetsCard.isPlanetChoiceAllowed());
            assertEquals(ship, planetsCard.getCurrentShipBoard());
            assertTrue(planetsCard.getLandedShips().isEmpty());
            assertTrue(planetsCard.getPlanetChoices().isEmpty());
            assertEquals(expRemainingChoices, planetsCard.getRemainingChoices());
        }
    }

    @Test
    void nextStepWhenNoOneLandedReturnsDrawState() {
        for (int i = 0; i < ships.size(); i++) {
            planetsCard.nextStep();
        }
        GameState testState = planetsCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
        assertFalse(planetsCard.isPlanetChoiceAllowed());
        // Once drawState is returned, the card internal state
        // is not relevant
    }

    @Test
    void nextStepWhenSomeoneLandedReturnsGoodsState() {
        for (int i = 0; i < ships.size(); i++) {
            planetsCard.nextStep();
        }
        planetsCard.choosePlanet(0);
        ShipBoard expCurrentShipBoard = planetsCard.getCurrentShipBoard();
        Set<Integer> expRemainingChoices = new HashSet<>(planetsCard.getRemainingChoices());
        GameState testState = planetsCard.nextStep();
        assertInstanceOf(AddGoodsState.class, testState);
        assertFalse(planetsCard.isPlanetChoiceAllowed());
        assertEquals(expCurrentShipBoard, planetsCard.getCurrentShipBoard());
        assertEquals(List.of(expCurrentShipBoard), planetsCard.getLandedShips());
        assertEquals(Map.of(expCurrentShipBoard, 0), planetsCard.getPlanetChoices());
        assertEquals(expRemainingChoices, planetsCard.getRemainingChoices());
    }

    @Test
    void nextStepUpdatesLandedShipPositions() {
        for (int i = 0; i < ships.size(); i++) {
            planetsCard.nextStep();
        }
        planetsCard.choosePlanet(0);
        planetsCard.nextStep();
        testDisplacement = 0;
        testShip = null;
        ShipBoard expShipBoard =  planetsCard.getCurrentShipBoard();
        planetsCard.nextStep();
        assertEquals(-planetsCard.getFlightDaysLoss(), testDisplacement);
        assertEquals(expShipBoard, testShip);
    }
}
