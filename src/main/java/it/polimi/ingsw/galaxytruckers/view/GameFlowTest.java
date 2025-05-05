package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.view.screens.NewCardScreen;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GameFlowTest {

    public static void main(String[] args) throws IOException {
        ServerController serverController = new ServerController();
        VirtualServer server = new RmiServer(serverController);
        ClientController controller = new ClientController(server);
        controller.setCLIViewManually(server);

        List<Point> shipArea = new ArrayList<>(List.of(
                new Point(4, 7), new Point(4, 8), new Point(4, 9),
                new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
                new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
                new Point(7, 6), new Point(7, 7), new Point(7, 8),
                new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
                new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
                new Point(10, 7),new Point(10, 8),new Point(10, 9)
        ));
        controller.setNickname("Roborbio");
        controller.setShipArea(new HashSet<>(shipArea));
        controller.setFlightBoard(10, List.of(2, 4, 5));

        for (int i=0 ; i<shipArea.size() - 20 ; i++) {
            controller.showComponentPositioning("Roborbio", i*5, i%4, shipArea.get(i));
            controller.showStashUpdate(new ArrayList<>(List.of(4, 8)));
            controller.addReavealedComponent(i);
        }

        controller.showUpdateCargoHold("Roborbio", new Point(9, 7), List.of(GoodsType.RED, GoodsType.BLUE));
        controller.showColorSelection("Roborbio", Colors.RED);
        controller.showPlayerToPlaceUpdate(Map.of("Roborbio", 2));

        //TEST

        controller.showNewCard(16);
        controller.choosePlanet(1);
        controller.showPlaceGoods();
        controller.updateGoodsBuffer(1);
        controller.updateGoodsBuffer(2);
        controller.updateGoodsBuffer(3);

    }

    ClientModel model;
    List<Point> shipArea;

}
