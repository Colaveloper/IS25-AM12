package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Map;

public class GuiPlanetScreen extends GuiAdventureScreen {
    private final ChoosePlanetState choosePlanetState;
    private HBox layout;

    public GuiPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState choosePlanetState) {
        super(model, controller, choosePlanetState);
        this.choosePlanetState = choosePlanetState;
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }

    @Override
    public Pane getNode() {
        layout = new HBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);

        // title
        Label title = new Label(choosePlanetState.isMyTurn() ? "Your turn to choose a planet" : "Waiting for other player");
        title.setTextFill(Color.WHITE);
        content.getChildren().add(title);

        // planet buttons
        HBox planets = new HBox(10);
        planets.setAlignment(Pos.CENTER);

        ShipBoard[] options = choosePlanetState.getOptions();
        for (int i = 0; i < options.length; i++) {
            Button btn = createPlanetButton(options[i], i);
            planets.getChildren().add(btn);
        }
        content.getChildren().add(planets);

        // skip button
        if (choosePlanetState.isMyTurn()) {
            Button skipBtn = new Button("Skip");
            skipBtn.setOnAction(e -> controller.goNext());
            content.getChildren().add(skipBtn);
        }

        layout.getChildren().add(content);
        return layout;
    }

    private Button createPlanetButton(ShipBoard planetData, int index) {
        Button btn = new Button();
        boolean taken = planetData != null;

        if (taken) {
            String playerName = "A player";
            for (Map.Entry<ShipBoard, Player> entry : model.getShipToPlayer().entrySet()) {
                if (entry.getKey().equals(planetData)) {
                    playerName = entry.getValue().getNickname();
                    break;
                }
            }
            btn.setText("Planet " + (index + 1) + "\n" + playerName + " landed here");
            btn.setDisable(true);
            btn.setStyle("-fx-text-fill: white;");
        } else {
            btn.setText("Planet " + (index + 1));
            btn.setDisable(!choosePlanetState.isMyTurn());
            btn.setOnAction(e -> {
                if (choosePlanetState.isMyTurn()) {
                    controller.choosePlanet(index);
                }
            });
        }

        return btn;
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        Platform.runLater(() -> {
            choosePlanetState.notifyChoosePlanet(shipBoard, choice, nextShipBoard);
            updateUI();
        });
    }

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        Platform.runLater(() -> {
            choosePlanetState.notifyCurrentPlayerUpdate(shipBoard);
            updateUI();
        });
    }

    private void updateUI() {
        if (layout != null) {
            layout.getChildren().clear();
            layout.getChildren().add(getNode().getChildren().getFirst());
        }
    }
}
