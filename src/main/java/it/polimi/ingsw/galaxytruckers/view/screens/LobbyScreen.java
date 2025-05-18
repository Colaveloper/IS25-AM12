package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliLobbyScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiLobbyScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public class LobbyScreen extends ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiLobbyScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) {
        return new CliLobbyScreen(model, controller);
    }
}
