package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class GuiShipPieceChoiceScreen extends GuiAdventureScreen {
    public GuiShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Parent getNode() {
        return new Label("GuiShipPieceChoiceScreen");
    }
}
