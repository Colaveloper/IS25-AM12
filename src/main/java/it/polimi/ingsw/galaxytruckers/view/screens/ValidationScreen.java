package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliValidationScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiValidationScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public class ValidationScreen implements ScreenFactory {

    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiValidationScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) {
        return new CliValidationScreen(model, controller);
    }
}
