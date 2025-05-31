package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class CliCrewInitializationScreen extends CliScreen {

    private final boolean shipNotValid;
    private CrewType currentCrewType;
    private final Map<ShipBoard, Map<CrewType,List<Point>>> crewtypeToPoints;

    public CliCrewInitializationScreen(ClientModel model, ControllerToServer controller,  ShipInitializationState gameState) {
        super(model, controller, gameState);
        this.crewtypeToPoints = gameState.getCrewtypeToPoints();
        shipNotValid = gameState.getCrewtypeToPoints().containsKey(model.getMyShip());
    }

    @Override
    public void render() {

        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if(shipNotValid) {
            if(crewtypeToPoints.containsKey(CrewType.PURPLE)) {
                System.out.println("choose position for purple alien in one of the highlighted cabins");
                currentCrewType = CrewType.PURPLE;
            } else if (crewtypeToPoints.containsKey(CrewType.BROWN)) {
                System.out.println("choose position for purple alien in one of the highlighted cabins");
                currentCrewType = CrewType.BROWN;
            }
            else{
                System.out.println("all aliens places, other cabins will be filled with humans, ");
                currentCrewType = CrewType.HUMAN;
            }
            //todo: highlight components
            //model.setSelectablePoints(model.getMyNickname(), model.getUnplacedCrew().get(currentCrewType));
        }
        else {
            System.out.println("empty cabins will be filled with humans, waiting for other players to finish");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {

        if(!shipNotValid) {
            System.out.println("your ship is valid, wait for other players to finish");
            return;
        }
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "P":
                Point point = getPoint(input);
                if (!myShipBoard.getShipArea().contains(point)) {
                    System.out.println("Cannot place alien outside of the ship");
                    return;
                }
                if (!crewtypeToPoints.get(myShipBoard).get(currentCrewType).contains(point)) {
                    System.out.println("cabin is not valid for " + currentCrewType + " alien");
                    return;
                }
                controller.initializeCabin(getPoint(input), currentCrewType);
            case "":
                for (CrewType currentCrewType : crewtypeToPoints.get(myShipBoard).keySet()) {
                    for (Point points : crewtypeToPoints.get(myShipBoard).get(currentCrewType)) {
                        controller.initializeCabin(points, CrewType.HUMAN);
                    }
                }
                break;
        }
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);

        //todo
    }
}
