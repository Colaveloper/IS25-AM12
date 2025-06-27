package it.polimi.ingsw.galaxytruckers.client.model.shipBuilding;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ComponentBankTest {

    private Battery makeBattery(int id) {
        Map<Direction, Connector> connectors = new HashMap<>();
        return new Battery(connectors, id, 1);
    }

    @Test
    void constructorInitializesCorrectly() {
        ComponentBank bank = new ComponentBank();
        assertEquals(152, bank.getCoveredComponentsN());
        assertTrue(bank.getUncoveredComponents().isEmpty());
    }

    @Test
    void addUncoveredComponentAddsComponent() {
        ComponentBank bank = new ComponentBank();
        Battery b = makeBattery(1);
        bank.addUncoveredComponent(b);
        assertEquals(1, bank.getUncoveredComponents().size());
        assertTrue(bank.getUncoveredComponents().contains(b));
    }

    @Test
    void removeUncoveredComponentRemovesById() {
        ComponentBank bank = new ComponentBank();
        Battery b1 = makeBattery(1);
        Battery b2 = makeBattery(2);
        bank.addUncoveredComponent(b1);
        bank.addUncoveredComponent(b2);
        bank.removeUncoveredComponent(b1);
        assertEquals(1, bank.getUncoveredComponents().size());
        assertFalse(bank.getUncoveredComponents().contains(b1));
        assertTrue(bank.getUncoveredComponents().contains(b2));
    }

    @Test
    void removeCoveredComponentDecrementsCount() {
        ComponentBank bank = new ComponentBank();
        bank.removeCoveredComponent();
        assertEquals(151, bank.getCoveredComponentsN());
    }

    @Test
    void getCoveredComponentsNReturnsCorrectValue() {
        ComponentBank bank = new ComponentBank();
        assertEquals(152, bank.getCoveredComponentsN());
        bank.removeCoveredComponent();
        assertEquals(151, bank.getCoveredComponentsN());
    }

    @Test
    void getUncoveredComponentsReturnsList() {
        ComponentBank bank = new ComponentBank();
        Battery b = makeBattery(1);
        bank.addUncoveredComponent(b);
        List<Component> list = bank.getUncoveredComponents();
        assertEquals(1, list.size());
        assertEquals(b, list.get(0));
    }

    @Test
    void setCoveredComponentsNSetsValue() {
        ComponentBank bank = new ComponentBank();
        bank.setCoveredComponentsN(42);
        assertEquals(42, bank.getCoveredComponentsN());
    }

    @Test
    void setUncoveredComponentsSetsList() {
        ComponentBank bank = new ComponentBank();
        Battery b1 = makeBattery(1);
        Battery b2 = makeBattery(2);
        List<Component> newList = Arrays.asList(b1, b2);
        bank.setUncoveredComponents(newList);
        assertEquals(2, bank.getUncoveredComponents().size());
        assertTrue(bank.getUncoveredComponents().containsAll(newList));
    }
}