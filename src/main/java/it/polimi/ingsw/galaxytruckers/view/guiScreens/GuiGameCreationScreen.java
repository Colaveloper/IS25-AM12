package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.screens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GuiGameCreationScreen extends GuiScreen {
    public GuiGameCreationScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(400);
        layout.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        Label instructionLabel = new Label("New Game");
        instructionLabel.setFont(new Font("Arial", 22));
        instructionLabel.setTextFill(Color.LIGHTSKYBLUE);
        instructionLabel.setEffect(new DropShadow(8, Color.DARKSLATEBLUE));

        String comboBoxStyle = "-fx-background-color: rgba(255,255,255,0.87); " +
                "-fx-border-color: rgba(90,155,212,0.73); " +
                "-fx-border-radius: 6; " +
                "-fx-font-size: 14px;";

        ComboBox<Level> levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll(Level.values());
        levelComboBox.setPromptText("Select Level");
        levelComboBox.setMaxWidth(Double.MAX_VALUE);
        levelComboBox.setStyle(comboBoxStyle);

        ComboBox<Integer> playersComboBox = new ComboBox<>();
        playersComboBox.getItems().addAll(2, 3, 4);
        playersComboBox.setPromptText("Number of Players");
        playersComboBox.setMaxWidth(Double.MAX_VALUE);
        playersComboBox.setStyle(comboBoxStyle);

        Button createButton = new Button("Start");
        createButton.setFont(new Font(18));
        createButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setStyle(
                "-fx-background-color: rgba(80, 0, 200, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );
        createButton.setOnAction(e -> {
            Level selectedLevel = levelComboBox.getValue();
            Integer selectedPlayers = playersComboBox.getValue();
            if (selectedLevel != null && selectedPlayers != null) {
                try {
                    controller.requestNewGame(selectedLevel, selectedPlayers);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        layout.getChildren().addAll(instructionLabel, levelComboBox, playersComboBox, createButton);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(layout);
    }
}
