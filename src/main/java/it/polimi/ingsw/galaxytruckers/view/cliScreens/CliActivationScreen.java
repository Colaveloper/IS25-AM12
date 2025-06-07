package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareEnginePowerState;

import java.awt.*;
import java.util.Set;

public abstract class CliActivationScreen extends CliScreen {

    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    public CliActivationScreen(ClientModel model, ControllerToServer controller, ActivateState activateState) {
        super(model, controller, activateState);
        this.currentShip = activateState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public void render() {

    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        if(!isMyTurn) {
            System.out.println("It's not your turn");
            return;
        }
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "A"-> {
                Point p = getPoint(input);
                if (!currentShip.getBatteries().containsKey(p) && !currentShip.getActivatables().containsKey(p)) {
                    System.out.println("Invalid position. Please select a component or battery.");
                    return;
                }
                if (currentShip.getBatteries().containsKey(p) && currentShip.getBatteries().get(p).getNumBatteries() > 0) {
                    System.out.println("That battery is empty");
                }
                if (currentShip.getBatteries().containsKey(p) && currentShip.getBatteries().get(p).getNumBatteries() > 0) {
                    controller.useBattery(p);
                } else if (currentShip.getActivatables().containsKey(p)) {
                    controller.activateComponent(p);
                }
            }
            case "" -> controller.goNext();
        }
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.BLUE);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.GREEN);
        //shipToCliShip.get(shipBoard).setDirty();
        cliAllShips.setDirty();
    }
}
