package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;
import javafx.scene.Parent;

public class GuiDeclareEnginePowerScreen extends GuiActivationScreen{
    public GuiDeclareEnginePowerScreen(ClientModel model, ControllerToServer controller, ActivateState state) {
        super(model, controller, state);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return null;
    }
}
