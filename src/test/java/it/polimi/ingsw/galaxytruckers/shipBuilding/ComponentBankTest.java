package it.polimi.ingsw.galaxytruckers.shipBuilding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComponentBankTest {

    @Test
    void loadComponents() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/main/resources/tiles.json");

        List<Component> components = ComponentBank.loadComponents(jsonFile);

        //ensure list is not null and at least one component was loaded
        assertNotNull(components);
        assertFalse(components.isEmpty());

        //check first component type
        assertEquals("Shield", components.get(0).getClass().getSimpleName());
    }
}