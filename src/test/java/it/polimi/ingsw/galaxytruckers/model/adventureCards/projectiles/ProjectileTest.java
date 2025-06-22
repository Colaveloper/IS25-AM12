package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntSupplier;

import static org.junit.jupiter.api.Assertions.*;

class ProjectileTest {
    ShipBoard myShipBoard;
    Component up, right, down, left;
    Point upp, rightp, downp, leftp;
    Projectile projectile;

    @BeforeEach
    void setUp() {
        projectile = new Projectile(() -> 0, Direction.UP) {
            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            @Override
            protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                return Optional.empty();
            }

            @Override
            public ProjectileType getProjectileType() {
                return null;
            }
        };
        up = new Component(new EnumMap<>(Direction.class));
        right = new Component(new EnumMap<>(Direction.class));
        down = new Component(new EnumMap<>(Direction.class));
        left = new Component(new EnumMap<>(Direction.class));
        upp = new Point(0, -1);
        rightp = new Point(1, 0);
        downp = new Point(0, 1);
        leftp = new Point(-1, 0);

        myShipBoard = new ShipBoard(GameColor.BLUE) {

            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Component> getComponentMap() {
                return Map.of(
                        upp, up,
                        rightp, right,
                        downp, down,
                        leftp, left
                );
            }
        };
    }

    @Test
    void getDirection() {
        assertEquals(Direction.UP, projectile.getDirection());
    }

    @Test
    void getDiceRoll() {
        assertEquals(0,  projectile.getDiceRoll());
    }

    @Test
    void getFirstFoundComponentPosition() {
        // makes getFirstFoundComponent public for testing
        class TransparentProjectile extends Projectile {
            public TransparentProjectile(IntSupplier dice, Direction direction) {
                super(dice, direction);
            }

            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return super.getFirstFoundComponentPosition(shipBoard);
            }

            @Override
            protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                return Optional.empty();
            }

            @Override
            public ProjectileType getProjectileType() {
                return null;
            }

            @Override
            public int getDiceRoll() {
                return super.getDiceRoll();
            }

            @Override
            public Direction getDirection() {
                return super.getDirection();
            }
        }
        assertEquals(Optional.of(upp), new TransparentProjectile(()->0, Direction.UP).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(rightp), new TransparentProjectile(()->0, Direction.RIGHT).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(downp), new TransparentProjectile(()->0, Direction.DOWN).getFirstFoundComponentPosition(myShipBoard));
        assertEquals(Optional.of(leftp), new TransparentProjectile(()->0, Direction.LEFT).getFirstFoundComponentPosition(myShipBoard));
    }

    @Test
    void fireAtRemovesComponentIfPresent() {
        Point targetComponentPosition = new Point();
        final boolean[] getsRemoved = {false};

        class TargetedProjectile extends Projectile{
            public TargetedProjectile(Direction direction) {
                super(direction);
            }

            @Override
            public Set<Point> getActivatablePoints(ShipBoard shipBoard) {
                return Set.of();
            }

            @Override
            protected Optional<Point> getComponentPositionToRemove(ShipBoard shipBoard) {
                return Optional.of(targetComponentPosition);
            }

            @Override
            public Direction getDirection() {
                return super.getDirection();
            }

            @Override
            public ProjectileType getProjectileType() {
                return null;
            }

            @Override
            public int getDiceRoll() {
                return super.getDiceRoll();
            }
        }

        ShipBoard myShipBoard = new ShipBoard(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public void discardComponent(Point point) {
                getsRemoved[0] = true;
            }
        };

        TargetedProjectile targetedProjectile = new TargetedProjectile(Direction.UP);

        assertTrue(targetedProjectile.fireAt(myShipBoard));
        assertTrue(getsRemoved[0]);
    }
}