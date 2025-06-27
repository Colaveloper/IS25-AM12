package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.view.cli.CliHighlights;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ActivateState;

import java.awt.*;
import java.util.Set;

/**
 * Represents the CLI screen for the ship component activation phase.
 */
public abstract class CliActivationScreen extends CliAdventureScreen {

    private int batteriesToSpend;
//    protected int activateablesComp;
//    protected int numBatteriesComp;

    /**
     * Creates a new activation screen with the given game state.
     *
     * @param model      The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param activateState The current activation phase state
     */
    public CliActivationScreen(ClientModel model, ClientControllerInterface controller, ActivateState activateState) {
        super(model, controller, activateState);
        batteriesToSpend = 0;
//        activateablesComp = activateState.getAvailablePositions().size();
//        numBatteriesComp  = currentShip.getBatteries().size();
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
                if (!currentShip.getActivatables().containsKey(p)) {
                    System.out.println("Invalid position. Please select a component.");
                    return;
                }
                controller.activateComponent(p);
            }
            case "B" -> {
                Point p = getPoint(input);
                if (!currentShip.getBatteries().containsKey(p)) {
                    System.out.println("Invalid position. Please select a battery.");
                    return;
                }
                if (currentShip.getBatteries().containsKey(p) && currentShip.getBatteries().get(p).getNumBatteries() <= 0) {
                    System.out.println("That battery is empty");
                    return;
                }
                controller.useBattery(p);
            }
            case "" -> {
                if(batteriesToSpend > 0) {
                    System.out.println("You need to activate " + batteriesToSpend + " batteries");
                    return;
                }
                if(batteriesToSpend < 0) {
                    System.out.println("You need to activate " + (batteriesToSpend * (-1)) + " components");
                    return;
                }
                controller.goNext();
            }
        }
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        batteriesToSpend ++;
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), CliHighlights.BLUE);
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend --;
        shipToCliShip.get(shipBoard).highlightPoints(Set.of(point), CliHighlights.GREEN);
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }
}
