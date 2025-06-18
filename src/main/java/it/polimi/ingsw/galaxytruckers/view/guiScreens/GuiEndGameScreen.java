package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Parent;

public class GuiEndGameScreen extends GuiScreen {
    public GuiEndGameScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public Parent getNode() {
        return null;
    }
}
