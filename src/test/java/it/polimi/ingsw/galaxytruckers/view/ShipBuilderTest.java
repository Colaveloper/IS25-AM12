package it.polimi.ingsw.galaxytruckers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBuilderTest {

    private ComponentBank shipBuilder;
    private List<Component> revealedComponents;

    @BeforeEach
    void setUp() throws IOException {
        shipBuilder = new ComponentBank();
        revealedComponents = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            revealedComponents.add(new Component(i%4, i));
        }
    }

    @Test
    void addAndGetComponent() {
        for (Component component : revealedComponents) {
            shipBuilder.addRevealedComponent(component);
        }

        for (int i = 0; i < revealedComponents.size(); i++) {
            assertEquals(revealedComponents.get(i), shipBuilder.getComponent(i));
        }
    }

    @Test
    void getImage() {
    }

    @Test
    void getDescription() {
        for (Component component : revealedComponents) {
            shipBuilder.addRevealedComponent(component);
        }

        //assertEquals(, shipBuilder.getDescription());
    }

    @Test
    void setLastComponent() {

    }
}