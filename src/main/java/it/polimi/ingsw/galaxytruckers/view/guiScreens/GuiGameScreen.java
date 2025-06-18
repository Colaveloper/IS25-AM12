package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

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
        VBox layout = new VBox(5); // Add spacing between ship and nickname
        layout.setAlignment(Pos.CENTER);

        // Add ship board
        layout.getChildren().add(guiShipBoards.get(shipBoard));

        // Add player nickname below the ship
        Player player = null;
        // Find the player for this ship
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

    // Gets a scaled-down version of a ship for display in the other players quadrants
    protected VBox getScaledShip(ShipBoard shipBoard) {
        VBox layout = new VBox(5); // Add spacing between ship and nickname
        layout.setAlignment(Pos.CENTER);

        // Get the ship board and scale it down
        GuiShipBoard shipBoardView = guiShipBoards.get(shipBoard);
        shipBoardView.setScaleX(0.6); // Scale to 60% of original size
        shipBoardView.setScaleY(0.6); // Scale to 60% of original size

        layout.getChildren().add(shipBoardView);

        // Add player nickname below the ship
        Player player = null;
        // Find the player for this ship
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

    protected HBox getAllShips() {
        HBox layout = new HBox();
        layout.setSpacing(20); // Spacing between main ship and other ships containers
        layout.setPrefHeight(Region.USE_COMPUTED_SIZE);
        layout.setPrefWidth(Region.USE_COMPUTED_SIZE);
        layout.setAlignment(Pos.CENTER); // Center the containers in the available space

        // Main player's ship view
        VBox mainView = getFullShip(model.getMyShip());

        // Set the main view to be slightly wider than the other ships container
        mainView.setPrefWidth(400); // Increased from 300 to 400
        mainView.setMaxWidth(400);  // Increased from 300 to 400

        // Style the main view container with the same styling as rejected components container
        mainView.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" + // Semi-transparent dark background
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +  // Blue-ish border
            "-fx-border-width: 1px;" +                       // Border width
            "-fx-border-radius: 5px;" +                      // Rounded corners for border
            "-fx-background-radius: 5px;" +                  // Rounded corners for background
            "-fx-padding: 10px;"                             // Inner padding
        );

        // Create a container for other players' ships divided into 4 quadrants
        VBox othersColumn = new VBox(10);
        othersColumn.setAlignment(Pos.CENTER);
        othersColumn.setPrefWidth(300);
        othersColumn.setMaxWidth(300);

        // Style the other players container
        othersColumn.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" + // Semi-transparent dark background
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +  // Blue-ish border
            "-fx-border-width: 1px;" +                       // Border width
            "-fx-border-radius: 5px;" +                      // Rounded corners for border
            "-fx-background-radius: 5px;" +                  // Rounded corners for background
            "-fx-padding: 10px;"                             // Inner padding
        );

        // Create a GridPane for 4 quadrants within the othersColumn
        GridPane quadrants = new GridPane();
        quadrants.setHgap(10);
        quadrants.setVgap(10);
        quadrants.setAlignment(Pos.CENTER);

        // Count to keep track of player placement in quadrants
        int playerCount = 0;

        // Place each opponent's ship in a separate quadrant
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            if (!shipBoard.equals(model.getMyShip())) {
                VBox shipView = getScaledShip(shipBoard); // Using scaled version instead of full-sized

                // Calculate row and column for placing in appropriate quadrant
                int col = playerCount % 2;
                int row = playerCount / 2;

                // Add ship to the correct quadrant position
                quadrants.add(shipView, col, row);

                // Set constraints to make each quadrant the same size
                GridPane.setFillWidth(shipView, true);
                GridPane.setFillHeight(shipView, true);

                playerCount++;
            }
        }

        // If we have fewer than 3 opponents (total players can be max 4), add placeholder labels
        while (playerCount < 3) {
            // Create a container for the "No Ship" placeholder
            VBox emptyQuadrant = new VBox();
            emptyQuadrant.setAlignment(Pos.CENTER);

            // Create a label with "[No Ship]" text
            Label noShipLabel = new Label("[No Ship]");
            // Style the label with CSS
            noShipLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: rgba(200, 200, 255, 0.7);" +  // Light blue-ish color
                "-fx-font-style: italic;"
            );

            emptyQuadrant.getChildren().add(noShipLabel);

            // Calculate row and column for placing in appropriate quadrant
            int col = playerCount % 2;
            int row = playerCount / 2;

            // Add empty quadrant to the grid
            quadrants.add(emptyQuadrant, col, row);

            playerCount++;
        }

        // Set column constraints to make columns equal width
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        quadrants.getColumnConstraints().addAll(col1, col2);

        // Set row constraints to make rows equal height
        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        quadrants.getRowConstraints().addAll(row1, row2);

        // Add the quadrants to the othersColumn
        othersColumn.getChildren().add(quadrants);

        layout.getChildren().addAll(mainView, othersColumn);
        return layout;
    }
    protected abstract GuiController getGuiController();

    protected VBox getStyledFlightBoard() {
        VBox container = new VBox(10); // Add spacing between elements
        container.setAlignment(Pos.CENTER);
        container.setPadding(new javafx.geometry.Insets(10));

        // Create and style the FlightBoard label
        Label flightBoardLabel = new Label("FlightBoard");
        flightBoardLabel.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        flightBoardLabel.setAlignment(Pos.CENTER);

        // Style the flight board container with the same styling as other containers
        container.setStyle(
            "-fx-background-color: rgba(20, 20, 40, 0.7);" + // Semi-transparent dark background
            "-fx-border-color: rgba(100, 100, 200, 0.8);" +  // Blue-ish border
            "-fx-border-width: 1px;" +                       // Border width
            "-fx-border-radius: 5px;" +                      // Rounded corners for border
            "-fx-background-radius: 5px;" +                  // Rounded corners for background
            "-fx-padding: 10px;"                             // Inner padding
        );

        // Add label and flightboard to container
        container.getChildren().addAll(flightBoardLabel, guiFlightBoard);
        return container;
    }
}
