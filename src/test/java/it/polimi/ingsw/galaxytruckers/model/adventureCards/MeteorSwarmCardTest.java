package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.BigMeteor;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeteorSwarmCardTest {
    Game game;
    MeteorSwarmCard meteorSwarmCard;
    FlightBoard flightBoard;
    List<Projectile> safeProjectiles;
    Projectile damagingProjectile;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    List<ShipBoard> ships;
    GameState testState;

    @BeforeEach
    void setUp() {
        ship1 = new SecondShipBoard(GameColor.RED) {
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of(), Set.of()); // breaking
            }
        };
        ship2 = new SecondShipBoard(GameColor.BLUE) {
            @Override
            public List<Set<Point>> getConnectedSets() {
                return List.of(Set.of()); // not breaking
            }
        };
        ships = new ArrayList<>(List.of(ship1, ship2));
        flightBoard = new FlightBoard() {

            @Override
            public List<ShipBoard> getOrderedShips() {
                return new ArrayList<>(ships);
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }
        };
        safeProjectiles = new ArrayList<>(List.of(
                new SmallMeteor(()->6, Direction.UP),
                new SmallMeteor(()->9,Direction.UP),
                new BigMeteor(()->8, Direction.RIGHT), // 1
                new BigMeteor(()->5, Direction.DOWN)
        ));
        damagingProjectile = new SmallMeteor(()->1,Direction.UP) {
            @Override
            public boolean fireAt(ShipBoard shipBoard) {
                return true;
            }
        };

        game = new Game(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        meteorSwarmCard = new MeteorSwarmCard(game,  Level.SECOND, safeProjectiles, 1);
        meteorSwarmCard.initialize();
    }

    @Test
    void getNextStateReturnsActivateThenDrawCardIfNoBreaking() {
        for (Projectile projectile : safeProjectiles) {
            for (ShipBoard shipBoard : ships) {
                testState = meteorSwarmCard.getNextState();
                assertEquals(meteorSwarmCard.getCurrentShipBoard(), shipBoard);
                assertInstanceOf(HandleProjectileState.class, testState);
            }
        }
        testState = meteorSwarmCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
    }
}