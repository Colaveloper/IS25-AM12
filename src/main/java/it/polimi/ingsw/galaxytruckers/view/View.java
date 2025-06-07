package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public abstract class View<S extends Screen> implements ModelObserver {
    protected final ClientModel model;
    protected final ClientController controller;
    protected final ScreenFactory<S> screenFactory;
//    protected Screen currentScreen;

    protected View(ClientModel model, ClientController controller, ScreenFactory<S> screenFactory) {
        this.model = model;
        this.controller = controller;
        this.screenFactory = screenFactory;

        model.addObserver(this);
    }
}
