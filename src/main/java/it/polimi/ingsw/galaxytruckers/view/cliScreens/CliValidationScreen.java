package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliValidationScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliValidationScreen(ClientModel model, ClientController controller) {
        super(model, controller);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
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
        ComponentType type = model.getComponent(model.getMyNickname(), inputPoint).getType();
        return type != ComponentType.EMPTY_AREA && type != ComponentType.EMPTY_SPACE;
    }

    @Override
    public void parseAndInvoke(String input)  {
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Point inputPoint = new Point(x, y);
        controller.removeComponent(inputPoint);
    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        if (!model.shipIsValid()) {
            output.add("your ship is invalid, choose a component to remove");
        }
        else{
            output.add("someone else has an invalid ship, wait while they correct them");
        }

        return output;
    }
}
