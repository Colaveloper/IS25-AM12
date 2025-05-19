//package it.polimi.ingsw.galaxytruckers.model.adventureCards;
//
//import it.polimi.ingsw.galaxytruckers.CliFlightBoard;
//import it.polimi.ingsw.galaxytruckers.enumTypes.FourColors;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CliComponent;
//import javafx.scene.image.Image;
//import org.junit.jupiter.api.BeforeEach;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//public class AdventureCardTestInitializer {
//    protected CliComponent component;
//    protected CliComponentBank componentBank = new CliComponentBank() {
//        @Override
//        public CliComponent getRanComponent() {
//            return component;
//        }
//    };
//    protected List<ShipBoard> ships;
//    protected Map<ShipBoard, Integer> shipPlaces;
//    protected ShipBoard testShip;
//    protected int testDisplacement;
//    protected CliFlightBoard flightBoard;
//
//    void setUp() {
//        ships = new ArrayList<>();
//        ships.add(new SecondShipBoard(FourColors.BLUE));
//        ships.add(new SecondShipBoard(FourColors.RED));
//        shipPlaces = new HashMap<>();
//        for (int i = 0; i < ships.size(); i++) {
//            shipPlaces.put(ships.get(i), 10-i);
//        }
//        flightBoard = new CliFlightBoard(null) {
//            @Override
//            public Map<ShipBoard, Integer> getShipToPlace() {
//                return shipPlaces;
//            }
//
//            @Override
//            public List<ShipBoard> getOrderedShips() {
//                return ships;
//            }
//
//            @Override
//            public void displaceShip(ShipBoard shipBoard, int displacement) {
//                testShip = shipBoard;
//                testDisplacement = displacement;
//            }
//
//            @Override
//            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
//                return true;
//            }
//
//            @Override
//            public Image getImage() {
//                return null;
//            }
//        };
//    }
//
//    private void addComponent(ShipBoard shipBoard, Point point) {
//        shipBoard.requestRandComponent();
//        shipBoard.placeComponent(point);
//        shipBoard.weldLastComponent();
//    }
//
//    protected void buildLargeShip(ShipBoard shipBoard) {
//        component = new Cannon(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
//        addComponent(shipBoard, new Point(8,6));
//        addComponent(shipBoard, new Point(10,7));
//        addComponent(shipBoard, new Point(6,5));
//
//        component = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
//        addComponent(shipBoard, new Point(7,7));
//        addComponent(shipBoard, new Point(8,7));
//        addComponent(shipBoard, new Point(9,8));
//
//        component = new Cabin(null, Arrays.asList(Connector.UNIVERSAL, Connector.NONE, Connector.UNIVERSAL, Connector.UNIVERSAL));
//        addComponent(shipBoard, new Point(6,7));
//
//        component = new Shield(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
//        addComponent(shipBoard, new Point(6,6));
//        addComponent(shipBoard, new Point(10,9));
//        addComponent(shipBoard, new Point(4,8));
//
//        component = new Engine(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL));
//        addComponent(shipBoard, new Point(7,8));
//        addComponent(shipBoard, new Point(9,9));
//        addComponent(shipBoard, new Point(4,7));
//
//        component = new CargoHold(null, Arrays.asList(Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL, Connector.UNIVERSAL), 2, true);
//        addComponent(shipBoard, new Point(9,7));
//        addComponent(shipBoard, new Point(7,6));
//        addComponent(shipBoard, new Point(5,7));
//    }
//}
