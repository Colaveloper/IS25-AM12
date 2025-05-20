package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;

// TODO: turn into interface
public abstract class ScreenFactory {
    public abstract GuiScreen getGuiScreen(ClientModel model, ClientController controller);
    public abstract CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException;
}
