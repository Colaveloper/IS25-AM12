package it.polimi.ingsw.galaxytruckers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipBuilderTest {

    private ComponentBank componentBank;
    private List<Integer> revealedComponents;

    @BeforeEach
    void setUp() throws IOException {
        componentBank = new ComponentBank();
        revealedComponents = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            revealedComponents.add(i*4);
        }
    }

    @Test
    void addAndGetComponent() throws IOException {
        for (Integer i : revealedComponents) {
            componentBank.addRevealedComponent(i);
        }
    }

    @Test
    void getImage() {
    }

    @Test
    void getDescription() {
    }

    @Test
    void setLastComponent() {

    }
}