package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class CliCrewInitializationScreen extends CliScreen {

    Map<CrewType,List<Point>> crewtypeToPoints;
    private boolean shipNotValid;
    private CrewType currentCrewType;
    private ShipInitializationState gameState;

    public CliCrewInitializationScreen(ClientModel model, ControllerToServer controller,  ShipInitializationState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        shipNotValid = gameState.getCrewtypeToPoints().containsKey(model.getMyShip());
        if (shipNotValid) {
            this.crewtypeToPoints = gameState.getCrewtypeToPoints().get(model.getMyShip());
        }
    }

    @Override
    public void render() {
        printShips();

        shipNotValid = gameState.getCrewtypeToPoints().containsKey(model.getMyShip());
        if (shipNotValid) {
            crewtypeToPoints = gameState.getCrewtypeToPoints().get(model.getMyShip());
        }
        printShips();
        if(shipNotValid) {
            if(crewtypeToPoints.containsKey(CrewType.PURPLE)) {
                System.out.print("choose position for purple alien in one of the highlighted cabins or ");
                currentCrewType = CrewType.PURPLE;
            } else if (crewtypeToPoints.containsKey(CrewType.BROWN)) {
                System.out.print("choose position for purple alien in one of the highlighted cabins or ");
                currentCrewType = CrewType.BROWN;
            }
            else{
                System.out.print("all aliens places, other cabins will be filled with humans, ");
                currentCrewType = CrewType.HUMAN;
            }
            System.out.println("C to continue");
            //todo: highlight components
            //model.setSelectablePoints(model.getMyNickname(), model.getUnplacedCrew().get(currentCrewType));
        }
        else {
            System.out.println("empty cabins will be filled with humans, waiting for other players to finish");
        }

        printActions();
    }

//    @Override
//    public boolean isFormatLegal(String input) {
//
//        // Validate format using regex
//        if (!input.matches("\\d+ \\d+")) {
//            return false;
//        }
//        if(input.matches("C")) {
//            return true;
//        }
//
//        // Split input and parse numbers
//        String[] parts = input.split(" ");
//        int x = Integer.parseInt(parts[0]);
//        int y = Integer.parseInt(parts[1]);
//
//        // Create point and check list
//        Point inputPoint = new Point(x, y);
//        return model.getSelectablePoints().contains(inputPoint);
//    }

    @Override
    public void parseAndInvoke(String input) {
        if(shipNotValid) {
            if(input.matches("C")) {
                for(CrewType currentCrewType : crewtypeToPoints.keySet()) {
                    for(Point point : crewtypeToPoints.get(currentCrewType)) {
                        controller.initializeCabin(point, CrewType.HUMAN);
                    }
                }
            }
            else{
                String[] parts = input.split("\\s+");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                controller.initializeCabin(new Point(x, y), currentCrewType);
            }
        }
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){

    }
}
