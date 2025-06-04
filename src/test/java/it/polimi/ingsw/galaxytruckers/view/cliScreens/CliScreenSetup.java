package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public class CliScreenSetup {
    protected static ClientController controller;
    protected static ClientModel model;
    protected static ShipBoard shipBoard;

    public static void shipSetUp() {
        controller = new ClientController();
        model = new ClientModel();

        model.createGame(Level.SECOND, 1);
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
