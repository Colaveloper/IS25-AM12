package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.BigMeteor;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CombatZoneTest {
    CombatZoneCard combatZoneCard;
    Map<ShipBoard, Integer> shipPlaces;
    FlightBoard flightBoard;
    ShipBoard ship1;
    ShipBoard ship2;
    ShipBoard testShip;
    int testDisplacement;
    int flightDaysLoss;
    int crewLoss;
    List<Projectile> projectiles;
    int crewSize1;
    int crewSize2;
    int position1;
    int position2;

    @BeforeEach
    void setUp() {

        shipPlaces = new HashMap<>();
        crewSize1 = 1;
        crewSize2 = 2;
        position1 = 9;
        position2 = 10;

        ship1 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getCrewSize() {
                return crewSize1;
            }
        };
        shipPlaces.put(ship1, position1);
        ship2 = new SecondShipBoard(Colors.BLUE) {
            @Override
            public int getCrewSize() {
                return crewSize2;
            }
        };
        shipPlaces.put(ship1, position2);

        flightBoard = new FlightBoard(null) {
            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return new ArrayList<>(List.of(ship1, ship2));
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
        };

        flightDaysLoss = 2;
        crewLoss = 2;
        projectiles = new ArrayList<>(List.of(
                new SmallMeteor(()->6,0),
                new BigMeteor(()->5, 2)
                ));
        combatZoneCard = new CombatZoneCard(null, Level.SECOND, flightBoard, 2, 2, projectiles);
    }

    @Test
    void shipWithSmallestCrewLosesFlightDaysThenActivateState() {
        GameState testState = combatZoneCard.nextStep();

        assertEquals(testShip, ship1);
        assertEquals(testDisplacement, -flightDaysLoss);
        assertInstanceOf(ActivateState.class, testState);
        assertEquals(ship1, combatZoneCard.getCurrentShipBoard());

        // rest unchanged
        assertEquals(projectiles, combatZoneCard.getProjectiles());
        assertEquals(crewSize1, ship1.getCrewSize());
        assertEquals(crewSize2, ship2.getCrewSize());
        assertEquals(shipPlaces, flightBoard.getShipToPlace());
    }
}
