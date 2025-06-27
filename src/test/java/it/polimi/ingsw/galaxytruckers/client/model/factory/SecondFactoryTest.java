package it.polimi.ingsw.galaxytruckers.client.model.factory;

import it.polimi.ingsw.galaxytruckers.client.model.state.SecondShipBuildingState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecondFactoryTest {

    @Test
    void createShipBuildingState() {
        SecondFactory factory = new SecondFactory();
        var state = factory.createShipBuildingState();
        assertNotNull(state, "ShipBuildingState should not be null");
        assertInstanceOf(SecondShipBuildingState.class, state, "Should be instance of SecondShipBuildingState");
    }
}