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

    private boolean shipNotValid;
    private boolean shipBroken;
    private int numPieces;

    public CliValidationScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState gameState) {
        super(model, controller, gameState);
        shipNotValid = gameState.getValidShipBoards().contains(myShipBoard);
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if(shipBroken) {
            System.out.println("your ship is broken, choose a piece to keep from these");
            List<Highlights> highlights = Highlights.getSomeColors(numPieces);
            for(int i = 1; i <= numPieces; i++) {
                System.out.println(highlights.get(i).getHighlight() + i + " " + highlights.get(i) + Highlights.RESET.getHighlight() + "\t");
            }
        }
        if (shipNotValid) {//add else if to separate validation and ship piece choice
            System.out.println("\nyour ship has invalid component positioning, choose a component to remove");
        }
        if(!shipBroken && !shipNotValid) {
            System.out.println("someone else has an invalid ship, wait while they correct them");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input)  {
        if (!shipNotValid && !shipBroken) {
            System.out.println("your ship is valid, wait for other players");
            return;
        }
        String[] parts = input.split("\\s+");
        if(parts.length == 2 && shipNotValid) {
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
        if (parts.length == 1 && shipBroken) {
            int pos = Integer.parseInt(parts[0]);
            if(pos >= numPieces || pos < 0){
                System.out.println("invalid ship piece choice");
                return;
            }
            controller.chooseShipPiece(Integer.parseInt(input));
        }

    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point){
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
        if(myShipBoard == shipBoard) shipBroken = false;
        cliAllShips.setDirty();
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces){
        if(myShipBoard == shipBoard) {
            shipBroken = true;
            numPieces = shipPieces.size();
        }
        cliAllShips.setDirty();
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard){
        if(myShipBoard == shipBoard) shipNotValid = false;
        cliAllShips.setDirty();
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        CliShipBoard ship = shipToCliShip.get(shipBoard);

        //todo
    }

}
