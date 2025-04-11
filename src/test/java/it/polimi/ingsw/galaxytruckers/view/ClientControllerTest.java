package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiClient;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;

class ClientControllerTest {
    ClientController controller;
    List<Point> shipArea;

    @BeforeEach
    void setUp() throws IOException {
        RmiServer mockServer = new RmiServer();
        controller = new ClientController(new RmiClient(mockServer), mockServer);

        shipArea = new ArrayList<>(List.of(
            new Point(4, 7), new Point(4, 8), new Point(4, 9),
            new Point(5, 6), new Point(5, 7), new Point(5, 8), new Point(5, 9),
            new Point(6, 5), new Point(6, 6), new Point(6, 7), new Point(6, 8), new Point(6, 9),
            new Point(7, 6), new Point(7, 7), new Point(7, 8),
            new Point(8, 5), new Point(8, 6), new Point(8, 7), new Point(8, 8), new Point(8, 9),
            new Point(9, 6), new Point(9, 7), new Point(9, 8), new Point(9, 9),
            new Point(10, 7), new Point(10, 8), new Point(10, 9)
        ));

        controller.setNickname("roborbio");
        controller.setShipArea(new HashSet<>(shipArea));
        controller.setFlightBoard(10, List.of(2, 4, 5));
        for (int i=0 ; i<shipArea.size() ; i++) {
            controller.showComponentPositioning("roborbio", i*5, i%4, shipArea.get(i)); //changing component coeff (<6) gives interesting cases for tests
        }

        controller.showUpdateCargoHold("roborbio", new Point(9, 7), List.of(GoodsType.RED, GoodsType.BLUE)); //TODO: doesn t work
        controller.showSelectablePoints(new ArrayList<>(List.of(new Point(7, 7))));
        controller.showSelectablePoints(new ArrayList<>(List.of(new Point(6, 7))));
        controller.showColorSelection("roborbio", Colors.RED);
        controller.showPlayerToPlaceUpdate(Map.of("roborbio", 2));
    }

    @Test
    public void tryToPrint() throws Exception {
        controller.showNewCard(1);
        // controller.setFlightBoard is called too resulting in 2 consecutive visualizations,
        // for now it's intentional, and it means it does work
    }
}