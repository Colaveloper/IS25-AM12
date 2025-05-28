package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CliSecondShipBuildingScreenTest {

    CliSecondShipBuildingScreen screen;
    ClientModel model = new ClientModel();

    @BeforeEach
    void setUp() {

        model.createGame(Level.SECOND, 4);
        model.addPlayer(new Player("player1"), GameColor.BLUE);
        model.addPlayer(new Player("player2"), GameColor.RED);
        model.addPlayer(new Player("player3"), GameColor.GREEN);
        model.addPlayer(new Player("player4"), GameColor.YELLOW);
        model.setMetaState(MetaState.INGAME);

        SecondShipBuildingState state = new SecondShipBuildingState();
        model.notifyCurrentState(state);

        screen = new CliSecondShipBuildingScreen(model, null, state);
    }

    @Test
    void notifyRequestRandComponent() {
        screen.render();
//        model.notifyRequestRandComponent();
    }
}