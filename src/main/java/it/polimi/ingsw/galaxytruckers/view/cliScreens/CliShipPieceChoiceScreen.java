package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.util.ArrayList;
import java.util.List;

public class CliShipPieceChoiceScreen extends CliScreen {

    private boolean shipNotValid;
    int numPieces;

    public CliShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);

        shipNotValid = gameState.getShipBoard().equals(model.getMyShip());

        numPieces =  gameState.getShipPieces().size();
    }

    @Override
    public void parseAndInvoke(String input) {
        if(shipNotValid) {
            controller.chooseShipPiece(Integer.parseInt(input));
        }
    }

    @Override
    public void render() {
        printShips();

        if (shipNotValid) {
            System.out.println("your ship is broken, choose a piece of ship to keep");
            System.out.println("choose from one of the following pieces: \n");
            List<Highlights> highlights = Highlights.getSomeColors(numPieces);
            for(int i = 1; i <= numPieces; i++) {
                System.out.println(highlights.get(i).getHighlight() + i + "\t" + highlights.get(i) + Highlights.RESET.getHighlight() + "\n");
            }
        }
        else{
            System.out.println("someone else has a broken ship, wait while they choose what piece to keep");
        }

        printActions();
    }
}
