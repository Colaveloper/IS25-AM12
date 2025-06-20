package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.layout.Pane;

import java.awt.*;

public abstract class GuiScreen extends Screen {
    protected final ClientModel model;
    protected final ControllerToServer controller;
    protected final GameState state;
    protected ShipBoard myShipBoard;



    public GuiScreen(ClientModel model, ControllerToServer controller, GameState state) {
        this.model = model;
        this.controller = controller;
        this.state = state;
        if (state != null) this.myShipBoard = model.getMyShip();

 }

    public GuiScreen(ClientModel model, ControllerToServer controller) {
        this(model, controller, null);
    }

    public abstract Pane getNode();
}

