package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;

public interface ScreenFactory {
     GuiScreen getGuiScreen(ClientModel model, ClientController controller);
     CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException;
}
