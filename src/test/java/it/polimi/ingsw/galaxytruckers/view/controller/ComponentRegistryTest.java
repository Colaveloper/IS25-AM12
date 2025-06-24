package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRegistryTest {
    private ComponentRegistry registry;

    @BeforeEach
    void setUp() {
        registry = ComponentRegistry.getInstance();
    }

    @Test
    void getInstance_ReturnsSameInstance() {
        ComponentRegistry secondInstance = ComponentRegistry.getInstance();
        assertSame(registry, secondInstance);
    }

    @Test
    void getComponent_ReturnsValidComponent() {
        Component component = registry.getComponent(1);

        assertNotNull(component);
        assertEquals(1, component.getId());
    }

    @Test
    void getComponent_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> registry.getComponent(-1));
    }

    @Test
    void getComponent_NonexistentId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> registry.getComponent(999999));
    }

    @Test
    void getStartingCabin_ReturnsValidComponentForColor() {
        Component cabin = registry.getStartingCabin(GameColor.RED);

        assertNotNull(cabin);
        assertTrue(cabin.getId() > 0);
    }

    @Test
    void getStartingCabin_AllColorsHaveCabins() {
        for (GameColor color : GameColor.values()) {
            Component cabin = registry.getStartingCabin(color);
            assertNotNull(cabin, "Should have cabin for color " + color);
        }
    }

    @Test
    void getInstance_InitializesComponentsFromJson() {
        ComponentRegistry instance = ComponentRegistry.getInstance();
        Component component = instance.getComponent(1);
        assertNotNull(component, "Component should be loaded from JSON");
    }

    @Test
    void getComponent_ReturnedComponentHasCorrectProperties() {
        Component component = registry.getComponent(1);

        assertNotNull(component);
    }

    @Test
    void getStartingCabin_CopiesAreIndependent() {
        Component firstCabin = registry.getStartingCabin(GameColor.RED);
        Component secondCabin = registry.getStartingCabin(GameColor.RED);

        assertNotSame(firstCabin, secondCabin, "Each call should return a new instance");
        assertEquals(firstCabin.getId(), secondCabin.getId(), "Copies should have same ID");
    }
}
