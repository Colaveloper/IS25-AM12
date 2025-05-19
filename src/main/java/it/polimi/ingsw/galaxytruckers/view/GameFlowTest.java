//package it.polimi.ingsw.galaxytruckers.view;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
//import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
//import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
//
//import java.awt.*;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//public class GameFlowTest {
//
//    public static void main(String[] args) throws IOException {
//        ServerController serverController = new ServerController();
//        ClientController controller = new ClientController(null);
//        controller.setCLIViewManually();
//
//        List<String> names = new ArrayList<>();
//        names.add("Giuigi");
//        names.add("Gioforchio");
//        controller.setMyNickname("Roborbio");
//        names.add("Roborbio");
//        controller.updateLobbyPlayers(names);
//
//        List<Point> getShipArea = new ArrayList<>(List.of(
//                new Point(4, 7), new Point(4, 8), new Point(4, 9),
//                new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
//                new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
//                new Point(7, 6), new Point(7, 7), new Point(7, 8),
//                new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
//                new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
//                new Point(10, 7),new Point(10, 8),new Point(10, 9)
//        ));
//
//        controller.setupGame(10, List.of(2, 4, 5), new HashSet<>(getShipArea));
//
//        controller.showStartBuilding(50);
//        //(input)
//
//        for (int i=0 ; i<getShipArea.size() -25; i++) {
//            controller.setCoveredComponents(50-i-1);
//            controller.showComponentPositioning("Roborbio", i*5, i%4, getShipArea.get(i));
//            //(input)
//
//            controller.showStashUpdate(new ArrayList<>(List.of(4, 8)));
//            controller.addReavealedComponent(i);
//        }
//
//
//        controller.showColorSelection("Roborbio", FourColors.RED);
//        controller.showPlayerToPlaceUpdate(Map.of("Roborbio", 2));
//
//            //TEST
//
//            //abandoned station
//        controller.showNewCard(5);
//
//            //meteors
//        controller.showNewCard(8);
//        //(input)
//
//        controller.showSelectablePoints(List.of(new Point(4, 7)));
//        controller.showProjectile(ProjectileType.BIGFIRE, 0, 6);
//        //(input)
//
//        controller.showSelectablePoints(List.of(new Point(4, 8)));
//        controller.showProjectile(ProjectileType.SMALLFIRE, 1, 7);
//        //(input)
//
//        controller.showSelectablePoints(List.of(new Point(4, 7)));
//        controller.showProjectile(ProjectileType.SMALLMETEOR, 2, 8);
//        //(input)
//
//
//
//            // abandoned ship
//        controller.showNewCard(1);
//        //(input)
//        controller.updateCredits("Roborbio", 4);
//        controller.updateCrewNumber("Roborbio", new Point(6, 6), 1);
//
//            // planets
//        controller.showNewCard(16);
//        //(input)
//
//        controller.choosePlanet(1);
//        controller.showPlaceGoods();
//        //(input)
//
//        controller.updateGoodsBuffer(1);
//        controller.updateGoods("Roborbio", new Point(9, 7), List.of(GoodsType.RED));
//        controller.showPlaceGoods();
//        //(input)
//
//        controller.updateGoodsBuffer(2);
//        controller.updateGoods("Roborbio", new Point(9, 7), List.of(GoodsType.RED, GoodsType.BLUE));
//        controller.showPlaceGoods();
//        //(input)
//
//        controller.updateGoodsBuffer(3);
//        controller.updateGoods("Roborbio", new Point(9, 7), List.of(GoodsType.RED, GoodsType.BLUE, GoodsType.YELLOW));
//
//    }
//
//    ClientModel model;
//    List<Point> getShipArea;
//
//}
