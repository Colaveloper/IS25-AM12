package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.CliHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChooseShipPieceState;

import java.awt.*;
import java.util.Set;
import java.util.List;

public class CliShipPieceChoiceScreen extends CliAdventureScreen {

    private boolean shipBroken;
    private int numPieces;
    private final List<Set<Point>> points;

    /**
     * Creates a new ship piece choice screen with the given game state.
     *
     * @param model      The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState  The current choose-ship-piece state
     */
    public CliShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, ChooseShipPieceState gameState) {
        super(model, controller, gameState);
        shipBroken = isMyTurn;
        numPieces =  gameState.getShipPieces().size();
        points = gameState.getShipPieces();
        shipNotConnected(currentShip, gameState.getShipPieces());
    }

    private void shipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        int numPieces = shipPieces.size();
        List<CliHighlights> highlights = CliHighlights.getSomeColors(numPieces);
        for(int i = 0; i < numPieces; i++){
            shipToCliShip.get(shipBoard).highlightPoints(shipPieces.get(i), highlights.get(i+1));
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
        List<CliHighlights> colors= CliHighlights.getSomeColors(numPieces);
//        for(int i = 0; i < numPieces; i++){
//            shipToCliShip.get(currentShip).highlightPoints(points.get(i), colors.get(i+1));  //color ship pieces with list of colors
//        }

        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (shipBroken) {
            System.out.println("your ship is broken, choose a piece of ship to keep");
            for(int i = 1; i <= numPieces; i++) {
                System.out.println(colors.get(i).getHighlight() + (i-1) + "\t" + colors.get(i) + CliHighlights.RESET.getHighlight() + "\n");
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
