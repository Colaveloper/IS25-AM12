package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;

import java.awt.*;
import java.util.Set;

public abstract class CliActivationScreen extends CliAdventureScreen {

    private int batteriesToSpend;
//    protected int activateablesComp;
//    protected int numBatteriesComp;

    public CliActivationScreen(ClientModel model, ControllerToServer controller, ActivateState activateState) {
        super(model, controller, activateState);
        batteriesToSpend = 0;
//        activateablesComp = activateState.getAvailablePositions().size();
//        numBatteriesComp  = currentShip.getBatteries().size();
    }

    @Override
    public abstract void render();

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
                if (currentShip.getBatteries().containsKey(p) && currentShip.getBatteries().get(p).getNumBatteries() <= 0) {
                    System.out.println("That battery is empty");
                    return;
                }
                if (currentShip.getBatteries().containsKey(p) && currentShip.getBatteries().get(p).getNumBatteries() > 0) {
                    controller.useBattery(p);
                } else if (currentShip.getActivatables().containsKey(p)) {
                    controller.activateComponent(p);
                }
            }
            case "" -> {
                if(batteriesToSpend > 0) {
                    System.out.println("Your need to activate " + batteriesToSpend + " batteries");
                    return;
                }
                if(batteriesToSpend < 0) {
                    System.out.println("Your need to activate " + (batteriesToSpend * (-1)) + " components");
                    return;
                }
                controller.goNext();
            }
        }
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        batteriesToSpend ++;
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.BLUE);
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend --;
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), Highlights.GREEN);
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }
}
