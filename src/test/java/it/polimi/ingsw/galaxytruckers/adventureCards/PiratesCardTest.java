package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
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
class PiratesCardTest {
    PiratesCard piratesCard;
    List<ShipBoard> ships;
    int firePowerThreshold;
    int creditPrize;
    int flightDaysLost;
    List<Projectile> projectiles;
    List<Set<Point>> shipPieces;
    boolean removePiece;
    int lastShipPower;

    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        shipPieces = new ArrayList<>();
        shipPieces.add(new HashSet<>());
        removePiece = false;
        lastShipPower = firePowerThreshold+1;
        ships.add(new SecondShipBoard(Colors.BLUE) {
            @Override
            public int getFirePower() {
                return firePowerThreshold-1;
            }

            @Override
            public List<Set<Point>> getConnectedSets() {
                return shipPieces;
            }
        });
        ships.add(new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return firePowerThreshold;
            }
        });
        ships.add(new SecondShipBoard(Colors.RED) {
            @Override
            public int getFirePower() {
                return lastShipPower;
            }
        });
        FlightBoard flightBoardStub = new FlightBoard(null) {
            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {

            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                Map<ShipBoard, Integer> map = new HashMap<>();
                for (ShipBoard s : ships) {
                    map.put(s, 0);
                }
                return map;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }
        };
        projectiles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            projectiles.add(new Projectile(0) {

                @Override
                public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                    return Set.of();
                }

                @Override
                protected Optional<Component> getComponentToRemove(ShipBoard shipBoard) {
                    return Optional.empty();
                }

                @Override
                public boolean fireAt(ShipBoard shipBoard) {
                    return removePiece;
                }
            });
        }
        piratesCard = new PiratesCard(null, Level.SECOND,
                flightBoardStub,firePowerThreshold,creditPrize,flightDaysLost,new ArrayList<>(projectiles));
    }

    @Test
    void constructorCorrectlyAssignsParameters() {
        assertEquals(firePowerThreshold, piratesCard.getFirePowerThreshold());
        assertEquals(creditPrize, piratesCard.getCreditPrize());
        assertEquals(flightDaysLost, piratesCard.getFlightDaysLoss());
    }

    @Test
    void nextStepReturnsActivate() {
        GameState testState = piratesCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
        assertFalse(piratesCard.isDefeated());
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void weakPlayerIsDefeated() {
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertFalse(piratesCard.isDefeated());
        assertInstanceOf(ActivateState.class, testState);
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void playerWithEqualPowerChangesNothing() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertFalse(piratesCard.isDefeated());
        assertInstanceOf(ActivateState.class, testState);
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void playerWithMorePowerDefeatsEnemies() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertInstanceOf(ChoiceState.class, testState);
        assertEquals(ships.getLast(), piratesCard.getWinnerShipBoard());
    }

    @Test
    void nextStepSetsDefeatedTrueWhenPlayersAreOver() {
        lastShipPower = firePowerThreshold;
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertInstanceOf(ActivateState.class, testState);
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void nextStepReturnsActivateWhileThereAreProjectilesAndPlayers() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(projectiles.getFirst(), piratesCard.getCurrentProjectile());
        assertInstanceOf(ActivateState.class, testState);
    }


    @Test
    void nextStepReturnsActivateWhenPieceIsNotRemoved() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(projectiles.getLast(), piratesCard.getCurrentProjectile());
        assertTrue(piratesCard.getProjectiles().isEmpty());
    }

    @Test
    void nextStepReturnsActivateWhenShipRemainsConnected() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        removePiece = true;
        GameState testState = piratesCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(projectiles.getLast(), piratesCard.getCurrentProjectile());
        assertTrue(piratesCard.getProjectiles().isEmpty());
    }

    @Test
    void nextStepReturnsChooseShipPieceWhenShipIsNotConnected() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        shipPieces.add(new HashSet<>());
        removePiece = true;
        GameState testState = piratesCard.nextStep();
        assertInstanceOf(ChooseShipPieceState.class, testState);
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(projectiles.getFirst(), piratesCard.getCurrentProjectile());
    }

    @Test
    void nextStepReturnsDrawWhenCardIsOver() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void getRewardWithoutWinnerThrowsException() {
        assertThrows(IllegalStateException.class, () -> piratesCard.getReward());
    }

    @Test
    void getRewardWithWinnerDoesNotThrowException() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.getReward();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(ships.getLast(), piratesCard.getWinnerShipBoard());
    }
}