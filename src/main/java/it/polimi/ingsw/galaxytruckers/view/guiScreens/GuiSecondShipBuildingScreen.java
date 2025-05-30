package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class GuiSecondShipBuildingScreen extends GuiScreen {
    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        super(model, controller, gameState);
    }

    @Override
    public Parent getNode() {
        VBox root = new VBox();
//        root.getChildren().add(new GuiComponentBank(model, controller).getNode());
        root.getChildren().add(new GuiFlightBoard(model.getGame().getFlightBoard(), controller));
//        root.getChildren().add(new GuiAllShips(model, controller).getNode());
        return root;
    }
}