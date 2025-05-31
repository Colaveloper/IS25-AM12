package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GoodsBuffer;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CliGoodsScreen extends CliScreen {

    private final boolean isMyTurn;
    private final ShipBoard currentShip;
    private final GoodsBuffer goodsBuffer;

    public CliGoodsScreen(ClientModel model, ControllerToServer controller, AddGoodsState gameState) {
        super(model, controller, gameState);
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
        this.goodsBuffer = gameState.getGoodsBuffer();
    }

    @Override
    public boolean isInputLegal(String input) {
        if (!isMyTurn) {
            System.out.println("It's not your turn to manage goods");
            return false;
        }

        if (!isFormatLegal(input)) {
            return false;
        }

        String[] parts = input.split("\\s+");
        if (parts.length < 4) {
            return false;
        }

        try {
            Point p = getPoint(input);
            GoodsType goodsType = GoodsType.valueOf(parts[3].toUpperCase());

            if (parts[0].equalsIgnoreCase("P")) {
                boolean hasGoodInBuffer = goodsBuffer.getGoodsBuffer().getOrDefault(goodsType, 0) > 0;
                boolean hasCargoBay = currentShip.getCargoHolds().containsKey(p);

                return hasGoodInBuffer && hasCargoBay;
            } else if (parts[0].equalsIgnoreCase("R")) {
                boolean hasCargoBay = currentShip.getCargoHolds().containsKey(p);
                boolean hasGoodInCargo = hasCargoBay && currentShip.getCargoHolds().get(p).getGoods().containsKey(goodsType);

                return hasCargoBay && hasGoodInCargo;
            }
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid goods type");
            return false;
        }
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        if (isMyTurn) {
            System.out.println("Your turn to manage goods");
            System.out.println("Goods in buffer:");
            for (GoodsType type : GoodsType.values()) {
                int amount = goodsBuffer.getGoodsBuffer().getOrDefault(type, 0);
                if (amount > 0) {
                    System.out.println(type + ": " + amount);
                }
            }
            System.out.println("Cargo holds with goods: " + countCargoHoldsWithGoods());
            System.out.println("Use P x y TYPE to place goods or R x y TYPE to remove goods");
            System.out.println("Goods types: RED, GREEN, BLUE, YELLOW");
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to manage goods");
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
        if (!isMyTurn) {
            System.out.println("It's not your turn to manage goods");
            return;
        }

        if (input.equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }

        String[] parts = input.split("\\s+");
        if (parts.length < 4) {
            System.out.println("Invalid input format. Use: P x y TYPE or R x y TYPE");
            return;
        }

        try {
            Point p = getPoint(input);
            GoodsType goodsType = GoodsType.valueOf(parts[3].toUpperCase());

            if (parts[0].equalsIgnoreCase("P")) {
                if (goodsBuffer.getGoodsBuffer().getOrDefault(goodsType, 0) <= 0) {
                    System.out.println("No " + goodsType + " goods available in buffer");
                    return;
                }
                if (!currentShip.getCargoHolds().containsKey(p)) {
                    System.out.println("No cargo hold at this position");
                    return;
                }
                controller.placeGoods(p, goodsType);
            } else if (parts[0].equalsIgnoreCase("R")) {
                if (!currentShip.getCargoHolds().containsKey(p)) {
                    System.out.println("No cargo hold at this position");
                    return;
                }
                CargoHold cargoHold = currentShip.getCargoHolds().get(p);
                if (!cargoHold.getGoods().containsKey(goodsType)) {
                    System.out.println("No " + goodsType + " goods at this position");
                    return;
                }
                controller.removeGoods(p, goodsType);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid goods type. Use: RED, GREEN, BLUE, YELLOW");
        }
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        cliAllShips.setDirty();
    }
}
