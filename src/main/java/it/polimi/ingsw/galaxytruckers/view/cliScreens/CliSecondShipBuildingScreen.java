package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
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

    private boolean hasStashed;
    private final SecondShipBuildingState gameState;
    private final Map<ShipBoard, CliShipHandAndStash> buildingShipToCliShip;


    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        hasStashed = false;
        cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        cliForecast = new CliForecast(gameState.getBlockedForecasts());
        cliForecastCards = new CliForecastCards();
        this.buildingShipToCliShip = new HashMap<>();
        for (Player player : model.getPlayers()) {
            buildingShipToCliShip.put(player.getShipBoard(), new CliShipHandAndStash(player.getShipBoard(), player.getNickname()));
        }
        cliAllShips = new CliAllShips(buildingShipToCliShip.values().stream().toList());
    }

    @Override
    public void render() {
        if(gameState.getHasForecastDeck()) {
            cliForecastCards.getDescription().forEach(System.out::println);
        } else {
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
        if(parts[0].isEmpty()) {
            if (gameState.getHasForecastDeck()) {
                controller.releaseForecast();
                return;
            }
            else System.out.println("Invalid input");
        }

        switch (parts[0].toUpperCase()) {
            case "C":
                if(componentInHand()) {
                    System.out.println("You already have a component in hand");
                    break;
                }
                controller.requestRandComponent();
                break;

            case "U":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    int uncovered = gameState.getComponentBank().getUncoveredComponents().size();
                    if (index < 0 || index >= uncovered) {
                        System.out.println("Invalid component index, must be from 0 to " + (uncovered - 1));
                        break;
                    }
                    if(componentInHand()) {
                        System.out.println("You already have a component in hand");
                        break;
                    }
                    controller.requestComponent(cliComponentBank.getUncoveredComponents().get(index).getId());
                }
                break;

            case "S":
                if (parts.length == 1) {
                    if(myShipBoard.getLastComponent() == null) {
                        System.out.println("No component to stash");
                        break;
                    }
                    if(myShipBoard.getStashedComponents().size() >= 2) {
                        System.out.println("Your stash is full");
                        break;
                    }
                    controller.stashComponent();
                } else if (parts.length == 2) {
                    if(componentInHand()){
                        System.out.println("Your hand is full");
                        break;
                    }
                    int index = Integer.parseInt(parts[1]);
                    controller.grabStashedComponent(index);
                }
                break;

            case "F":
                if (parts.length == 2) {
                    if(componentInHand()){
                        System.out.println("Free your hand before picking forecast");
                        break;
                    }
                    int index = Integer.parseInt(parts[1]);
                    controller.acquireForecast(index);
                }
                break;

            case "R":
                if(hasStashed){
                    System.out.println("Cant reject stashed component in hand");
                    break;
                }
                if(myShipBoard.getLastComponent() == null) {
                    System.out.println("Nothing to reject");
                    break;
                }
                controller.rejectComponent();
                break;

            case "P":
                if (parts.length == 3) {
                    Point point = getPoint(input);
                    if(!myShipBoard.getShipArea().contains(point)){
                        System.out.println("Cannot place component outside of the ship");
                        break;
                    }
                    if(myShipBoard.getComponentMap().containsKey(point)) {
                        System.out.println("This point is already occupied");
                        break;
                    }
                    controller.placeComponent(getPoint(input), Direction.UP); //todo add orientation
                }
                break;

            case "L":
                if(myShipBoard.getLastComponent() == null) {
                    System.out.println("Nothing to rotate");
                    break;
                }
                //model.rotateCurrentComponentLeft();
                //todo rotate component in model
                break;

            case "H":
                controller.flipHourglass();
                break;

            case "E":
                if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    if(!model.getGame().getFlightBoard().getStartingPositions().contains(index)) {
                        System.out.println("invalid position");
                        break;
                    }
                    if(model.getGame().getFlightBoard().getShipToPlace().containsValue(index)) {
                        System.out.println("position already occupied");
                        break;
                    }
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
        hasStashed = false;
        cliComponentBank.removeCovered();
        buildingShipToCliShip.get(shipBoard).setHand(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        hasStashed = false;
        cliComponentBank.removeUncovered(component);
        buildingShipToCliShip.get(shipBoard).setHand(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.clearHand();
        ship.onStash(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.onRemoveComponent(oldPosition);
        ship.onStash(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        //can t reject component picked from stashed
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.clearHand();
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        //can t reject component picked from stashed
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.onRemoveComponent(oldPosition);
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        hasStashed = true;
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.setHand(component);
        ship.onGrabStashed(index);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        Component placedComponent = shipBoard.getComponentMap().get(point);
        if (placedComponent != null) {
            CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
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
        cliFlightBoard.updatePositions(shipBoard, position);
        cliFlightBoard.setDirty();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        // Mark the forecast display as dirty to update it
        cliForecast.setBlockedForecasts(deckIndex, shipBoard.getColor());
        cliForecast.setDirty();
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        cliForecastCards.setCards(adventureCards);
        cliForecastCards.setDirty();
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {
        // Update the forecast display when a forecast is released
        if(myShipBoard == shipBoard) {
            gameState.hasForecastDeck(false);
        }
        cliForecast.removeBlockedForecast(index);
        cliForecast.setDirty();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        CliShipHandAndStash ship = buildingShipToCliShip.get(shipBoard);
        ship.onRemoveComponent(point);
        cliAllShips.setDirty();
    }

    private boolean componentInHand(){
        return myShipBoard.getLastComponent() != null && myShipBoard.getLastPosition() == null;
    }
}
