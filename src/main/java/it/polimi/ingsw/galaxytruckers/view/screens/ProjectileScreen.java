package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliProjectilesScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;

public class ProjectileScreen implements ScreenFactory{
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return null;
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException {
        return new CliProjectilesScreen(model, controller);
    }
}
