package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentBankTest {
    ComponentBank componentBank;

    @BeforeEach
    void setUp() {
        componentBank = new ComponentBank();
        componentBank.initialize();
    }

    @Test
    void drawRandComponentReturnsRandomComponent() {
        int numCovered =  componentBank.getNumCovered();
        componentBank.drawRandComponent();
        assertEquals(numCovered-1,componentBank.getNumCovered());
    }

    @Test
    void addToUncoveredComponentsAddsToUncoveredComponents() {
        Component component = new Component();
        componentBank.addToUncoveredComponents(component);
        assertEquals(component, componentBank.getUncoveredComponents().get(component.getId()));
    }

    @Test
    void addToCoveredComponentsAddsToCoveredComponents() {
        Component component = new Component();
        componentBank.returnCoveredComponent(component);
        assertEquals(component, componentBank.getCoveredComponents().getLast());
    }

    @Test
    void removeUncoveredThrowsWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> componentBank.removeUncoveredComponent(-1));
    }

    @Test
    void removeUncoveredRemovesComponent() {
        Component component = componentBank.drawRandComponent();
        componentBank.addToUncoveredComponents(component);
        assertEquals(component, componentBank.removeUncoveredComponent(component.getId()));
        assertFalse(componentBank.getUncoveredComponents().containsKey(component.getId()));
    }
}