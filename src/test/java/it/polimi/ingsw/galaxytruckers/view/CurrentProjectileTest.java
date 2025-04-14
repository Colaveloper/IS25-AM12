package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrentProjectileTest {

    CurrentProjectile currentProjectile;

    @BeforeEach
    void setUp() {
        currentProjectile = new CurrentProjectile(ProjectileType.BIGMETEOR, 1, 7);
    }
    @Test
    void getImage() {
        assertNull(currentProjectile.getImage());
    }

    @Test
    void getDescription() {
        List<String> expected = List.of(
                "a big meteor is approaching on row 7 from the left!"
        );
        assertEquals(expected, currentProjectile.getDescription());
    }
}