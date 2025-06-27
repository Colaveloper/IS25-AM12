package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.GameEventListener;
import it.polimi.ingsw.galaxytruckers.server.model.GameEventListenerForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.GameStub;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.LifeSupport;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.checkerframework.dataflow.qual.AssertMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class ShipInitializationStateTest {
    ShipInitializationState shipInitializationState;
    Game game;
    CountDownLatch latch;
    List<ShipBoard> ships;
    ShipBoard alienShip;
    ShipBoard otherShip;
    Map<CrewType, Set<Point>> crewTypeMap;
    GameEventListener listener = GameEventListenerForTesting.getMock();

    @BeforeEach
    void setUp() {
        crewTypeMap = new HashMap<>();
        ships = new ArrayList<>();
        game = new GameStub(Level.SECOND);
        ships.add(game.addShipBoard(GameColor.RED));
        ships.add(game.addShipBoard(GameColor.YELLOW));
        setupShip(ships.getFirst());
        otherShip = ships.getLast();
        latch = StateTransitionUtils.setupLatch(game);
        shipInitializationState = new ShipInitializationState();
        game.setCurrentState(shipInitializationState);
        for (ShipBoard ship : ships) {
            game.getFlightBoard().placeShipOnFlightBoard(ship);
        }
    }

    private void setupShip(ShipBoard shipBoard) {
        shipBoard.offerComponent(new LifeSupport(CrewType.PURPLE));
        shipBoard.placeComponent(new Point(8,7), Direction.UP);
        shipBoard.weldLastComponent();
        shipBoard.offerComponent(new Cabin());
        shipBoard.placeComponent(new Point(9,7), Direction.UP);
        shipBoard.weldLastComponent();
        shipBoard.offerComponent(new Cabin());
        shipBoard.placeComponent(new Point(8,8), Direction.UP);
        shipBoard.weldLastComponent();
        shipBoard.offerComponent(new LifeSupport(CrewType.BROWN));
        shipBoard.placeComponent(new Point(8,9), Direction.UP);
        shipBoard.weldLastComponent();
        alienShip = shipBoard;
        crewTypeMap.put(CrewType.PURPLE, new HashSet<>());
        crewTypeMap.put(CrewType.BROWN, new HashSet<>());
        crewTypeMap.get(CrewType.PURPLE).addAll(Set.of(new Point(8,8),new Point(9,7)));
        crewTypeMap.get(CrewType.BROWN).add(new Point(8,8));
    }

    @AssertMethod
    void assertTransition() {
        StateTransitionUtils.assertTransition(latch,game, DrawCardState.class);
    }

    @AssertMethod
    void assertNoTransition() {
        StateTransitionUtils.assertNoTransition(latch,game,shipInitializationState);
    }

    @Test
    void setGame() {
        assertEquals(1, shipInitializationState.getShipRelevantCabins().size());
        assertTrue(shipInitializationState.getShipRelevantCabins().containsKey(alienShip));
        assertEquals(crewTypeMap, shipInitializationState.getCrewTypeMap(alienShip));
    }

    @Test
    void skipAddsToPendingShips() {
        shipInitializationState.skip(otherShip);
        assertEquals(Set.of(otherShip), shipInitializationState.getPendingShipBoards());
        assertNoTransition();
    }

    @Test
    void skipAllChangesState() {
        for (ShipBoard ship : ships) {
            shipInitializationState.skip(ship);
        }
        assertTransition();
    }

    @Test
    void cancelSkipRemovesFromPendingShips() {
        shipInitializationState.skip(otherShip);
        shipInitializationState.cancelSkip(otherShip);
        assertTrue(shipInitializationState.getPendingShipBoards().isEmpty());
    }

    @Test
    void initializeCabinThrowsExceptionWhenInvalid() {
        assertThrows(IllegalArgumentException.class, () -> shipInitializationState.initializeCabin(otherShip,null,null));
        assertThrows(IllegalArgumentException.class, () -> shipInitializationState.initializeCabin(alienShip,null,CrewType.HUMAN));
        assertThrows(IllegalArgumentException.class, () -> shipInitializationState.initializeCabin(alienShip,new Point(0,0), CrewType.PURPLE));
    }

    @Test
    void initializeCabin() {
        Point point = crewTypeMap.get(CrewType.PURPLE).iterator().next();
        shipInitializationState.initializeCabin(alienShip,point,CrewType.PURPLE);
        assertEquals(CrewType.PURPLE, alienShip.getCabins().get(point).getCrewType());
    }

    @Test
    void initializeCabinChangesState() {
        Point point = crewTypeMap.get(CrewType.PURPLE).iterator().next();
        shipInitializationState.initializeCabin(alienShip,point,CrewType.PURPLE);
        point = crewTypeMap.get(CrewType.BROWN).iterator().next();
        shipInitializationState.initializeCabin(alienShip,point,CrewType.BROWN);
        assertTransition();
    }

    @Test
    void goNextInitializesToHumansAndChangesState() {
        shipInitializationState.goNext(alienShip);
        for (Cabin cabin : alienShip.getCabins().values()) {
            assertEquals(2, cabin.getNumResidents());
            assertEquals(CrewType.HUMAN, cabin.getCrewType());
        }
        assertTransition();
    }
}