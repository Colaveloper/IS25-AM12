package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GoodsBuffer;

import java.awt.*;

public class CliGoodsScreen extends CliAdventureScreen {

    private final GoodsBuffer goodsBuffer;

    public CliGoodsScreen(ClientModel model, ControllerToServer controller, AddGoodsState gameState) {
        super(model, controller, gameState);
        this.goodsBuffer = gameState.getGoodsBuffer();
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
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
//            System.out.println("Use P x y TYPE to place goods or R x y TYPE to remove goods");
//            System.out.println("Goods types: RED, GREEN, BLUE, YELLOW");
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
        if(input.trim().equalsIgnoreCase("Y")) {
            controller.giveUp();
            return;
        }
        String[] parts = input.split("\\s+");
        if (!isMyTurn) {
            System.out.println("It's not your turn, unreachable statement");
            return;
        }
        switch (parts[0].toUpperCase()) {
            case "P" -> {
                GoodsType goodsType = GoodsType.valueOf(parts[3].trim().toUpperCase());
                Point p = getPoint(input);

                if(!(goodsBuffer.getGoodsBuffer().getOrDefault(goodsType, 0) > 0)) {
                    System.out.println("no goods of that type");
                    return;
                }
                if(!currentShip.getCargoHolds().containsKey(p)){
                    System.out.println("no cargo holds in that position");
                    return;
                }
                if (goodsBuffer.getGoodsBuffer().getOrDefault(goodsType, 0) <= 0) {
                    System.out.println("No " + goodsType + " goods available in buffer");
                    return;
                }
                if (!currentShip.getCargoHolds().containsKey(p)) {
                    System.out.println("No cargo hold at this position");
                    return;
                }
                controller.placeGoods(p, goodsType);
            }
            case "R" -> {
                GoodsType goodsType = GoodsType.valueOf(parts[3].toUpperCase());
                Point p = getPoint(input);

                if(!currentShip.getCargoHolds().containsKey(p)){
                    System.out.println("no cargo holds in that position");
                    return;
                }

                CargoHold cargoHold = currentShip.getCargoHolds().get(p);
                if (!cargoHold.getGoods().containsKey(goodsType)) {
                    System.out.println("No " + goodsType + " goods at this position");
                    return;
                }
                controller.removeGoods(p, goodsType);
            }
            case "" -> controller.goNext();
        }
    }

    // place/remove goods are done in state, cli is updated in cliScreen
}
