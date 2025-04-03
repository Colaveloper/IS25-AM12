package it.polimi.ingsw.galaxytruckers.model.shipBuilding;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.JavaFXInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentBankTest extends JavaFXInitializer {
    ComponentBank componentBank;

    @BeforeEach
    void setUp() {
        componentBank = new ComponentBank();
    }

    @Test
    void loadComponentSizes() throws IOException {
        List<Component> components = componentBank.loadComponents();

        // ensure list is not null and at least one component was loaded
        assertNotNull(components);
        assertFalse(components.isEmpty());

        //ensure that all 156 components have been loaded
        assertEquals(156, components.size());

        //checking first and last component of each type in list
        assertEquals("Shield", components.get(0).getClass().getSimpleName());
        assertEquals("Shield", components.get(7).getClass().getSimpleName());

        assertEquals("LifeSupport", components.get(8).getClass().getSimpleName());
        assertEquals("LifeSupport", components.get(19).getClass().getSimpleName());

        assertEquals("DoubleCannon", components.get(20).getClass().getSimpleName());
        assertEquals("DoubleCannon", components.get(30).getClass().getSimpleName());

        assertEquals("Cannon", components.get(31).getClass().getSimpleName());
        assertEquals("Cannon", components.get(55).getClass().getSimpleName());

        assertEquals("DoubleEngine", components.get(56).getClass().getSimpleName());
        assertEquals("DoubleEngine", components.get(64).getClass().getSimpleName());

        assertEquals("Engine", components.get(65).getClass().getSimpleName());
        assertEquals("Engine", components.get(85).getClass().getSimpleName());

        //special cargo hold
        assertEquals("CargoHold", components.get(86).getClass().getSimpleName());
        assertEquals("CargoHold", components.get(94).getClass().getSimpleName());

        assertEquals("Component", components.get(95).getClass().getSimpleName());
        assertEquals("Component", components.get(102).getClass().getSimpleName());

        //normal cargo hold
        assertEquals("CargoHold", components.get(103).getClass().getSimpleName());
        assertEquals("CargoHold", components.get(117).getClass().getSimpleName());

        assertEquals("Battery", components.get(118).getClass().getSimpleName());
        assertEquals("Battery", components.get(134).getClass().getSimpleName());

        //normal cabins
        assertEquals("Cabin", components.get(135).getClass().getSimpleName());
        assertEquals("Cabin", components.get(151).getClass().getSimpleName());

        //4 main cabins
        assertEquals("Cabin", components.get(152).getClass().getSimpleName());
        assertEquals("Cabin", components.get(155).getClass().getSimpleName());
    }
}