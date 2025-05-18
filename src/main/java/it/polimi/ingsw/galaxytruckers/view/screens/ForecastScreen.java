package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.*;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiForecastScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiGameCreationScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;

public class ForecastScreen extends ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiForecastScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException {
        return new CliForecastScreen(model, controller);
    }
}
