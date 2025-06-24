package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class GuiEndGameScreen extends GuiScreen {

    public GuiEndGameScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public Pane getNode() {
        Label title = new Label("GAME OVER");
        title.setFont(new Font("Arial", 30));
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTextFill(Color.LIGHTSKYBLUE);
        title.setEffect(new DropShadow(10, Color.DARKSLATEBLUE));

        VBox tableContainer = new VBox(0);
        tableContainer.setStyle("-fx-background-color: rgba(10, 20, 50, 0.7); -fx-background-radius: 8;");
        tableContainer.setPadding(new Insets(10));
        tableContainer.setMaxWidth(400);

        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setSpacing(15);
        headerRow.setPadding(new Insets(10, 15, 10, 20));
        headerRow.setStyle("-fx-border-color: transparent transparent rgba(200, 200, 200, 0.3) transparent;");

        Label playerLabel = new Label("Player");
        playerLabel.setFont(new Font("Arial", 14));
        playerLabel.setTextFill(Color.WHITE);
        playerLabel.setPrefWidth(150);

        Label scoreLabel = new Label("Score");
        scoreLabel.setFont(new Font("Arial", 14));
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setPrefWidth(100);

        headerRow.getChildren().addAll(playerLabel, scoreLabel);
        tableContainer.getChildren().add(headerRow);

        model.getFinalScores().forEach((player, score) -> {
            HBox scoreRow = createScoreRow(player.getNickname(), score);
            tableContainer.getChildren().add(scoreRow);
        });

        Button newGameButton = new Button("START NEW GAME");
        newGameButton.setFont(new Font(18));
        newGameButton.setMaxWidth(Double.MAX_VALUE);
        newGameButton.setStyle(
                "-fx-background-color: rgba(80, 0, 200, 0.6); -fx-text-fill: white; -fx-background-radius: 8;"
        );
        newGameButton.setOnAction(e -> model.setMetaState(MetaState.JOINORCREATE));

        VBox layout = new VBox(20, title, tableContainer, newGameButton);
        layout.setMinHeight(Region.USE_PREF_SIZE);
        layout.setMaxHeight(Region.USE_PREF_SIZE);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setMaxWidth(400);
        layout.setStyle(
                "-fx-background-color: rgba(0, 0, 50, 0.4); -fx-background-radius: 20;"
        );

        return layout;
    }

    private HBox createScoreRow(String playerName, Integer score) {
        HBox rowContent = new HBox();
        rowContent.setAlignment(Pos.CENTER_LEFT);
        rowContent.setSpacing(10);
        rowContent.setPadding(new Insets(12, 10, 12, 20));
        rowContent.setStyle("-fx-background-color: rgba(30, 40, 70, 0.6);");

        Label playerValue = new Label(playerName);
        playerValue.setFont(new Font("Arial", 14));
        playerValue.setTextFill(Color.WHITE);
        playerValue.setPrefWidth(150);

        Label scoreValue = new Label(score.toString());
        scoreValue.setFont(new Font("Arial", 14));
        scoreValue.setTextFill(Color.WHITE);
        scoreValue.setPrefWidth(100);

        rowContent.getChildren().addAll(playerValue, scoreValue);

        return rowContent;
    }
}
