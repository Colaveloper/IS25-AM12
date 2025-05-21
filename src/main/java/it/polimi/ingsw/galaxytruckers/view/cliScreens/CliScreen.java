package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public abstract class CliScreen extends CliElement {
    ControllerToServer controller;

    public CliScreen(ClientModel model, ControllerToServer controller) {
        super(model);
        this.controller = controller;
    }

    public abstract boolean isLegalInput(String input);
    public abstract void parseAndInvoke(String input) ;
}
