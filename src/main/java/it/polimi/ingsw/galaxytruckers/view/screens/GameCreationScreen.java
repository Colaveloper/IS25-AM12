package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;

public class GameCreationScreen implements ScreenStrategy {
    @Override
    public void showCLI(ClientModel model) {
        System.out.println("No one is currently creating a new game, so... it's up to you!");
        System.out.println("Choose a Level and the number of players");
        System.out.print("Available levels: ");
        System.out.println(String.join(", ", Arrays.stream(Level.values())
                .map(Enum::name)
                .toArray(String[]::new)));
        System.out.println("The game is available for 2, 3, or 4 players");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        for (Level level : Level.values()) {
            if (input.matches("^" + level.name() + "\\s[2-4]$")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        String[] parts = input.split("\\s");
        server.newGame(Level.valueOf(parts[0].toUpperCase()), Integer.parseInt(parts[1]));
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
        root.getChildren().clear();

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
                    server.newGame(selectedLevel, selectedPlayers);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        layout.getChildren().addAll(instructionLabel, levelComboBox, playersComboBox, createButton);
        root.getChildren().add(layout);
    }
}
