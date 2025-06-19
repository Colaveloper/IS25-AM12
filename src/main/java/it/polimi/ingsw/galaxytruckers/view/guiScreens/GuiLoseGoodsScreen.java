package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiLoseGoodsScreen extends GuiGameScreen {
    public GuiLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState removeGoodsState) {
        super(model, controller, removeGoodsState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiLoseGoodsScreen");
    }
}
