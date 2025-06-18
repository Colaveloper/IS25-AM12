package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class GuiHourglass extends StackPane {
    private final GuiController controller;
    private final Label timerLabel = new Label("60");
    private final Button hourglassButton = new Button("⏳");
    private final Timeline timeline;
    private int secondsLeft = 60;

    public GuiHourglass(GuiController controller) {
        this.controller = controller;

        timerLabel.setStyle("-fx-font-size: 24px;");
        hourglassButton.setStyle("-fx-font-size: 24px;");
        hourglassButton.setOnAction(e -> controller.flipHourglass());
        getChildren().add(timerLabel);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            timerLabel.setText(String.valueOf(secondsLeft));
            if (secondsLeft <= 0) {
                notifyHourglassEnd();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void notifyFlipHourglass() {
        Platform.runLater(()-> {
            secondsLeft = 60;
            timerLabel.setText("60");
            getChildren().setAll(timerLabel);
            timeline.playFromStart();
        });
    }

    public void notifyHourglassEnd() {
        Platform.runLater(()-> {
            getChildren().setAll(hourglassButton);
            timeline.stop();
            controller.flipHourglass();
        });
    }
}
