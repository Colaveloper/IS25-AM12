package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ChoosePlanetState;

import java.awt.*;

/**
 * CLI screen for choosing a planet to land on during the game.
 */
public class CliPlanetScreen extends CliAdventureScreen {
    // this screen does all players without stateChange unlike the others
    // so currentShip and isMyTurn are not final

    private boolean isMyTurn;
    private ShipBoard currentShip;
    private final ShipBoard[] choiceToShip;

    /**
     * Creates a new planet choice screen with the given game state.
     *
     * @param model      The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState  The current choose-planet state
     */
    public CliPlanetScreen(ClientModel model, ClientControllerInterface controller, ChoosePlanetState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = gameState.isMyTurn();
        this.choiceToShip = gameState.getOptions();
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        printCurrentCard().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
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

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        currentShip = shipBoard;
        isMyTurn = currentShip.equals(myShipBoard);
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        super.notifyPlaceGoods(shipBoard, point, goodsType);
    }
}
