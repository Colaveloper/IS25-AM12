package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import javafx.scene.Parent;

public class GuiNewCardScreen extends GuiScreen {
    public GuiNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState drawCardState) {
        super(model, controller, drawCardState);
    }

    @Override
    public Parent getNode() {
        return null;
    }
}
