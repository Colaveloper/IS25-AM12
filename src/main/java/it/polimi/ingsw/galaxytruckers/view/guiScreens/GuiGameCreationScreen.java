package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GuiGameCreationScreen extends GuiScreen {
    public GuiGameCreationScreen(ClientModel model, ClientController controller) {
        super(model, controller);
    }

    @Override
    public void attachContentToRoot(VBox root) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        Label instructionLabel = new Label("Create a New Game");
        instructionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ComboBox<Level> levelComboBox = new ComboBox<>();
        levelComboBox.getItems().addAll(Level.values());
        levelComboBox.setPromptText("Select a Level");

        ComboBox<Integer> playersComboBox = new ComboBox<>();
        playersComboBox.getItems().addAll(2, 3, 4);
        playersComboBox.setPromptText("Select number of players");

        Button createButton = new Button("Create Game");
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
        root.getChildren().add(layout);
    }
}
