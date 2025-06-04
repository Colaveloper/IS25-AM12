package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CliSecondShipBuildingScreenTest {

    CliSecondShipBuildingScreen screen;
    ClientModel model = new ClientModel();
    Player player1 = new Player("player1");
//    Player player2 = new Player("player2");
//    Player player3 = new Player("player3");
//    Player player4 = new Player("player4");

    @BeforeEach
    void setUp() {

        model.createGame(Level.SECOND, 4);
        model.setPlayer(player1);
        model.addPlayer(player1, GameColor.BLUE);
//        model.addPlayer(player2, GameColor.RED);
//        model.addPlayer(player3, GameColor.GREEN);
//        model.addPlayer(player4, GameColor.YELLOW);

        SecondShipBuildingState state = new SecondShipBuildingState();
        model.setMetaState(MetaState.INGAME);
        model.notifyCurrentState(state);

        screen = new CliSecondShipBuildingScreen(model, null, state);
    }

    @Test
    void notifyRequestRandComponent() {
        screen.render();
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                10
        ));
        screen.render();
    }

    @Test
    void notifyRejectComponent() {
        screen.render();
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                10
        ));
        model.notifyRejectComponent(player1.getShipBoard());
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                10
        ));
        model.notifyRejectComponent(player1.getShipBoard());
        screen.render();
    }


    @Test
    void notifyStashComponent() {
        screen.render();
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                10
        ));
        model.notifyStashComponent(player1.getShipBoard());
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                70
        ));
        model.notifyStashComponent(player1.getShipBoard());
        screen.render();
    }

    @Test
    void notifyPlaceComponent() {
        screen.render();
        model.notifyRequestRandComponent(player1.getShipBoard(), new Component(
                Map.of(
                        Direction.RIGHT, Connector.NONE,
                        Direction.DOWN, Connector.NONE,
                        Direction.LEFT, Connector.NONE,
                        Direction.UP, Connector.NONE
                ),
                10
        ));
        model.notifyPlaceComponent(player1.getShipBoard(), new Point(7, 7), Direction.LEFT);
        screen.render();
    }

    @Test
    void notifyPlaceOnFlightBoard() {
        screen.render();
        model.notifyFlightBoardPosition(player1.getShipBoard(), 1);
        screen.render();
    }
}