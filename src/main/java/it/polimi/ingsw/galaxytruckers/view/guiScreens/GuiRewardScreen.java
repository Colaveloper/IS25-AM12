package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GuiRewardScreen extends GuiGameScreen {
    public GuiRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState grabRewardState) {
        super(model, controller, grabRewardState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }
}
