package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.shared.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.client.model.state.HandleProjectileState;

class CliProjectilesScreenTest extends CliScreenSetup {
    static CliProjectilesScreen screen;
    static HandleProjectileState state;

    static void setUp() {
        CliScreenSetup.shipSetUp();
        state = new HandleProjectileState(model.getMyShip(), shipBoard, new Projectile(7, Direction.UP, ProjectileType.BIGMETEOR), shipBoard.getActivatables().keySet());
        screen = new CliProjectilesScreen(model, controller, state);
    }

    public static void main(String[] args) {
        setUp();
        screen.render();
    }
}