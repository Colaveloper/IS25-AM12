package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliNicknameChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiNicknameChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public class NicknameChoiceScreen extends ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiNicknameChoiceScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) {
        return new CliNicknameChoiceScreen(model, controller);
    }
}
