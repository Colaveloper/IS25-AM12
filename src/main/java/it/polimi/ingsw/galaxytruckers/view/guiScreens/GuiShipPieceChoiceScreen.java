package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GuiShipPieceChoiceScreen extends GuiAdventureScreen {
    public GuiShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }
}
