package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ActivateState;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GuiDeclareEnginePowerScreen extends GuiActivationScreen {
    public GuiDeclareEnginePowerScreen(ClientModel model, ControllerToServer controller, ActivateState state) {
        super(model, controller, state);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.getChildren().add(new Label("GuiDeclareEnginePowerScreen"));
        layout.getChildren().add(getGuiAllShips());
        return layout;
    }
}
