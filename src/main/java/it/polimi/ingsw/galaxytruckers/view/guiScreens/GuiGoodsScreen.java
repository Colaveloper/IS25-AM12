package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.AddGoodsState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiGoodsScreen extends GuiGameScreen {
    public GuiGoodsScreen(ClientModel model, ControllerToServer controller, AddGoodsState addGoodsState) {
        super(model, controller, addGoodsState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiGoodsScreen");
    }
}
