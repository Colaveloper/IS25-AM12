package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cli.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShipPieceChoiceScreen extends ScreenStrategy{

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public ShipPieceChoiceScreen(ClientModel model) {
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
            output.add("your ship is broken, choose a piece of ship to keep");
        }
        else{
            output.add("someone else has a broken ship, wait while they choose what piece to keep");
        }

        return output;
    }
}
