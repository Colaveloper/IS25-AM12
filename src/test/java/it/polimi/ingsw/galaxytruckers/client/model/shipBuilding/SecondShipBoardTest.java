package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SecondShipBoardTest {
    @Test
    void getShipArea() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Set<Point> area = board.getShipArea();
        assertTrue(area.contains(new Point(4, 7)));
        assertEquals(27, area.size());
    }

    @Test
    void finishBuilding() {
        SecondShipBoard board = new SecondShipBoard(GameColor.RED);
        Component mockComponent = Mockito.mock(Component.class);
        board.setStashedComponents(List.of(mockComponent));
        assertFalse(board.getStashedComponents().isEmpty());
        board.finishBuilding();
        assertTrue(board.getStashedComponents().isEmpty());
    }

    @Test
    void setStashedComponents() {
        SecondShipBoard board = new SecondShipBoard(GameColor.GREEN);
        Component comp1 = Mockito.mock(Component.class);
        Component comp2 = Mockito.mock(Component.class);
        List<Component> comps = List.of(comp1, comp2);
        board.setStashedComponents(comps);
        assertEquals(2, board.getStashedComponents().size());
    }

    @Test
    void stashComponent() {
        SecondShipBoard board = new SecondShipBoard(GameColor.YELLOW);
        Component mockComponent = Mockito.mock(Component.class);
        board.lastComponent = mockComponent;
        board.lastPosition = new Point(1, 1);
        board.componentMap.put(board.lastPosition, board.lastComponent);
        board.stashComponent();
        assertEquals(1, board.getStashedComponents().size());
        assertNull(board.lastComponent);
        assertNull(board.lastPosition);
        assertFalse(board.componentMap.containsKey(new Point(1, 1)));
    }

    @Test
    void stashComponentNoLastPosition() {
        SecondShipBoard board = new SecondShipBoard(GameColor.YELLOW);
        Component mockComponent = Mockito.mock(Component.class);
        board.lastComponent = mockComponent;
        board.lastPosition = null; // Ensure the if statement is false
        board.stashComponent();
        assertEquals(1, board.getStashedComponents().size());
        assertNull(board.lastComponent);
        assertNull(board.lastPosition);
    }

    @Test
    void grabStashedComponent() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Component mockComponent = Mockito.mock(Component.class);
        board.setStashedComponents(List.of(mockComponent));
        board.grabStashedComponent(0);
        assertTrue(board.getStashedComponents().isEmpty());
        assertEquals(mockComponent, board.lastComponent);
    }

    @Test
    void getStashedComponents() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        assertNotNull(board.getStashedComponents());
        assertTrue(board.getStashedComponents().isEmpty());
    }

    @Test
    void initializeCabinWithPurpleAlien() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Point pos = new Point(2, 2);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.PURPLE);
        Mockito.when(mockCabin.getNumResidents()).thenReturn(1);
        board.cabins.put(pos, mockCabin);
        int res = board.initializeCabin(pos, CrewType.PURPLE);
        assertEquals(1, res);
        board.firePower = 1;
        assertEquals(5, board.getFirePower());
    }

    @Test
    void getFirePowerWithPurpleAlienReturnsCorrectValue() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        board.firePower = 3;
        assertEquals(3, board.getFirePower());
        Point pos = new Point(10, 10);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.PURPLE);
        Mockito.when(mockCabin.getNumResidents()).thenReturn(1);
        board.cabins.put(pos, mockCabin);
        board.initializeCabin(pos, CrewType.PURPLE);
        assertEquals(7, board.getFirePower());
    }

    @Test
    void getFirePowerWhenFirePowerZero() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        board.firePower = 0;
        Point pos = new Point(20, 20);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.PURPLE);
        Mockito.when(mockCabin.getNumResidents()).thenReturn(1);
        board.cabins.put(pos, mockCabin);
        board.initializeCabin(pos, CrewType.PURPLE);
        assertEquals(0, board.getFirePower());
    }

    @Test
    void getEnginePowerWithBrownAlienReturnsCorrectValue() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        board.enginePower = 2;
        assertEquals(2, board.getEnginePower());
        Point pos = new Point(11, 11);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.BROWN);
        Mockito.when(mockCabin.getNumResidents()).thenReturn(1);
        board.cabins.put(pos, mockCabin);
        board.initializeCabin(pos, CrewType.BROWN);
        assertEquals(4, board.getEnginePower());
    }

    @Test
    void getEnginePowerWhenEnginePowerZero() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        board.enginePower = 0;
        Point pos = new Point(21, 21);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.BROWN);
        Mockito.when(mockCabin.getNumResidents()).thenReturn(1);
        board.cabins.put(pos, mockCabin);
        board.initializeCabin(pos, CrewType.BROWN);
        assertEquals(0, board.getEnginePower());
    }

    @Test
    void addBrownAlienIncrementsEnginePower() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Point pos = new Point(3, 3);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.BROWN);
        board.add(pos, mockCabin);
        board.enginePower = 1;
        assertEquals(3, board.getEnginePower());
    }

    @Test
    void removeCabinWithBrownAlienReducesEnginePower() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Point pos = new Point(4, 4);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.BROWN);
        board.add(pos, mockCabin);
        board.enginePower = 1;
        assertEquals(3, board.getEnginePower());
        board.remove(pos, mockCabin);
        assertEquals(1, board.getEnginePower());
    }

    @Test
    void loseCrewDecrementsFirePowerWithPurpleAlien() {
        SecondShipBoard board = new SecondShipBoard(GameColor.BLUE);
        Point pos = new Point(5, 5);
        Cabin mockCabin = Mockito.mock(Cabin.class);
        Mockito.when(mockCabin.getCrewType()).thenReturn(CrewType.PURPLE);
        board.cabins.put(pos, mockCabin);
        board.initializeCabin(pos, CrewType.PURPLE);
        board.firePower = 1;
        assertEquals(5, board.getFirePower());
        board.loseCrew(pos);
        assertEquals(1, board.getFirePower());
    }
}