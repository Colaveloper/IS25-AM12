package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuiShipPieceChoiceScreen extends GuiScreen {
    public GuiShipPieceChoiceScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) throws IOException {

    }
}
