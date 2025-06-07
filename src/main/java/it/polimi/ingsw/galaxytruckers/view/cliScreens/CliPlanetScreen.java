package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;
import java.util.ArrayList;
import java.util.List;

public class CliPlanetScreen extends CliScreen {

    private boolean isMyTurn;
    private ShipBoard currentShip;
    private final List<ShipBoard> planets;

    public CliPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
        this.planets = new ArrayList<>();
        for(int i = 0; i < gameState.getOptions().size(); i++) planets.add(null);
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to choose a planet to land on");
            //System.out.println("Available planets: ");
            for(int i = 0; i < planets.size(); i++) {
               if (planets.get(i) != null) System.out.println("planet " + i + " - " + planets.get(i).getColor());
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
                if (choice < 0 || planets.get(choice) != null) {
                    System.out.println("Invalid planet choice.");
                    return;
                }
                controller.choosePlanet(choice);
            }
            case "" -> controller.goNext();
        }
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        currentShip = shipBoard;
        isMyTurn = shipBoard == myShipBoard;
        planets.set(choice, shipBoard);
    }
}
