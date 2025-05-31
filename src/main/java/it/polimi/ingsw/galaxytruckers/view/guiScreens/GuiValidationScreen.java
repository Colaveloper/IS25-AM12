package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuiValidationScreen extends GuiScreen {
    public GuiValidationScreen(ClientModel model, ClientController controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public void attachContentToRoot(VBox root) throws IOException {

    }
}
