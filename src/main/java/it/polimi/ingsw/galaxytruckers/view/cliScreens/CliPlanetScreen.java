package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;

import java.util.*;

public class CliPlanetScreen extends CliScreen {

    private boolean isMyTurn;
    private ShipBoard currentShip;
    private final ShipBoard[] choiceToShip;

    public CliPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = gameState.isMyTurn();
        this.choiceToShip = gameState.getOptions();
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to choose a planet to land on");
            //System.out.println("Available planets: ");
            for(int i = 0; i < choiceToShip.length; i++) {
               if (choiceToShip[i] != null) System.out.println("planet " + i + " - " + choiceToShip[i].getColor());
               else System.out.println("planet " + i + " - available");
            }
            //System.out.println("Enter a number between 0 and " + (numPlanets - 1) + " to choose a planet");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to choose a planet");
        }
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        if (!isMyTurn) {
            System.out.println("It's not your turn to choose a planet");
            return;
        }
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()){
            case "L" -> {
                int choice = Integer.parseInt(parts[1]);
                if (choice < 0 || choice >= choiceToShip.length || choiceToShip[choice] != null) {
                    System.out.println("Invalid planet choice.");
                    return;
                }
                controller.choosePlanet(choice);
            }
            case "" -> controller.goNext();
        }
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoardLanded, int choice, ShipBoard nextShipBoard) {
        currentShip = nextShipBoard;
        isMyTurn = currentShip.equals(myShipBoard);
        choiceToShip[choice] = shipBoardLanded;
    }

}
