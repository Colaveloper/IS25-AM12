package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipCorrectionState;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CliValidationScreen extends CliScreen {

    private boolean shipValid;
    private boolean shipBroken;
    private int myShipPieces;

    public CliValidationScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState gameState) {
        super(model, controller, gameState);
        shipValid = gameState.getValidShipBoards().contains(myShipBoard);
        shipBroken = false;
        if (shipValid) shipBroken = gameState.getShipPieces().containsKey(myShipBoard);
        if (shipBroken) myShipPieces = gameState.getShipPieces().get(myShipBoard).size();
        for(ShipBoard ship : gameState.getShipPieces().keySet()) {
            shipNotConnected(ship, gameState.getShipPieces().get(ship));
        }
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);

        if(shipBroken) {
            System.out.println("your ship is broken, choose a piece to keep from these");
            List<Highlights> highlights = Highlights.getSomeColors(myShipPieces);
            for(int i = 1; i <= myShipPieces; i++) {
                String colorChoice = highlights.get(i) == Highlights.RESET ? "WHITE" : highlights.get(i).toString();
                System.out.println(highlights.get(i).getHighlight() + i + " " + colorChoice + Highlights.RESET.getHighlight() + "\t");
            }
        }
        if (!shipValid) {//add else if to separate validation and ship piece choice
            System.out.println("\nyour ship has invalid component positioning, choose a component to remove");
        }
        if(!shipBroken && shipValid) {
            System.out.println("someone else has an invalid ship, wait while they correct them");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input)  {
        if (shipValid && !shipBroken) {
            System.out.println("your ship is valid, wait for other players");
            return;
        }
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()){
            case "R" -> {
                if(shipValid) {
                    System.out.println("ship is valid, wait for other players, unreachable");
                    return;
                }
                Point point = getPoint(input);
                if (!myShipBoard.getShipArea().contains(point)) {
                    System.out.println("Cannot place component outside of the ship");
                    return;
                }
                if (!myShipBoard.getComponentMap().containsKey(point)) {
                    System.out.println("There's no component in that point");
                    return;
                }
                controller.removeComponent(point);
            }

            case "K" -> {
                if(!shipBroken) {
                    System.out.println("unreachable statement");
                    return;
                }
                int pos = Integer.parseInt(parts[1]);
                if(pos >= myShipPieces || pos < 0){
                    System.out.println("invalid ship piece choice");
                    return;
                }
                controller.chooseShipPiece(Integer.parseInt(parts[1]));
            }
        }
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        ship.onRemoveComponent(point);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed){
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        for(Point point : removed){
            ship.onRemoveComponent(point);
        }
        if(myShipBoard == shipBoard) {
            shipBroken = false;
            shipValid = true;
        }
        cliAllShips.setDirty();
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces){
        if(myShipBoard == shipBoard) {
            myShipPieces = shipPieces.size();
            shipBroken = true;
            shipValid = true;
        }
        shipNotConnected(shipBoard, shipPieces);
    }

    private void shipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        int numPieces = shipPieces.size();
        List<Highlights> highlights = Highlights.getSomeColors(numPieces);
        for(int i = 0; i < numPieces; i++){
            shipToCliShip.get(shipBoard).highlightPoints(shipPieces.get(i), highlights.get(i + 1));
        }
        cliAllShips.setDirty();
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard){
        if(myShipBoard == shipBoard) {
            shipBroken = false;
            shipValid = true;
        }
        cliAllShips.setDirty();
    }
}
