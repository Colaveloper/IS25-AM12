package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DeclareFirePowerState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiDeclareFirePowerScreen extends GuiActivationScreen {
    public GuiDeclareFirePowerScreen(ClientModel model, ControllerToServer controller, DeclareFirePowerState declareFirePowerState) {
        super(model, controller, declareFirePowerState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiDeclareFirePowerScreen");
    }
}
