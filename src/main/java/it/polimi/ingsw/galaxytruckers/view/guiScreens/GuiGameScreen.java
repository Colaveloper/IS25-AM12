package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAllShips;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GuiGameScreen extends GuiScreen implements PointPressHandler {

    protected GuiFlightBoard guiFlightBoard;
    protected GuiAllShips guiAllShips;
    private GameState gameState;

    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);
        gameState = state;
        this.guiAllShips = new GuiAllShips(model.getClientPlayer(), model.getShipToPlayer(), controller, this);
        this.guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), controller);
    }

    @Override
    public abstract void handlePointPress(Point point);

    // TODO: remove this monstrosity
    protected void parseComponentPress(Point point) {
        if (!model.getGame().getGivenUpShips().contains(model.getClientPlayer().getShipBoard())) {
            List<StateActions> availableActions = new ArrayList<>(state.getAvailableActions());
            if (availableActions.contains(StateActions.ACTIVATE_COMPONENT)) {
                controller.activateComponent(point);
            } else if (availableActions.contains(StateActions.SPEND_BATTERIES)) {
                controller.useBattery(point);
            } else if (availableActions.contains(StateActions.LOSE_CREW)) {
                controller.loseCrew(point);
            } else if (availableActions.contains(StateActions.LOSE_GOOD)) {
                controller.loseGoods(point);
            } else if (availableActions.contains(StateActions.REMOVE_GOOD)) {
//                controller.removeGoods();
            } else if (availableActions.contains(StateActions.ADD_GOOD)) {
//                controller.placeGoods();
            } else if (availableActions.contains(StateActions.PLACE_COMPONENT)) {
                controller.placeComponent(point, Direction.UP); // TODO: make dynamic
            } else if (availableActions.contains(StateActions.REMOVE_COMPONENT)) {
                controller.removeComponent(point);
            } else if (availableActions.contains(StateActions.INITIALIZE_CABIN)) {
//                controller.initializeCabin();
            }
        }
    }
}
