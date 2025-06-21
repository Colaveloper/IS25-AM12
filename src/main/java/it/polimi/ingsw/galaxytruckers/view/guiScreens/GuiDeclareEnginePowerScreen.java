package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;
import javafx.scene.control.Label;

public class GuiDeclareEnginePowerScreen extends GuiActivationScreen {
    public GuiDeclareEnginePowerScreen(ClientModel model, ControllerToServer controller, ActivateState state) {
        super(model, controller, state);
        if (isMyTurn()) {
            guiLog.getChildren().setAll(new Label("Select an double-engine to activate or a battery to use"));
        }
    }
}
