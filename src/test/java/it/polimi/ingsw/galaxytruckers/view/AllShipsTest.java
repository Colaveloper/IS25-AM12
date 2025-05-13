//package it.polimi.ingsw.galaxytruckers.view;
//
//import it.polimi.ingsw.galaxytruckers.view.cli.CliAllShips;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.Set;
//
//class AllShipsTest {
//
//    static CliAllShips allShips;
//    static LinkedHashMap<String, CliShipBoard> shipboardList;
//    static CliShipBoard shipboard;
//
//    //@BeforeEach
//    static void setUp() {
//        shipboardList = new LinkedHashMap<>();
//        shipboard = new CliShipBoard();
//        shipboard.setShipArea(Set.of(new Point(12,7),new Point(11,7),new Point(10,7),new Point(9,7), new Point(8,7), new Point(7,8), new Point(6,7), new Point(7,6)));
//
//        shipboardList.put("player1", shipboard);
//        shipboardList.put("player2", shipboard);
//        shipboardList.put("player3", shipboard);
//        shipboardList.put("player4", shipboard);
//        allShips = new CliAllShips(shipboardList);
//    }
//
//    @Test
//    void getNewDescription() {
//        allShips.getNewDescription();
//    }
//
//    public static void main(String[] args) throws IOException {
//        setUp();
//        allShips.getNewDescription().forEach(System.out::println);
//
//    }
//}