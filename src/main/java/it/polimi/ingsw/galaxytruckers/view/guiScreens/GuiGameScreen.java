package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.PurpleContainer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

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
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(guiShipBoards.get(shipBoard));

        Player player = null;
        for (Map.Entry<ShipBoard, Player> entry : model.getShipToPlayer().entrySet()) {
            if (entry.getKey().equals(shipBoard)) {
                player = entry.getValue();
                break;
            }
        }

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

        Player player = null;
        for (Map.Entry<ShipBoard, Player> entry : model.getShipToPlayer().entrySet()) {
            if (entry.getKey().equals(shipBoard)) {
                player = entry.getValue();
                break;
            }
        }

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

        mainView.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" +
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;"
        );

        VBox othersColumn = new VBox(10);
        othersColumn.setAlignment(Pos.CENTER);
        othersColumn.setPrefWidth(300);
        othersColumn.setMaxWidth(300);

        othersColumn.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" +
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;" +
            "-fx-padding: 10px;"
        );


        GridPane quadrants = new GridPane();
        quadrants.setHgap(10);
        quadrants.setVgap(10);
        quadrants.setAlignment(Pos.CENTER);


        int playerCount = 0;


        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            if (!shipBoard.equals(model.getMyShip())) {
                VBox shipView = getScaledShip(shipBoard);
                int col = playerCount % 2;
                int row = playerCount / 2;

                quadrants.add(shipView, col, row);

                GridPane.setFillWidth(shipView, true);
                GridPane.setFillHeight(shipView, true);

                playerCount++;
            }
        }

        while (playerCount < 3) {
            VBox emptyQuadrant = new VBox();
            emptyQuadrant.setAlignment(Pos.CENTER);

            Label noShipLabel = new Label("[No Ship]");
            noShipLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: rgba(200, 200, 255, 0.7);" +
                "-fx-font-style: italic;"
            );

            emptyQuadrant.getChildren().add(noShipLabel);

            int col = playerCount % 2;
            int row = playerCount / 2;

            quadrants.add(emptyQuadrant, col, row);

            playerCount++;
        }

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        quadrants.getColumnConstraints().addAll(col1, col2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        quadrants.getRowConstraints().addAll(row1, row2);

        othersColumn.getChildren().add(quadrants);

        layout.getChildren().addAll(mainView, othersColumn);
        return layout;
    }
    protected abstract GuiController getGuiController();

    // styled flight board with label and container
    protected VBox getStyledFlightBoard() {
        PurpleContainer container = new PurpleContainer(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(10));

        Label flightBoardLabel = new Label("FlightBoard");
        flightBoardLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        flightBoardLabel.setAlignment(Pos.CENTER);

        container.getChildren().addAll(flightBoardLabel, guiFlightBoard);
        return container;
    }
}

