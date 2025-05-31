package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAllShips;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiScreen extends Screen {
    protected final ClientModel model;
    protected final ControllerToServer controller;
    protected final GameState state;

    public GuiScreen(ClientModel model, ControllerToServer controller, GameState state) {
        this.model = model;
        this.controller = controller;
        this.state = state;
 }

    public GuiScreen(ClientModel model, ControllerToServer controller) {
        this(model, controller, null);
    }

    public abstract Parent getNode();
}

// todo: make a utility method for common background
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        // Load background image
//        Image backgroundImage = new Image("file:src/main/resources/textures/background.png"); // adjust path if needed
//        ImageView backgroundView = new ImageView(backgroundImage);
//        backgroundView.setFitWidth(800);
//        backgroundView.setFitHeight(600);
//        backgroundView.setPreserveRatio(false);
//
//        // Set up root content layer
//        root = new VBox();
//        root.setPadding(new Insets(50));
//        root.setSpacing(50);
//
//        // StackPane to layer background + root content
//        StackPane layeredRoot = new StackPane();
//        layeredRoot.getChildren().addAll(backgroundView, root);
//
//        Scene scene = new Scene(layeredRoot, 800, 600);
//        stage.setScene(scene);
//        stage.setTitle("Galaxy Trucker");
//        stage.show();
//    } 