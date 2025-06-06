package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

import java.awt.*;
import java.util.Map;

public class CliLoseGoodsScreen extends CliScreen {

    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    public CliLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to lose goods");
            return false;
        }

        if (!isFormatLegal(input)) {
            return false;
        }

        Point p = getPoint(input);
        return currentShip.getCargoHolds().containsKey(p) &&
               !currentShip.getCargoHolds().get(p).getGoods().isEmpty();
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

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

//    @Override
//    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
//        CliShipBoard ship = shipToCliShip.get(shipBoard);
//        cliAllShips.setDirty();
//    }
}
