package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public abstract class View {

    protected final ScreenFactory screenFactory;
    protected final ClientModel model;
    protected final ControllerToServer controller;

    public View(ControllerToServer controller, ClientModel model) {
        this.screenFactory = new ScreenFactory();
        this.controller = controller;
        this.model = model;
    }

    //todo generic method for creating screen for cli and gui
    public abstract void setScreen(GameState gameState);
}
