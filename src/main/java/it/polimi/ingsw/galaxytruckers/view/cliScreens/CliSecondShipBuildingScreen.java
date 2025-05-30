package it.polimi.ingsw.galaxytruckers.view.cliScreens;

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

    private final CliComponentBank cliComponentBank;
    private final CliForecast cliForecast;
    private final CliForecastCards cliForecastCards;
    private final CliAllShips cliAllShips;
    private final Map<ShipBoard, CliShipHandAndStash> shipToCliShip;
    private boolean hasForecastDeck = false;

    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);
        this.shipToCliShip = new HashMap<>();
        for (Player player : model.getPlayers()) {
            shipToCliShip.put(player.getShipBoard(), new CliShipHandAndStash(player.getShipBoard(), player.getNickname()));
        }
        cliAllShips = new CliAllShips(shipToCliShip.values().stream().toList());
        cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        cliForecast = new CliForecast(gameState.getBlockedForecasts());
        cliForecastCards = new CliForecastCards();
    }

    @Override
    public void render() {
        if(hasForecastDeck) {
            cliForecastCards.getDescription().forEach(System.out::println);
        }
        else {
            cliComponentBank.getDescription().forEach(System.out::println);
            cliForecast.getDescription().forEach(System.out::println);
            cliFlightBoard.getDescription().forEach(System.out::println);
            cliAllShips.getDescription().forEach(System.out::println);
        }
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
            case "":
                controller.releaseForecast();
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
        shipToCliShip.get(shipBoard).setHand(component);
        cliAllShips.setDirty();
        // shipToCliShip.get(shipBoard).setHand(component);
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        cliComponentBank.removeUncovered(component);
        shipToCliShip.get(shipBoard).setHand(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        ship.clearHand();
        ship.onStash(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        shipToCliShip.get(shipBoard).clearHand();
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        ship.onRemoveComponent(oldPosition);
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        ship.setHand(component);
        ship.onGrabStashed(index);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        Component placedComponent = shipBoard.getComponentMap().get(point);
        if (placedComponent != null) {
            CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
            ship.onPutComponent(point, placedComponent);
            ship.clearHand();
            cliAllShips.setDirty();
        }
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        // Update the hourglass status in the UI
        // cliFlightBoard.setDirty();
    }

    @Override
    public void notifyHourglassEnd() {
        // Mark the flight board as dirty to update the hourglass status
        // cliFlightBoard.setDirty();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        // Update the flight board position and mark it as dirty
        // cliFlightBoard.setDirty();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        // Mark the forecast display as dirty to update it
        // cliForecast.setDirty();
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        cliForecastCards.setCards(adventureCards);
        hasForecastDeck = true;
        cliForecastCards.setDirty();
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {
        // Update the forecast display when a forecast is released
        if(model.getMyShip() == shipBoard) {
            hasForecastDeck = false;
        }
        cliForecast.setDirty();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
        ship.onRemoveComponent(point);
        cliAllShips.setDirty();
    }
}
