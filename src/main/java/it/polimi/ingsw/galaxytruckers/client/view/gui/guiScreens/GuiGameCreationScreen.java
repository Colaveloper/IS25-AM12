package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;

/**
 * GUI screen for creating a new game.
 * This screen allows the user to select game settings such as flight level
 * and the number of players before starting a new game session.
 */
public class GuiGameCreationScreen extends GuiScreen {

    /**
     * Constructs a new game creation screen.
     *
     * @param model      The client model containing game state
     * @param controller The controller for communicating with the server
     */
    public GuiGameCreationScreen(ClientModel model, ClientControllerInterface controller) {
        super(model, controller);
    }

    /**
     * {@inheritDoc}
     * Creates and returns the game creation interface with level selection,
     * player count selection, and a start button.
     *
     * @return A styled Pane containing the game creation user interface
     */
    @Override
    public Pane getNode() {
        VBox layout = new VBox(20);
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setMaxHeight(Region.USE_PREF_SIZE);
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
        levelComboBox.getItems().addAll(
            Arrays.stream(Level.values())
                  .filter(level -> level != Level.FIRST)
                  .toArray(Level[]::new)
        );
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
                    createButton.setDisable(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        layout.getChildren().addAll(instructionLabel, levelComboBox, playersComboBox, createButton);
        return layout;
    }
}
