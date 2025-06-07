package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;
import javafx.scene.Parent;

public class GuiCrewInitializationScreen extends GuiScreen {
    public GuiCrewInitializationScreen(ClientModel model, ControllerToServer controller, ShipInitializationState shipInitializationState) {
        super(model, controller, shipInitializationState);
    }

    @Override
    public Parent getNode() {
        return null;
    }
}
