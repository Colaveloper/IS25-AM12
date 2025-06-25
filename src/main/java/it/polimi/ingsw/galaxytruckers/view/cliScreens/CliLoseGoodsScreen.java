package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

import java.awt.*;

public class CliLoseGoodsScreen extends CliAdventureScreen {

    /**
     * Creates a new lose goods screen with the given game state.
     *
     * @param model      The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState  The current remove-goods state
     */
    public CliLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if (isMyTurn) {
            if(myShipBoard.getCargoOnShip() == 0) System.out.println("Your turn to lose batteries");
            else System.out.println("Your turn to lose goods");
            System.out.println("Select cargo holds to discard goods from");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to lose goods");
        }
        printActions();
    }


    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        String[] parts = input.split("\\s+");
        if (!isMyTurn) {
            System.out.println("It's not your turn, this line should never be reached");
            return;
        }
        if (parts[0].equalsIgnoreCase("L")) {
            Point p = getPoint(input);
            if(myShipBoard.getCargoOnShip() == 0) {
                if (!myShipBoard.getBatteries().containsKey(p)){
                    System.out.println("No battery at this position");
                    return;
                }
                if(myShipBoard.getBatteries().get(p).getNumBatteries() == 0) {
                    System.out.println("Battery at this position is not active");
                    return;
                }
            }
            else {
                if (!myShipBoard.getCargoHolds().containsKey(p)) {
                    System.out.println("No cargo hold at this position");
                    return;
                }
                if (myShipBoard.getCargoHolds().get(p).getGoods().isEmpty()) {
                    System.out.println("No goods in this cargo hold");
                    return;
                }

            }
            controller.loseGoods(p);
        }
    }
}
