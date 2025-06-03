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
        shipBroken = currentShip.equals(model.getMyShip());
        numPieces =  gameState.getShipPieces().size();
        points = gameState.getShipPieces();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()){
            case "Y" -> controller.giveUp();
            case "K" -> {
                if(!shipBroken) {
                    System.out.println("Your ship is valid");
                }
                int choice = Integer.parseInt(parts[1]);
                controller.chooseShipPiece(choice);
            }
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
            System.out.println("choose from one of the following pieces: \n");
            for(int i = 1; i <= numPieces; i++) {
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
