package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.state.RemoveCrewState;

import java.awt.*;

/**
 * CLI screen for removing crew members from a ship during the adventure phase.
 */
public class CliRemoveCrewScreen extends CliAdventureScreen {


    /**
     * Creates a new remove crew screen with the given game state.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current remove crew state
     */
    public CliRemoveCrewScreen(ClientModel model, ClientControllerInterface controller, RemoveCrewState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (isMyTurn) {
            System.out.println("Your turn to remove crew members");
            System.out.println("Select cabins to remove crew from");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to remove crew members");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "Y"-> controller.giveUp();
            case "L" -> {
                if (!isMyTurn) {
                    System.out.println("It's not your turn to remove crew");
                }
                Point p = getPoint(input);
                if (!currentShip.getCabins().containsKey(p)) {
                    System.out.println("No cabin at this position");
                    return;
                }
                if (currentShip.getCabins().get(p).getNumResidents() <= 0) {
                    System.out.println("No crew members in this cabin");
                    return;
                }
                controller.loseCrew(p);
            }
        }
    }

//    @Override
//    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
//        CliShipBoard ship = shipToCliShip.get(shipBoard);
//        // Update the ship's crew display
//        cliAllShips.setDirty();
//    }
}
