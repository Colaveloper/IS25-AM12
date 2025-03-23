package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.Dice;
import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.SecondFlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.BigMeteor;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmCardTest extends AdventureCardTest {
    AdventureCard meteorSwarmCard;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;
    List<Projectile> projectiles;

    SecondShipBoard ship1;
    SecondShipBoard ship2;

    Component component;

    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        ship1 = new SecondShipBoard(componentBank, Colors.BLUE);
        ship2 = new SecondShipBoard(componentBank, Colors.RED);
        ships.add(ship1);
        ships.add(ship2);
        buildLargeShip(ship1);
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }
        FlightBoard flightBoard = new FlightBoard(null) {
            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                testShip = shipBoard;
                testDisplacement = displacement;
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };

        projectiles = new ArrayList<>(List.of(new SmallMeteor(()->6,0),
                new SmallMeteor(()->9,0),
                new BigMeteor(()->8, 1),
                new BigMeteor(()->5, 2)
        ));
        meteorSwarmCard = new MeteorSwarmCard(null, Level.FIRST, flightBoard,  projectiles);
    }

    @Test
    void testFirstStep() {
        GameState activateState = meteorSwarmCard.nextStep();
        assertInstanceOf(ActivateState.class, activateState);
    }
}