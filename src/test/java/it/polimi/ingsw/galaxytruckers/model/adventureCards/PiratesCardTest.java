package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class PiratesCardTest {
    Game game;
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
        ships.add(new SecondShipBoard(FourColors.BLUE) {
            @Override
            public int getFirePower() {
                return firePowerThreshold-1;
            }

            @Override
            public List<Set<Point>> getConnectedSets() {
                return shipPieces;
            }
        });
        ships.add(new SecondShipBoard(FourColors.RED) {
            @Override
            public int getFirePower() {
                return firePowerThreshold;
            }
        });
        ships.add(new SecondShipBoard(FourColors.RED) {
            @Override
            public int getFirePower() {
                return lastShipPower;
            }
        });
        FlightBoard flightBoardStub = new FlightBoard() {
            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return false;
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
                protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                    return Optional.empty();
                }

                @Override
                public boolean fireAt(ShipBoard shipBoard) {
                    return removePiece;
                }
            });
        }
        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoardStub;
            }
        };
        piratesCard = new PiratesCard(game,
                Level.SECOND, firePowerThreshold,creditPrize,flightDaysLost,new ArrayList<>(projectiles), 1);
        piratesCard.initialize();
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
        assertInstanceOf(DeclareFirePowerState.class, testState);
        assertFalse(piratesCard.isDefeated());
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void weakPlayerIsDefeated() {
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertFalse(piratesCard.isDefeated());
        assertInstanceOf(DeclareFirePowerState.class, testState);
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void playerWithEqualPowerChangesNothing() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertFalse(piratesCard.isDefeated());
        assertInstanceOf(DeclareFirePowerState.class, testState);
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
        assertInstanceOf(GrabRewardState.class, testState);
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
        assertInstanceOf(HandleProjectileState.class, testState);
        assertNull(piratesCard.getWinnerShipBoard());
    }

    @Test
    void nextStepReturnsHandleProjectileWhileThereAreProjectilesAndPlayers() {
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        piratesCard.nextStep();
        GameState testState = piratesCard.nextStep();
        assertEquals(ships.subList(0,1), piratesCard.getDefeatedPlayers());
        assertTrue(piratesCard.isDefeated());
        assertEquals(projectiles.getFirst(), piratesCard.getCurrentProjectile());
        assertInstanceOf(HandleProjectileState.class, testState);
        assertEquals(projectiles.subList(1, projectiles.size()).reversed(), piratesCard.getProjectiles());
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
    void getRewardDoesNotAffectCardState() {
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