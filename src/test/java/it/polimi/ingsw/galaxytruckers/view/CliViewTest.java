//package it.polimi.ingsw.galaxytruckers.view;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
//import it.polimi.ingsw.galaxytruckers.view.screens.*;
//import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//import java.io.IOException;
//import java.util.*;
//import java.util.List;
//
//class CliViewTest {
//    View view;
//    ClientModel model;
//    List<Point> getShipArea;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        model = new ClientModel();
//
//        getShipArea = new ArrayList<>(List.of(
//            new Point(4, 7), new Point(4, 8), new Point(4, 9),
//            new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
//            new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
//                                   new Point(7, 6), new Point(7, 7), new Point(7, 8),
//            new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
//            new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
//            new Point(10, 7),new Point(10, 8),new Point(10, 9)
//        ));
//
//        model.setMyNickname("Roborbio");
//        model.setShipArea(new HashSet<>(getShipArea));
//        model.setFlightBoard(10, List.of(2, 4, 5));
//        for (int i=0 ; i<getShipArea.size() ; i++) {
//            model.setComponent("Roborbio", i*5, i%4, getShipArea.get(i)); //changing component coeff (<6) gives interesting cases for tests
//        }
//
//        model.setGoods("Roborbio", new Point(9, 7), List.of(GoodsType.RED, GoodsType.BLUE));
//        model.setSelectablePoints(new ArrayList<>(List.of(new Point(8, 8), new Point(9, 7))));
//        model.setPlayerColor("Roborbio", GameColor.RED);
//        model.setColorToPlace(Map.of("Roborbio", 2));
//        model.setCurrentCard(3);
//        model.setProjectile(ProjectileType.BIGFIRE, 1, 7);
//        for (int i = 0 ; i < 10 ; i++) {
//            //model.addRevealedComponent(i);
//        }
//        model.setStashedComponents(new ArrayList<>(List.of(4, 8)));
//        model.setComponentInHand(3);
//    }
//
//    @Test
//    public void showAllScreens() throws Exception {
//        new NicknameChoiceScreen().showCLI(model);
//        System.out.println("_______________________________________________________________");
////        new NewCardScreen().showCLI(model);
//        System.out.println("_______________________________________________________________");
////        new ProjectilesScreen().showCLI(model);
//        System.out.println("_______________________________________________________________");
//        new CliShipBuildingScreen(model).showCLI(model);
//    }
//}