package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;

public class CliPlanetScreen extends CliScreen {

    private final boolean isMyTurn;
    private final ShipBoard currentShip;
    private final int numPlanets;

    public CliPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
        this.numPlanets = gameState.getOptions().size();
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to choose a planet");
            return false;
        }

        if (!isFormatLegal(input)) {
            return false;
        }

        try {
            int choice = Integer.parseInt(input);
            return choice >= 0 && choice < numPlanets;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 0 and " + (numPlanets - 1));
            return false;
        }
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to choose a planet to land on");
            System.out.println("Available planets: " + numPlanets);
            System.out.println("Enter a number between 0 and " + (numPlanets - 1) + " to choose a planet");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to choose a planet");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to choose a planet");
            return;
        }

        if (input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice < 0 || choice >= numPlanets) {
                System.out.println("Invalid planet choice. Please enter a number between 0 and " + (numPlanets - 1));
                return;
            }
            controller.choosePlanet(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
        cliFlightBoard.setDirty();
    }
}
