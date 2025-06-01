package it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SmallMeteorTest {
    ShipBoard myShipBoard;
    Point point1;
    Point point2;
    Shield shield1;
    Shield shield2;
    Map<Point, Shield> myShields;
    Set<Point> testActivablePositions;
    List<Connector> noneConnectors;
    List<Connector> universalConnectors;
    Point sturdyPosition;
    Component sturdyComponent;
    Point weakPosition;
    Component weakComponent;

    @BeforeEach
    void setUp() {
        point1 = new Point(0, 0);
        point2 = new Point(1, 0);
        sturdyPosition = new Point(10, 11);
        weakPosition = new Point(12, 13);

        noneConnectors = new ArrayList<>(List.of(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE));
        universalConnectors = new ArrayList<>(List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));

        sturdyComponent = new Component(null) {
            @Override
            public List<Connector> getConnectors() {
                return new ArrayList<>(noneConnectors);
            }
        };
        weakComponent = new Component(null) {
            @Override
            public List<Connector> getConnectors() {
                return new ArrayList<>(universalConnectors);
            }
        };

        myShields = new HashMap<>();

        myShipBoard = new ShipBoard(GameColor.BLUE) {
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
    void getActivatablePointsReturnsUsefulShieldsPositions() {
        myShields = new HashMap<>(Map.of(
                point1, new Shield(new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{0, 1};
                    }
                },
                point2, new Shield(new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{2, 3};
                    }
                }
        ));

        testActivablePositions = new SmallMeteor(()->0,0).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,1).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);

        testActivablePositions = new SmallMeteor(()->0,2).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,3).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
    }

    @Test
    void getActivatablePointsReturnsEmptySetIf() {
        myShields = new HashMap<>(Map.of(
                point1, new Shield(new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{0, 1};
                    }
                },
                point2, new Shield(new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{2, 3};
                    }
                }
        ));

        testActivablePositions = new SmallMeteor(()->0,0).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,1).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point1), testActivablePositions);

        testActivablePositions = new SmallMeteor(()->0,2).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
        testActivablePositions = new SmallMeteor(()->0,3).getActivatablePoints(myShipBoard);
        assertEquals(Set.of(point2), testActivablePositions);
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyOptionalIfProtected() {
        myShipBoard = new ShipBoard(GameColor.BLUE) {
            @Override
            protected boolean containsPoint(Point point) {
                return true;
            }

            @Override
            public boolean[] getShieldDirections() {
                return new boolean[]{true, true, true, true};
            };
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.empty(), new SmallMeteor(i).getComponentPositionToRemove(myShipBoard));
        }
    }

    @Test
    void getComponentPositionToRemoveReturnsEmptyOptionalIfNoShieldsButNoExposedConnector() {
        Projectile testProjectile = new SmallMeteor(()->0,0) {
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
        Projectile testProjectile = new SmallMeteor(()->0,0) {
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