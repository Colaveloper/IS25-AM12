package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.client.controller.ClientController;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.*;

import java.awt.*;
import java.util.Map;

public class CliScreenSetup {
    protected static ClientController controller;
    protected static ClientModel model;
    protected static ShipBoard shipBoard;

    public static void shipSetUp() {
        controller = new ClientController();
        model = new ClientModel();

        model.createGame(Level.SECOND);
        controller.setModel(model);
        controller.setMyNickname("Player 1");
        shipBoard = new SecondShipBoard(GameColor.BLUE);
        shipBoard.offerComponent(new Shield(Map.of(
                Direction.UP, Connector.SINGLE,
                Direction.RIGHT, Connector.DOUBLE,
                Direction.DOWN, Connector.SINGLE,
                Direction.LEFT, Connector.DOUBLE), 154));
        shipBoard.placeComponent(new Point(8, 7), Direction.UP);
        shipBoard.weldLastComponent();

        shipBoard.offerComponent(new Cannon(Map.of(
                Direction.UP, Connector.SINGLE,
                Direction.RIGHT, Connector.DOUBLE,
                Direction.DOWN, Connector.SINGLE,
                Direction.LEFT, Connector.DOUBLE), 154));
        shipBoard.placeComponent(new Point(9, 7), Direction.UP);
        shipBoard.weldLastComponent();
    }
}
