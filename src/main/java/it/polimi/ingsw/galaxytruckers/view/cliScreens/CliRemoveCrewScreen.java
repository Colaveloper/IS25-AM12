package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveCrewState;

import java.awt.*;

public class CliRemoveCrewScreen extends CliAdventureScreen {


    public CliRemoveCrewScreen(ClientModel model, ControllerToServer controller, RemoveCrewState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
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
