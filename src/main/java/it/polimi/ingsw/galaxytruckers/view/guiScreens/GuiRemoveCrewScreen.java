package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveCrewState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiRemoveCrewScreen extends GuiGameScreen {
    public GuiRemoveCrewScreen(ClientModel model, ControllerToServer controller, RemoveCrewState removeCrewState) {
        super(model, controller, removeCrewState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiRemoveCrewScreen");
    }
}
