package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;

import java.awt.*;

/**
 * CLI screen for handling projectiles in the game.
 */
public class CliProjectilesScreen extends CliActivationScreen {

    private final HandleProjectileState gameState;
    private final boolean isMyTurn;
    private final ShipBoard currentShip;

    /**
     * Creates a new projectiles screen with the given game state.
     *
     * @param model      The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState  The current handle-projectile state
     */
    public CliProjectilesScreen(ClientModel model, ControllerToServer controller, HandleProjectileState gameState) {
        super(model, controller, gameState);
        this.gameState = gameState;
        this.currentShip = gameState.getShipBoard();
        this.isMyTurn = currentShip.equals(model.getMyShip());
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        String direction = switch (gameState.getProjectile().direction()) {
            case Direction.UP   -> "front on column ";
            case Direction.RIGHT-> "right on row ";
            case Direction.DOWN -> "back on column ";
            case Direction.LEFT -> "left on row ";
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
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        CliShipBoard ship = shipToCliShip.get(shipBoard);
        ship.onRemoveComponent(point);
        cliAllShips.setDirty();
    }
}
