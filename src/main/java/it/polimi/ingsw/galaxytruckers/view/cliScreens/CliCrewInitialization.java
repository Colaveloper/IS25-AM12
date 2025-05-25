package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;

public class CliCrewInitialization extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliCrewInitialization(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);

        flightBoard = new CliFlightBoard(model);
        allShips = new CliAllShips(model);
    }

    @Override
    public void render() {
        System.out.println(flightBoard.getDescription());
        System.out.println(allShips.getDescription());

        CrewType crewType = model.getUnplacedCrewType();
        if(!model.shipIsValid() && !crewType.equals(CrewType.HUMAN)) {
            System.out.println("choose position for" + crewType + "in one of the highlighted cabins");
            System.out.println("or C to continue");
            model.setSelectablePoints(model.getMyNickname(), model.getUnplacedCrew().get(crewType));
        }
        else {
            System.out.println("empty cabins will be filled with humans, waiting for other players to finish");
        }
    }

//    @Override
//    public boolean isLegalInput(String input) {
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
        if(!model.shipIsValid()) {
            if(input.matches("C")) {
                for(CrewType crewType : model.getUnplacedCrew().keySet()) {
                    for(Point point : model.getUnplacedCrew().get(crewType)) {
                        controller.initializeCabin(point, CrewType.HUMAN);
                    }
                }
            }
            else{
                String[] parts = input.split("\\s+");
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                controller.initializeCabin(new Point(x, y), model.getUnplacedCrewType());
            }
        }
    }


}
