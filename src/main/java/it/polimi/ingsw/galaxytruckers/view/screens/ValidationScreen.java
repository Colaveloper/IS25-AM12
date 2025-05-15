package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cli.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValidationScreen extends ScreenStrategy{

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public ValidationScreen(ClientModel model) {
        super(model);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {

    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) throws IOException {

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
