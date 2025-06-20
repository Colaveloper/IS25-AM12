package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import org.junit.jupiter.api.BeforeEach;

class GuiShipBuildingScreenTest {

    GuiSecondShipBuildingScreen screen;
    ClientModel model = new ClientModel();
    Player player1 = new Player("player1");
//    Player player2 = new Player("player2");
//    Player player3 = new Player("player3");
//    Player player4 = new Player("player4");

    @BeforeEach
    void setUp() {

        model.createGame(Level.SECOND);
        model.addPlayer(player1, GameColor.BLUE);
//        model.addPlayer(player2, GameColor.RED);
//        model.addPlayer(player3, GameColor.GREEN);
//        model.addPlayer(player4, GameColor.YELLOW);
        model.setMetaState(MetaState.INGAME);

        SecondShipBuildingState state = new SecondShipBuildingState();
        model.notifyCurrentState(state);

        screen = new GuiSecondShipBuildingScreen(model, null, state);
    }

//    @Test
//    void notifyRequestRandComponent() {

//        screen.render();
//        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
//                List.of(Connector.NONE,Connector.NONE,Connector.NONE,Connector.NONE),
//                10
//        ));
//        screen.render();
//    }
}