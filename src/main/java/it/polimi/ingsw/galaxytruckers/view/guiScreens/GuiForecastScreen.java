package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.VBox;

public class GuiForecastScreen extends GuiScreen {
    public GuiForecastScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) {

    }
}
