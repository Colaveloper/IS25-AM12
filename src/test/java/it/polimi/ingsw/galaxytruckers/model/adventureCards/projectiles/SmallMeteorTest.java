package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class SmallMeteorTest {
    ShipBoard myShipBoard;
    Point point1;
    Point point2;
    Map<Point, Shield> myShields;
    Set<Point> testActivablePositions;
    Map<Direction, Connector> noneConnectors;
    Map<Direction, Connector> universalConnectors;
    Point sturdyPosition;
    Component sturdyComponent;
    Point weakPosition;
    Component weakComponent;
    SmallMeteor smallMeteor;

    @BeforeEach
    void setUp() {
        smallMeteor = new SmallMeteor(Direction.UP);
        point1 = new Point(0, 0);
        point2 = new Point(1, 0);
        sturdyPosition = new Point(10, 11);
        weakPosition = new Point(12, 13);

        noneConnectors = Map.of(
                Direction.UP, Connector.NONE,
                Direction.LEFT, Connector.NONE,
                Direction.RIGHT, Connector.NONE,
                Direction.DOWN, Connector.NONE
        );
        universalConnectors = Map.of(
                Direction.UP, Connector.UNIVERSAL,
                Direction.LEFT, Connector.UNIVERSAL,
                Direction.RIGHT, Connector.UNIVERSAL,
                Direction.DOWN, Connector.UNIVERSAL
        );
        sturdyComponent = new Component(new EnumMap<>(Direction.class)) {
            @Override
            public Map<Direction, Connector> getConnectors() {
                return new HashMap<>(noneConnectors);
            }
        };
        weakComponent = new Component(new EnumMap<>(Direction.class)) {
            @Override
            public Map<Direction, Connector> getConnectors() {
                return new HashMap<>(universalConnectors);
            }
        };

        myShields = new HashMap<>();

        myShipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Map<Point, Shield> getShields() {
                return myShields;
            }

            @Override
            public Map<Point, Component> getComponentMap() {
                return new HashMap<>(Map.of(weakPosition, weakComponent, sturdyPosition, sturdyComponent));
            }
        };
    }

    @Test
    void getProjectileType() {
        assertEquals(ProjectileType.SMALLMETEOR, smallMeteor.getProjectileType());
    }

    @Test
    void getActivatablePointsReturnsUsefulShieldsPositions() {
        myShields = new HashMap<>(Map.of(
                point1, new Shield(new HashMap<>(noneConnectors)) {
                    @Override
                    public Set<Direction> getDefensibleDirections() {
                        return Set.of(Direction.UP, Direction.RIGHT);
                    }
                },
                point2, new Shield(new HashMap<>(noneConnectors)) {
                    @Override
                    public Set<Direction> getDefensibleDirections() {
                        return Set.of(Direction.DOWN, Direction.LEFT);
                    }
                }
        ));

        testActivablePositions = new SmallMeteor(()->0,Direction.UP).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,Direction.RIGHT).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);

        testActivablePositions = new SmallMeteor(()->0,Direction.DOWN).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,Direction.LEFT).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
    }

    @Test
    void getActivatablePointsReturnsEmptySetIf() {
        myShields = new HashMap<>(Map.of(
                point1, new Shield(new HashMap<>(noneConnectors)) {
                    @Override
                    public Set<Direction> getDefensibleDirections() {
                        return Set.of(Direction.UP, Direction.RIGHT);
                    }
                },
                point2, new Shield(new HashMap<>(noneConnectors)) {
                    @Override
                    public Set<Direction> getDefensibleDirections() {
                        return Set.of(Direction.DOWN, Direction.LEFT);
                    }
                }
        ));

        testActivablePositions = new SmallMeteor(()->0,Direction.UP).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,Direction.RIGHT).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);

        testActivablePositions = new SmallMeteor(()->0,Direction.DOWN).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,Direction.LEFT).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyOptionalIfProtected() {
        myShipBoard = new SecondShipBoardForTesting(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public Set<Direction> getShieldDirections() {
                return Set.of(Direction.values());
            };
        };

        for (Direction direction : Direction.values()) {
            assertEquals(Optional.empty(), new SmallMeteor(direction).getComponentPositionToRemove(myShipBoard));
        }
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyOptionalIfNoShieldsButNoExposedConnector() {
        Projectile testProjectile = new SmallMeteor(()->0,Direction.UP) {
            @Override
            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(sturdyPosition);
            }
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.empty(), testProjectile.getComponentPositionToRemove(myShipBoard));
        }
    }

    @Test
    void getComponentToRemoveReturnsComponentPositionIfNoShieldsAndExposedConnector() {
        Projectile testProjectile = new SmallMeteor(()->0,Direction.UP) {
            @Override
            public Optional<Point> getFirstFoundComponentPosition(ShipBoard shipBoard) {
                return Optional.of(weakPosition);
            }
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.of(weakPosition), testProjectile.getComponentPositionToRemove(myShipBoard));
        }
    }
}