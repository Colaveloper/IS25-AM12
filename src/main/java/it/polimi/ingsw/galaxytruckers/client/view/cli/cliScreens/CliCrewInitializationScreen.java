package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipInitializationState;

import java.awt.*;
import java.util.Map;
import java.util.Set;

/**
 * CLI screen for crew initialization in the Galaxy Truckers game.
 */
public class CliCrewInitializationScreen extends CliScreen {

    private final boolean shipNotValid;
    private CrewType currentCrewType;
    private final Map<CrewType, Set<Point>> crewTypeToPoints;

    /**
     * Creates a new crew initialization screen with the given game state.
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current ship initialization state
     */
    public CliCrewInitializationScreen(ClientModel model, ClientControllerInterface controller, ShipInitializationState gameState) {
        super(model, controller, gameState);
        shipNotValid = gameState.getCrewtypeToPoints().containsKey(model.getMyShip());
        this.crewTypeToPoints = gameState.getCrewtypeToPoints().getOrDefault(myShipBoard, null);
    }

    @Override
    public void render() {

        printShipFlightStats().forEach(System.out::println);

        if(shipNotValid) {
            if(crewTypeToPoints.containsKey(CrewType.PURPLE)) {
                System.out.println("choose position for purple alien in one of the highlighted cabins");
                currentCrewType = CrewType.PURPLE;
            } else if (crewTypeToPoints.containsKey(CrewType.BROWN)) {
                System.out.println("choose position for brown alien in one of the highlighted cabins");
                currentCrewType = CrewType.BROWN;
            }
            else{
                System.out.println("all aliens places, other cabins will be filled with humans, ");
                currentCrewType = CrewType.HUMAN;
            }

            //model.setSelectablePoints(model.getMyNickname(), model.getUnplacedCrew().get(currentCrewType));
            printActions();
        }
        else {
            System.out.println("empty cabins will be filled with humans, waiting for other players to finish");
        }
    }

    @Override
    public void parseAndInvoke(String input) {
        if(!shipNotValid) {
            System.out.println("your ship is valid, wait for other players to finish");
            return;
        }
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "P"-> {
                Point point = getPoint(input);
                if (!myShipBoard.getShipArea().contains(point)) {
                    System.out.println("Cannot place alien outside of the ship");
                    return;
                }
                if (!crewTypeToPoints.get(currentCrewType).contains(point)) {
                    System.out.println("cabin is not valid for " + currentCrewType + " alien");
                    return;
                }
                controller.initializeCabin(getPoint(input), currentCrewType);
            }
            case ""-> controller.goNext();
        }
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }
}
