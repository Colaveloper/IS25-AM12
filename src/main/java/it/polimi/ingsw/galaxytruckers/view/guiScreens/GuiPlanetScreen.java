package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiPlanetScreen extends GuiGameScreen {
    public GuiPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState choosePlanetState) {
        super(model, controller, choosePlanetState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiPlanetScreen");
    }
}
