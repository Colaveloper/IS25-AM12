package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.ModelObserver;

public abstract class View {

    protected final ScreenFactory screenFactory;
    protected final ClientModel model;
    protected final ControllerToServer controller;

    public View(ControllerToServer controller, ClientModel model) {
        this.screenFactory = new ScreenFactory();
        this.controller = controller;
        this.model = model;
        model.getMetaState().addObserver((_)->updateScreen());
//        model.getGameProperty().addObserver(game -> {
//            if (game != null) {
//                game.getStateProperty().addObserver((_)->updateScreen());
//            }
//        });
    }


    //todo generic method for creating screen for cli and gui
    public abstract void updateScreen();

    public abstract void start();

//    public abstract void updateScreen(GameState gameState);
}
