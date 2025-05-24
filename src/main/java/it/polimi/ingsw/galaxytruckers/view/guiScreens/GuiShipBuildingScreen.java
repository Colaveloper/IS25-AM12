package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAllShips;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiComponentBank;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class GuiShipBuildingScreen extends GuiScreen {
    public GuiShipBuildingScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) throws IOException {
        root.getChildren().add(new GuiComponentBank(model, controller).getNode());
        root.getChildren().add(new GuiFlightBoard(model, controller).getNode());
        root.getChildren().add(new GuiAllShips(model, controller).getNode());
    }
}
