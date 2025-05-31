package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;

import java.awt.*;
import java.util.Set;

public class CliDeclareFirePowerScreen extends CliScreen {

    private final DeclareFirePowerState gameState;
    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    public CliDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to declare fire power");
            return false;
        }

        if (!isFormatLegal(input)) {
            return false;
        }

        Point p = getPoint(input);
        Set<Point> availablePositions = gameState.getAvailablePositions();

        return availablePositions.contains(p) || currentShip.getBatteries().containsKey(p);
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to declare fire power");
            System.out.println("Select cannon components to activate or batteries to use");
            System.out.println("Available cannons: " + gameState.getAvailablePositions().size());
            System.out.println("Available batteries: " + currentShip.getBatteries().size());
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to declare fire power");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to declare fire power");
            return;
        }

        if (input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }

        Point p = getPoint(input);

        if (currentShip.getBatteries().containsKey(p)) {
            controller.useBattery(p);
        } else if (gameState.getAvailablePositions().contains(p)) {
            controller.activateComponent(p);
        } else {
            System.out.println("Invalid position. Please select a cannon or battery.");
        }
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
    }
}
