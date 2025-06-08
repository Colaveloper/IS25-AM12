package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChooseShipPieceState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class CliShipPieceChoiceScreen extends CliScreen {

    private boolean shipBroken;
    private int numPieces;
    private final ShipBoard currentShip;
    private final List<Set<Point>> points;

    public CliShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, ChooseShipPieceState gameState) {
        super(model, controller, gameState);
        currentShip = gameState.getShipBoard();
        shipBroken = currentShip.equals(myShipBoard);
        numPieces =  gameState.getShipPieces().size();
        points = gameState.getShipPieces();
        shipNotConnected(currentShip, gameState.getShipPieces());
    }

    private void shipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        int numPieces = shipPieces.size();
        List<Highlights> highlights = Highlights.getSomeColors(numPieces);
        for(int i = 0; i < numPieces; i++){
            shipToCliShip.get(shipBoard).highlightPoints(shipPieces.get(i), highlights.get(i));
        }
        cliAllShips.setDirty();
    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        if (!myShipBoard.equals(currentShip)) {
            System.out.println("It's not your turn");
            return;
        }
        String[] parts = input.split("\\s+");
        if (parts[0].equalsIgnoreCase("K")) {
            int pos = Integer.parseInt(parts[1]);
            if(pos >= numPieces || pos < 0){
                System.out.println("invalid ship piece choice");
                return;
            }
            controller.chooseShipPiece(Integer.parseInt(parts[1]));
        }
    }

    @Override
    public void render() {
        List<Highlights> colors= Highlights.getSomeColors(numPieces);
        for(int i = 0; i < numPieces; i++){
            shipToCliShip.get(currentShip).highlightPoints(points.get(i), colors.get(i));  //color ship pieces with list of colors
        }

        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (shipBroken) {
            System.out.println("your ship is broken, choose a piece of ship to keep");
            for(int i = 0; i < numPieces; i++) {
                System.out.println(colors.get(i).getHighlight() + i + "\t" + colors.get(i) + Highlights.RESET.getHighlight() + "\n");
            }
        }
        else{
            System.out.println("someone else has a broken ship, wait while they choose what piece to keep");
        }
        printActions();
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed){
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        for(Point point : removed){
            ship.onRemoveComponent(point);
        }
        if(myShipBoard == shipBoard) shipBroken = false;
        numPieces = 1;
        cliAllShips.setDirty();
    }
}
