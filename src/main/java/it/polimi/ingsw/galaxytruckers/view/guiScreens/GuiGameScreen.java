package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiGameScreen extends GuiScreen {

    protected final GuiFlightBoard guiFlightBoard;
    protected final Map<ShipBoard, GuiShipBoard> guiShipBoards; // static?

    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);
        this.guiShipBoards = new HashMap<>();
        guiShipBoards.put(model.getMyShip(), new GuiShipBoard(model.getMyShip(), getGuiController()));
        for (ShipBoard s : model.getGame().getShipBoards()) {
            if (!s.equals(model.getMyShip())) {
                guiShipBoards.put(s, new GuiShipBoard(s, new GuiController() {}));
            }
        };
        this.guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), getGuiController());
    }

    // overridden for different levels and game-phases
    protected VBox getFullShip(ShipBoard shipBoard) {
        VBox layout = new VBox();
        layout.getChildren().add(guiShipBoards.get(shipBoard));
        return layout;
    }

    protected HBox getAllShips() {
        HBox layout = new HBox();
        layout.setSpacing(20);
        layout.setPrefHeight(Region.USE_COMPUTED_SIZE);
        layout.setPrefWidth(Region.USE_COMPUTED_SIZE);

        // Main player's large ship view
        VBox mainView = getFullShip(model.getMyShip());
        mainView.setMaxWidth(Double.MAX_VALUE);
        mainView.setPrefWidth(2 * 300);
        HBox.setHgrow(mainView, Priority.ALWAYS); // todo remove ?

        // VBox for other players
        VBox othersColumn = new VBox(10);
        othersColumn.setAlignment(Pos.CENTER);
        othersColumn.setPrefWidth(300);
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            if (!shipBoard.equals(model.getMyShip())) {
                VBox shipView = getFullShip(shipBoard);
                othersColumn.getChildren().add(shipView);
            }
        }

        layout.getChildren().addAll(mainView, othersColumn);
        return layout;
    }
    protected abstract GuiController getGuiController();
}
