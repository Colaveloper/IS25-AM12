package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.*;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.TestShipBuildingState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliTestShipBuildingScreen extends CliScreen {

    private final CliComponentBank cliComponentBank;
    private final TestShipBuildingState gameState;
    private final Map<ShipBoard, CliShipAndHand> buildingShipToCliShip;
    private final CliComponentLegend cliComponentLegend;



    public CliTestShipBuildingScreen(ClientModel model, ControllerToServer controller, TestShipBuildingState gameState) {
        super(model, controller, gameState);
        this.cliComponentBank = new CliComponentBank(gameState.getComponentBank());
        this.gameState = gameState;
        cliComponentLegend = new CliComponentLegend();
        this.buildingShipToCliShip = new HashMap<>();
        for (Player player : model.getPlayers()) {
            buildingShipToCliShip.put(player.getShipBoard(), new CliShipAndHand(player.getShipBoard(), player.getNickname()));
        }
        List<CliShipAndHand> list = new ArrayList<>(buildingShipToCliShip.values().stream().toList());
        list.sort(Comparator.comparing(CliShipAndHand::getNickname));
        list.remove(buildingShipToCliShip.get(myShipBoard));
        list.addFirst(buildingShipToCliShip.get(myShipBoard));
        cliAllShips = new CliAllShips(list);
    }

    @Override
    public void render() {
        cliComponentBank.getDescription().forEach(System.out::println);

        // Then combine the flight board with the forecast+legend
        DescriptionUtils.sideBySide(
                cliFlightBoard.getDescription(),
                cliComponentLegend.getDescription()
        ).forEach(System.out::println);

        System.out.println(
                "firepower: "       + myShipBoard.getFirePower()/2 +
                "\tengine power: "  + myShipBoard.getEnginePower() +
                "\tbatteries: "     + myShipBoard.getNumBatteries()
        );

        cliAllShips.getDescription().forEach(System.out::println);
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        if(parts[0].isEmpty()) System.out.println("Invalid input");

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

            case "G":
                controller.grabPlacedComponent();
                break;

            case "E":
                controller.placeShipOnFlightBoard();
                break;
            case "R":
                if (parts.length == 1) {
                    controller.rejectComponent();
                }
                else if (parts.length == 2) {
                    Component lastComponent = myShipBoard.getLastComponent();
                    if(lastComponent == null) {
                        System.out.println("Nothing to rotate");
                        break;
                    }
                    Direction lastDirection = lastComponent.getOrientation();
                    if(parts[1].equalsIgnoreCase("LEFT")) lastComponent.setOrientation(lastDirection.getLeft());
                    else if(parts[1].equalsIgnoreCase("RIGHT")) lastComponent.setOrientation(lastDirection.getRight());

                    cliAllShips.setDirty();
                    buildingShipToCliShip.get(myShipBoard).setDirty();

                    buildingShipToCliShip.get(myShipBoard).clearHand();
                    buildingShipToCliShip.get(myShipBoard).setHand(lastComponent);

                    render();
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
    public void notifyGrabPlacedComponent(ShipBoard shipBoard, Point prevPosition) {
        CliShipAndHand ship = buildingShipToCliShip.get(shipBoard);
        ship.onRemoveComponent(prevPosition);
        ship.setHand(shipBoard.getLastComponent());
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        CliShipAndHand ship = buildingShipToCliShip.get(shipBoard);
        ship.clearHand();
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        CliShipAndHand ship = buildingShipToCliShip.get(shipBoard);
        ship.onRemoveComponent(oldPosition);
        cliComponentBank.addUncovered(component);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        cliFlightBoard.setPosition(shipBoard, position);
        cliFlightBoard.setDirty();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        Component placedComponent = shipBoard.getComponentMap().get(point);
        if (placedComponent != null) {
            CliShipAndHand ship = buildingShipToCliShip.get(shipBoard);
            ship.onPutComponent(point, placedComponent);
            ship.clearHand();
            cliAllShips.setDirty();
        }
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition) {
        Component placedComponent = shipBoard.getComponentMap().get(newPoint);
        if (placedComponent != null) {
            CliShipAndHand ship = buildingShipToCliShip.get(shipBoard);
            ship.onPutComponent(newPoint, placedComponent);
            ship.onRemoveComponent(oldPosition);
            cliAllShips.setDirty();
        }
    }

    private boolean componentInHand(){
        return myShipBoard.getLastComponent() != null && myShipBoard.getLastPosition() == null;
    }
}
