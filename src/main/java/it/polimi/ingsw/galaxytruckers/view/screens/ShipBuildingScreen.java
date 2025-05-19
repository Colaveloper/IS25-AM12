package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.*;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;

public class ShipBuildingScreen extends ScreenFactory {
    @Override
    public GuiScreen getGuiScreen(ClientModel model, ClientController controller) {
        return new GuiShipBuildingScreen(model, controller);
    }

    @Override
    public CliScreen getCliScreen(ClientModel model, ClientController controller) throws IOException {
        return new CliShipBuildingScreen(model, controller);
    }
}
