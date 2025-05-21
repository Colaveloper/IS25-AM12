package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.Highlights;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliShipPieceChoiceScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;
    int numPieces;

    public CliShipPieceChoiceScreen(ClientModel model, ClientController controller) {
        super(model, controller);

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);

        numPieces =  model.getSelectableShipPieces().size();
    }

    @Override
    public boolean isLegalInput(String input) {
        return Integer.parseInt(input) <= numPieces &&
                Integer.parseInt(input) > 0;
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.chooseShipPiece(Integer.parseInt(input));
    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());

        if (!model.shipIsValid()) {
            output.add("your ship is broken, choose a piece of ship to keep");
            output.add("choose from one of the following pieces: \n");
            List<Highlights> highlights = Highlights.getSomeColors(numPieces);
            for(int i = 1; i <= numPieces; i++) {
                output.add(highlights.get(i).getHighlight() + i + "\t" + highlights.get(i) + Highlights.RESET.getHighlight() + "\n"); //todo: add color
            }
        }
        else{
            output.add("someone else has a broken ship, wait while they choose what piece to keep");
        }

        return output;
    }
}
