package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliGameCreationScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiGameCreationScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public class GameCreationScreen extends ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiGameCreationScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) {
        return new CliGameCreationScreen(model, controller);
    }
}
