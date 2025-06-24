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
            System.out.println("Your turn to lose goods");
            System.out.println("Select cargo holds to discard goods from");
            System.out.println("Cargo holds with goods: " + countCargoHoldsWithGoods());
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to lose goods");
        }
        printActions();
    }

    private int countCargoHoldsWithGoods() {
        int count = 0;
        for (CargoHold cargoHold : currentShip.getCargoHolds().values()) {
            if (!cargoHold.getGoods().isEmpty()) {
                count++;
            }
        }
        return count;
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
        if (parts[0].equalsIgnoreCase("R")) {
            Point p = getPoint(input);
            if (!currentShip.getCargoHolds().containsKey(p)) {
                System.out.println("No cargo hold at this position");
                return;
            }

            if (currentShip.getCargoHolds().get(p).getGoods().isEmpty()) {
                System.out.println("No goods in this cargo hold");
                return;
            }
            controller.loseGoods(p);
        }
    }
}
