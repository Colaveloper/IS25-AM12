package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Shield;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
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
    Component sturdyComponent;
    Component weakComponent;

    @BeforeEach
    void setUp() {
        point1 = new Point(0, 0);
        point2 = new Point(1, 0);

        noneConnectors = new ArrayList<>(List.of(Connector.NONE, Connector.NONE, Connector.NONE, Connector.NONE));
        universalConnectors = new ArrayList<>(List.of(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));

        sturdyComponent = new Component(null, null) {
            @Override
            public List<Connector> getConnectors() {
                return new ArrayList<>(noneConnectors);
            }
        };
        weakComponent = new Component(null, null) {
            @Override
            public List<Connector> getConnectors() {
                return new ArrayList<>(universalConnectors);
            }
        };

        myShields = new HashMap<>();

        myShipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public Map<Point, Shield> getShields() {
                return myShields;
            }
        };
    }

    @Test
    void getActivatablePointsReturnsUsefulShieldsPositions() {
        myShields = new HashMap<>(Map.of(
                point1, new Shield(null, new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{0, 1};
                    }
                },
                point2, new Shield(null, new ArrayList<>(noneConnectors)) {
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
                point1, new Shield(null, new ArrayList<>(noneConnectors)) {
                    @Override
                    public int[] getDefensibleDirections() {
                        return new int[]{0, 1};
                    }
                },
                point2, new Shield(null, new ArrayList<>(noneConnectors)) {
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
    void getComponentToRemoveReturnsEmptyOptionalIfProtected() {
        myShipBoard = new ShipBoard(null) {
            @Override
            protected boolean containsPoint(Point point) {
                return false;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public boolean[] getShieldDirections() {
                return new boolean[]{true, true, true, true};
            };
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.empty(), new SmallMeteor(i).getComponentToRemove(myShipBoard));
        }
    }

    @Test
    void getComponentToRemoveReturnsEmptyOptionalIfNoShieldsButNoExposedConnector() {
        Projectile testProjectile = new SmallMeteor(()->0,0) {
            @Override
            public Optional<Component> getFirstFoundComponent (ShipBoard shipBoard) {
                return Optional.of(sturdyComponent);
            }
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.empty(), testProjectile.getComponentToRemove(myShipBoard));
        }
    }

    @Test
    void getComponentToRemoveReturnsComponentIfNoShieldsAndExposedConnector() {
        Projectile testProjectile = new SmallMeteor(()->0,0) {
            @Override
            public Optional<Component> getFirstFoundComponent (ShipBoard shipBoard) {
                return Optional.of(weakComponent);
            }
        };

        for (int i = 0; i < 4; i++) {
            assertEquals(Optional.of(weakComponent), testProjectile.getComponentToRemove(myShipBoard));
        }
    }
}