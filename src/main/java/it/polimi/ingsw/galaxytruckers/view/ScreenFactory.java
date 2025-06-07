package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.*;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.*;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

public interface ScreenFactory<S extends Screen> {
    S createScreen(MetaState metaState, ClientModel model, ControllerToServer controller);
    S createScreen(GameState gameState, ClientModel model, ControllerToServer controller);
}

