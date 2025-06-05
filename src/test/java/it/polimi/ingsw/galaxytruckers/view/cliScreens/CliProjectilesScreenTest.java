package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;

class CliProjectilesScreenTest extends CliScreenSetup {
    static CliProjectilesScreen screen;
    static HandleProjectileState state;

    static void setUp() {
        CliScreenSetup.shipSetUp();
        state = new HandleProjectileState(shipBoard, new Projectile(7, Direction.UP, ProjectileType.BIGMETEOR), shipBoard.getActivatables().keySet());
        screen = new CliProjectilesScreen(model, controller, state);
    }

    public static void main(String[] args) {
        setUp();
        screen.render();
    }
}