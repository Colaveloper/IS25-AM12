package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.ComponentType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliCrewInitialization extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliCrewInitialization(ClientModel model, ControllerToServer controller) {
        super(model, controller);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);

    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        CrewType crewType = model.getUnplacedCrewType();

        output.add("choose position for" + crewType + "in one of the highlighted cabins");
        model.setSelectablePoints(model.getMyNickname(), model.getUnplacedCrewPoints(crewType));

        return output;
    }

    @Override
    public boolean isLegalInput(String input) {
        // Validate format using regex
        if (!input.matches("\\d+ \\d+")) {
            return false;
        }

        // Split input and parse numbers
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);

        // Create point and check list
        Point inputPoint = new Point(x, y);
        return model.getSelectablePoints().contains(inputPoint);
    }

    @Override
    public void parseAndInvoke(String input) {

    }


}
