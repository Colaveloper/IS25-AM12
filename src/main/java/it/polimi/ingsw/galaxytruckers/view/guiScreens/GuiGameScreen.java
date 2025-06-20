package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.PurpleContainer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiGameScreen extends GuiScreen {
    protected final GuiFlightBoard guiFlightBoard;
    protected final Map<ShipBoard, GuiShipBoard> guiShipBoards;
    private static final String CONTAINER_STYLE =
        "-fx-background-color: rgba(20, 20, 40, 0.7);" +
        "-fx-border-color: rgba(100, 100, 200, 0.8);" +
        "-fx-border-width: 1px;" +
        "-fx-border-radius: 5px;" +
        "-fx-background-radius: 5px;" +
        "-fx-padding: 10px;";
    protected final PurpleContainer guiContextBox;

    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);
        this.guiShipBoards = new HashMap<>();
        guiShipBoards.put(model.getMyShip(), new GuiShipBoard(model.getMyShip(), getGuiController()));
        for (ShipBoard s : model.getGame().getShipBoards()) {
            if (!s.equals(model.getMyShip())) {
                guiShipBoards.put(s, new GuiShipBoard(s, new GuiController() {}));
            }
        }
        this.guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), getGuiController());
        guiContextBox = new PurpleContainer();
        guiContextBox.setAlignment(Pos.CENTER);
    }

    public Pane getNode() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                guiContextBox,
                getGuiFlightBoard(),
                getGuiAllShips()
        );
        return layout;
    }

    protected VBox getFullShip(ShipBoard shipBoard) {
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);

        GuiShipBoard shipBoardView = guiShipBoards.get(shipBoard);
        layout.getChildren().add(shipBoardView);

        Player player = model.getShipToPlayer().entrySet().stream()
                .filter(entry -> entry.getKey().equals(shipBoard))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (player != null) {
            Label nicknameLabel = new Label(player.getNickname());
            nicknameLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
            );
            layout.getChildren().add(nicknameLabel);
        }

        return layout;
    }

    protected VBox getScaledShip(ShipBoard shipBoard) {
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);

        GuiShipBoard shipBoardView = guiShipBoards.get(shipBoard);
        shipBoardView.setScaleX(0.6);
        shipBoardView.setScaleY(0.6);

        layout.getChildren().add(shipBoardView);

        Player player = model.getShipToPlayer().entrySet().stream()
                .filter(entry -> entry.getKey().equals(shipBoard))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);

        if (player != null) {
            Label nicknameLabel = new Label(player.getNickname());
            nicknameLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
            );
            layout.getChildren().add(nicknameLabel);
        }

        return layout;
    }

    protected HBox getGuiAllShips() {
        // main ship and other ships in separate containers
        // other ships organized in a grid layout
        // each ship has a label with the player's nickname
        HBox layout = new HBox();
        layout.setSpacing(20);
        layout.setPrefHeight(Region.USE_COMPUTED_SIZE);
        layout.setPrefWidth(Region.USE_COMPUTED_SIZE);
        layout.setAlignment(Pos.CENTER);

        VBox mainView = getFullShip(model.getMyShip());
        mainView.setPrefWidth(400);
        mainView.setMaxWidth(400);
        mainView.setStyle(CONTAINER_STYLE);

        VBox othersColumn = new VBox(10);
        othersColumn.setAlignment(Pos.CENTER);
        othersColumn.setPrefWidth(200);
        othersColumn.setMaxWidth(200);
        othersColumn.setStyle(CONTAINER_STYLE);

        GridPane shipGrid = new GridPane();
        shipGrid.setHgap(10);
        shipGrid.setVgap(10);
        shipGrid.setAlignment(Pos.CENTER);

        for (int i = 0; i < 2; i++) {
            ColumnConstraints colConstraint = new ColumnConstraints();
            colConstraint.setPercentWidth(50);
            shipGrid.getColumnConstraints().add(colConstraint);

            RowConstraints rowConstraint = new RowConstraints();
            rowConstraint.setPercentHeight(50);
            shipGrid.getRowConstraints().add(rowConstraint);
        }

        int index = 0;
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            if (!shipBoard.equals(model.getMyShip())) {
                VBox shipView = getScaledShip(shipBoard);
                shipGrid.add(shipView, index % 2, index / 2);
                index++;
            }
        }

        othersColumn.getChildren().add(shipGrid);
        layout.getChildren().addAll(mainView, othersColumn);
        return layout;
    }

    // styled flight board with label and container
    protected VBox getGuiFlightBoard() {
        PurpleContainer container = new PurpleContainer(10);
        container.setAlignment(Pos.CENTER);
        container.setMaxHeight(30);
        container.setMaxWidth(300);

        Label flightBoardLabel = new Label("FlightBoard");
        flightBoardLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );

        container.getChildren().addAll(flightBoardLabel, guiFlightBoard);
        return container;
    }

    protected abstract GuiController getGuiController();

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
    }
}