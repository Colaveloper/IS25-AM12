package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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

    public abstract Parent getNode();
}

