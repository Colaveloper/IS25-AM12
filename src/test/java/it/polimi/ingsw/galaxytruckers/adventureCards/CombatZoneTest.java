package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.BigMeteor;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CombatZoneTest {
    CombatZoneCard combatZoneCard;
    Map<ShipBoard, Integer> shipPlaces;
    FlightBoard flightBoard;
    ShipBoard ship1;
    ShipBoard ship2;
    ShipBoard testShip;
    List<ShipBoard> ships;
    int testDisplacement;
    int flightDaysLoss;
    int crewLoss;
    List<Projectile> projectiles;
    int crewSize1;
    int crewSize2;
    int firePower1;
    int firePower2;
    int enginePower1;
    int enginePower2;
    int position1;
    int position2;
    Projectile damagingProjectile;

    @BeforeEach
    void setUp() {

        shipPlaces = new HashMap<>();
        crewSize1 = 1;
        crewSize2 = 2;
        firePower1 = 3;
        firePower2 = 5;
        enginePower1 = 4;
        enginePower2 = 7;
        position1 = 9;
        position2 = 10;

        ship1 = new SecondShipBoard(Colors.RED) {
            @Override
            public int getCrewSize() {
                return crewSize1;
            }
            @Override
            public int getFirePower() {
                return firePower1;
            }
            @Override
            public int getEnginePower() {
                return enginePower1;
            }
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of(), Set.of());
            }
        };
        ship2 = new SecondShipBoard(Colors.BLUE) {
            @Override
            public int getCrewSize() {
                return crewSize2;
            }
            @Override
            public int getFirePower() {
                return firePower2;
            }
            @Override
            public int getEnginePower() {
                return enginePower2;
            }
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of(), Set.of());
            }
        };
        ships = new ArrayList<>(List.of(ship1, ship2));
        shipPlaces.put(ship1, position1);
        shipPlaces.put(ship2, position2);

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
                new BigMeteor(()->1, 3),
                new BigMeteor(()->5, 2)
                ));
        damagingProjectile = new SmallMeteor(()->1,0) {
            @Override
            public boolean fireAt(ShipBoard shipBoard) {
                return true;
            }
        };
        combatZoneCard = new CombatZoneCard(null, Level.SECOND, flightBoard, 2, 2, projectiles);
    }

    @Test
    void shipWithSmallestCrewLosesFlightDaysThenActivateState() {
        GameState testState = combatZoneCard.nextStep();

        assertEquals(Math.min(crewSize1, crewSize2), combatZoneCard.getMinCrewSize());
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

    @Test
    void nextStepReturnsActivateStateForEngines() {
        for (ShipBoard ship : ships) {
            GameState testState = combatZoneCard.nextStep();
            assertInstanceOf(ActivateState.class, testState);
            assertEquals(ship, combatZoneCard.getCurrentShipBoard());

            // rest unchanged
            assertEquals(projectiles, combatZoneCard.getProjectiles());
            assertEquals(crewSize1, ship1.getCrewSize());
            assertEquals(crewSize2, ship2.getCrewSize());
            assertEquals(shipPlaces, flightBoard.getShipToPlace());
        }
        GameState testState = combatZoneCard.nextStep();
    }

    @Test
    void nextStepReturnsLoseCrewStateAfterCannonActivation() {
        for (ShipBoard ship : ships) {
            combatZoneCard.nextStep();
        }
        assertInstanceOf(ChooseCrewToLoseState.class, combatZoneCard.nextStep());
    }

    @Test
    void sufferCrewLossDiminishesCrewLossLeftUntilItIsZero() {
        for (int i = crewLoss; i > 0; i-- ) {
            combatZoneCard.sufferCrewLoss();
            crewLoss--;
            assertEquals(crewLoss, combatZoneCard.getCrewLossLeft());
        }
        assertThrows(IllegalStateException.class, combatZoneCard::sufferCrewLoss);
    }

    @Test
    void minEnginePowerGetsCalculated() {
        for (ShipBoard ship : ships) { // none activated cannons
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        assertEquals(Math.min(enginePower1, enginePower2), combatZoneCard.getMinEnginePower());
    }

    @Test
    void nextStepReturnsActivateStateForCannons() {
        for (ShipBoard ship : ships) { // engine activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        for (int i = crewLoss; i > 0; i-- ) {
            combatZoneCard.sufferCrewLoss();
        }
        for (ShipBoard ship : ships) {
            GameState testState = combatZoneCard.nextStep();
            assertInstanceOf(ActivateState.class, testState);
            assertEquals(ship, combatZoneCard.getCurrentShipBoard());
        }
    }

    @Test
    void minFirePowerGetsCalculated() {
        for (ShipBoard ship : ships) { // engine activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        for (int i = crewLoss; i > 0; i-- ) { // losing crew
            combatZoneCard.sufferCrewLoss();
        }
        for (ShipBoard ship : ships) { // cannon activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        assertEquals(Math.min(firePower1, firePower2), combatZoneCard.getMinFirePower());
        assertEquals(Math.min(firePower1, firePower2), combatZoneCard.getCurrentShipBoard().getFirePower());
    }

    @Test
    void nextStepReturnsActivateStateForEachProjectile() {
        for (ShipBoard ship : ships) { // engine activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        for (int i = crewLoss; i > 0; i-- ) { // losing crew
            combatZoneCard.sufferCrewLoss();
        }
        for (ShipBoard ship : ships) { // cannon activation
            combatZoneCard.nextStep();
        }
        for (Projectile projectile : projectiles) {
            GameState testState = combatZoneCard.nextStep();
            assertInstanceOf(ActivateState.class, testState);
            assertEquals(projectile, combatZoneCard.getCurrentProjectile());
        }
    }

    @Test
    void nextStepReturnsDrawCardStateAfterProjectiles() {
        for (ShipBoard ship : ships) { // engine activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        for (int i = crewLoss; i > 0; i-- ) { // losing crew
            combatZoneCard.sufferCrewLoss();
        }
        for (ShipBoard ship : ships) { // cannon activation
            combatZoneCard.nextStep();
        }
        for (Projectile projectile : projectiles) {
            combatZoneCard.nextStep();
        }
        GameState testState = combatZoneCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void nextStepReturnsChooseShipPieceStateIfBroken() {

        projectiles = new ArrayList<>(List.of(damagingProjectile));

        combatZoneCard = new CombatZoneCard(null, Level.SECOND, flightBoard, 2, 2, projectiles);

        for (ShipBoard ship : ships) { // engine activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep();
        for (int i = crewLoss; i > 0; i-- ) { // losing crew
            combatZoneCard.sufferCrewLoss();
        }
        for (ShipBoard ship : ships) { // cannon activation
            combatZoneCard.nextStep();
        }
        combatZoneCard.nextStep(); // shield activation
        GameState testState = combatZoneCard.nextStep();
        assertInstanceOf(ChooseShipPieceState.class, testState);
    }
}
