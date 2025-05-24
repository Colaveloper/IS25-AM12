package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliShipPieceChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipPieceChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

public class ShipPieceChoiceScreen implements ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiShipPieceChoiceScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) {
        return new CliShipPieceChoiceScreen(model, controller);
    }
}
