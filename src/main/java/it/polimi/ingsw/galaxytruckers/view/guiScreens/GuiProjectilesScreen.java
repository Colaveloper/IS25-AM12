package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiProjectilesScreen extends GuiActivationScreen {
    public GuiProjectilesScreen(ClientModel model, ControllerToServer controller, HandleProjectileState handleProjectileState) {
        super(model, controller, handleProjectileState);
    }


    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiProjectilesScreen");
    }
}
