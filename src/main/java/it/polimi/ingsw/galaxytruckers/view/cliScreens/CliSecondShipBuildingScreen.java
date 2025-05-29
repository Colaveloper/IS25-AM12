package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.*;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliSecondShipBuildingScreen extends CliScreen {
    private SecondShipBuildingState gameState;

    private final CliComponentBank cliComponentBank;
    private final CliForecast cliForecast;
    private final CliAllShips cliAllShips;
    private final Map<ShipBoard, CliShipBoard> shipToCliShip;

    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);
        this.shipToCliShip = new HashMap<>();
        for (Player player : model.getPlayers()) {
            shipToCliShip.put(player.getShipBoard(), new CliShipHandAndStash(player.getShipBoard(), player.getNickname()));
        }
        cliAllShips = new CliAllShips(shipToCliShip.values().stream().toList());
        cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        cliForecast = null; //TODO: remove
        //cliForecast = new CliForecast(gameState.getBlockedForecasts());
        this.gameState = gameState;
    }

    @Override
    public void render() {
        //<GameColor> list = gameState.getLockedForecastsProperty().getUnmodifiableView();
//        if(list != null && list.contains(model.getMyShip().getColor())) {
//            System.out.println("Cards in the forecast deck:\n");
//            for (AdventureCard card : gameState.getForecastDeck()){
//                System.out.println(new CliAdventureCard(card).getDescription());
//            }
//        }
//        else {
            cliComponentBank.getDescription().forEach(System.out::println);
            cliForecast.getDescription().forEach(System.out::println);
            cliFlightBoard.getDescription().forEach(System.out::println);
            cliAllShips.getDescription().forEach(System.out::println);
//        }
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0]) {
            case "C":
                controller.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]) - 1;
                    //int componentId = gameState.getComponentBank().getUncoveredComponentsProperty().get(index).getId();
                    //controller.requestComponent(componentId);
                }
                break;

            case "S":
                if (parts.length == 1) {
                    controller.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.acquireForecast(index);
                }
                break;

            case "R":
                controller.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    controller.placeComponent(getPoint(input), 0); //todo add orientation
                }
                break;

            case "L":
                //model.rotateCurrentComponentLeft();
                //todo rotate component in model
                break;

            case "H":
                controller.flipHourglass();
                break;

            case "E":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    controller.placeShipOnFlightboard(index);
                }
                break;

            default:
                // should be impossible
                System.out.println("Invalid command.");
                break;
        }
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        cliComponentBank.removeCovered();
        this.render();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        super.notifyRequestComponent(shipBoard, component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard) {
        super.notifyStashComponent(shipBoard);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard) {
        super.notifyRejectComponent(shipBoard);
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        super.notifyGrabStashedComponent(shipBoard, index);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        super.notifyPlaceComponent(shipBoard, point, orientation);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        super.notifyFlipHourglass(shipBoard);
    }

    @Override
    public void notifyHourglassEnd() {
        super.notifyHourglassEnd();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        super.notifyFlightBoardPosition(shipBoard, position);
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        super.notifyPeekForecast(shipBoard, deckIndex);
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        super.setForecastDeck(adventureCards);
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard) {
        super.notifyReleaseForecast(shipBoard);
    }
}
