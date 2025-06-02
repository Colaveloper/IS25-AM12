package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;

import java.awt.*;
import java.util.Set;

public class CliProjectilesScreen extends CliScreen {

    private final HandleProjectileState gameState;
    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    public CliProjectilesScreen(ClientModel model, ControllerToServer controller, HandleProjectileState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }


    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);

        String direction = switch (gameState.getProjectile().direction()) {
            case 0 -> "front on column ";
            case 1 -> "right or left on row ";
            case 2 -> "back on column ";
            case 3 -> "right or left on row ";
            default -> "unknown direction ";
        };

        System.out.println("A " + gameState.getProjectile().type() +
                " is approaching from " + direction + gameState.getProjectile().roll()
        );

        if (isMyTurn) {
            System.out.println("Your turn to handle the projectile");
            System.out.println("Select a component to activate or a battery to use");
            System.out.println("Available components: " + currentShip.getActivatables().size());
            System.out.println("Available batteries: " + currentShip.getBatteries().size());
        } else {
            System.out.println("Waiting for " + currentShip.getColor() + " ship to handle the projectile");
        }

        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String[] parts = input.split("\\s+");
        switch (parts[0].toUpperCase()) {
            case "Y"-> controller.giveUp();
            case "A"-> {
                Point p = getPoint(input);
                if(!isMyTurn){
                    System.out.println("It's not your turn to handle projectiles");
                    return;
                }
                if (!currentShip.getBatteries().containsKey(p) && !currentShip.getActivatables().containsKey(p)) {
                    System.out.println("Invalid position. Please select a component or battery.");
                    return;
                }
                if (currentShip.getBatteries().containsKey(p)) {
                    controller.useBattery(p);
                } else if (currentShip.getActivatables().containsKey(p)) {
                    controller.activateComponent(p);
                }
            }
            case "" -> {
                if (!isMyTurn) {
                    System.out.println("It's not your turn to handle projectiles");
                    return;
                }
                controller.goNext();
            }
        }
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        ship.highlightPoints(Set.of(point), Highlights.CYAN);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        ship.highlightPoints(Set.of(point), Highlights.GREEN);
        cliAllShips.setDirty();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        ship.onRemoveComponent(point);
        cliAllShips.setDirty();
    }

}
