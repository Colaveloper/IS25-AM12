package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.*;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliSecondShipBuildingScreen extends CliScreen {

    private final CliComponentBank cliComponentBank;
    private final CliForecast cliForecast;
    private final CliForecastCards cliForecastCards;
    private final CliComponentLegend cliComponentLegend;

    private final SecondShipBuildingState gameState;
    private final Map<ShipBoard, CliShipHandAndStash> buildingShipToCliShip;

    public CliSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        cliForecast = new CliForecast(gameState.getBlockedForecasts());
        cliForecastCards = new CliForecastCards();
        cliComponentLegend = new CliComponentLegend();
        this.buildingShipToCliShip = new HashMap<>();
        for (Player player : model.getPlayers()) {
            buildingShipToCliShip.put(player.getShipBoard(), new CliShipHandAndStash(player.getShipBoard(), player.getNickname()));
        }
        List<CliShipHandAndStash> list = new ArrayList<>(buildingShipToCliShip.values().stream().toList());
        list.sort(Comparator.comparing(CliShipHandAndStash::getNickname));
        list.remove(buildingShipToCliShip.get(myShipBoard));
        list.addFirst(buildingShipToCliShip.get(myShipBoard));
        cliAllShips = new CliAllShips(list);
    }

    @Override
    public void render() {
        if(gameState.getHasForecastDeck()) {
            cliForecastCards.getDescription().forEach(System.out::println);
        } else {
            cliComponentBank.getDescription().forEach(System.out::println);

            // First combine the forecast with the component legend
            List<String> forecastWithLegend = DescriptionUtils.sideBySide(
                cliForecast.getDescription(),
                cliComponentLegend.getDescription()
            );

            // Then combine the flight board with the forecast+legend
            DescriptionUtils.sideBySide(
                cliFlightBoard.getDescription(),
                forecastWithLegend
            ).forEach(System.out::println);

            System.out.println(
                    "firepower: "   + myShipBoard.getFirePower()/2 +
                    "\tengine power: " + myShipBoard.getEnginePower() +
                    "\tbatteries: "   + myShipBoard.getNumBatteries()
            );

            // Display hourglass status if it's running
            Hourglass hourglass = gameState.getHourglass();
            if (hourglass != null) {
                if (hourglass.getIsRunning()) {
                    System.out.println("HOURGLASS RUNNING - Time left: " + hourglass.getTimeLeft() + " seconds");
                    if (hourglass.getFlipsLeft() == 0) {
                        System.out.println("WARNING: This is the FINAL hourglass flip!");
                    }
                } else if (hourglass.getFlipsLeft() < 3) {
                    System.out.println("Hourglass ended, ready for another flip. " + (hourglass.getFlipsLeft() == 1 ? "Final flip available once you place your ship on the flight board." : ""));
                }
            }

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
                    controller.stashComponent();
                } else if (parts.length == 2) {
                    int index = Integer.parseInt(parts[1]);
                    if(myShipBoard.getStashedComponents().size() <= index) {
                        System.out.println("No stashed component found");
                        break;
                    }
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
                if (parts.length == 1) {
                    controller.rejectComponent();
                } else if (parts.length == 2 && parts[1].equalsIgnoreCase("LEFT")) {
                    // Rotate component left
                    if(myShipBoard.getLastComponent() == null) {
                        System.out.println("Nothing to rotate");
                        break;
                    }
                    Direction currentOrientation = myShipBoard.getLastComponent().getOrientation();
                    Direction newOrientation = currentOrientation.getLeft();
                    myShipBoard.getLastComponent().setOrientation(newOrientation);

                    buildingShipToCliShip.get(myShipBoard).clearHand();
                    buildingShipToCliShip.get(myShipBoard).setHand(myShipBoard.getLastComponent());
                    cliAllShips.setDirty();
                    render(); // render immediately because orientation is client-side only
                } else if (parts.length == 2 && parts[1].equalsIgnoreCase("RIGHT")) {
                    if(myShipBoard.getLastComponent() == null) {
                        System.out.println("Nothing to rotate");
                        break;
                    }
                    Direction currentOrientation = myShipBoard.getLastComponent().getOrientation();
                    Direction newOrientation = currentOrientation.getRight();
                    myShipBoard.getLastComponent().setOrientation(newOrientation);

                    buildingShipToCliShip.get(myShipBoard).clearHand();
                    buildingShipToCliShip.get(myShipBoard).setHand(myShipBoard.getLastComponent());
                    cliAllShips.setDirty();
                    render(); // render immediately because orientation is client-side only
                }
                else {
                    System.out.println("Invalid command. Use 'R' to reject the component or 'R LEFT'/'R RIGHT' to rotate it.");
                }
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
                    Direction orientation = Direction.UP;
                    if (myShipBoard.getLastComponent() != null) {
                        orientation = myShipBoard.getLastComponent().getOrientation();
                    }
                    controller.placeComponent(getPoint(input), orientation);
                }
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
        cliComponentBank.removeCovered();
        buildingShipToCliShip.get(shipBoard).setHand(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
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
        Hourglass hourglass = gameState.getHourglass();
        if (hourglass != null) {
            if (hourglass.getFlipsLeft() == 0) {
                System.out.println("Hourglass flipped for the FINAL time!");
                System.out.println("All players must complete their ships before the timer ends!");
            } else {
                System.out.println("Hourglass flipped!");
            }
        }
    }

    @Override
    public void notifyHourglassEnd() {}

    private boolean componentInHand(){
        return myShipBoard.getLastComponent() != null && myShipBoard.getLastPosition() == null;
    }
}
