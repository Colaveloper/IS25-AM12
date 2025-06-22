//package it.polimi.ingsw.galaxytruckers.view.guiElements;
//
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Platform;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.StackPane;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import javafx.util.Duration;
//
//public class GuiHourglass extends StackPane {
//    private final GuiController controller;
//    private final Label timerLabel;
//    private final Button hourglassButton;
//    private final Timeline timeline;
//    private int secondsLeft = 60;
//    private boolean isRunning = true; // Start in running state
//    private GuiLog guiLog;
//
//    public GuiHourglass(GuiController controller) {
//        this.controller = controller;
//
//        timerLabel = new Label("60");
//        timerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
//        timerLabel.setTextFill(Color.BLACK);
//
//        hourglassButton = new Button("⏳");
//        hourglassButton.setFont(Font.font("System", 14));
//        hourglassButton.setPadding(new Insets(2, 4, 2, 4));
//        hourglassButton.setOnAction(e -> {
//            if (!isRunning) {
//                controller.flipHourglass();
//            }
//        });
//
//        setMaxSize(40, 30);
//        setMinSize(40, 30);
//        setPrefSize(40, 30);
//        setPadding(new Insets(2));
//        setAlignment(Pos.CENTER);
//
//        setBackground(new javafx.scene.layout.Background(
//            new javafx.scene.layout.BackgroundFill(
//                Color.rgb(240, 240, 240, 0.7),
//                new javafx.scene.layout.CornerRadii(4),
//                Insets.EMPTY
//            )
//        ));
//
//        setBorder(new javafx.scene.layout.Border(
//            new javafx.scene.layout.BorderStroke(
//                Color.rgb(204, 204, 204),
//                javafx.scene.layout.BorderStrokeStyle.SOLID,
//                new javafx.scene.layout.CornerRadii(4),
//                new javafx.scene.layout.BorderWidths(1)
//            )
//        ));
//
//        getChildren().add(timerLabel);
//
//        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
//            secondsLeft--;
//            timerLabel.setText(String.valueOf(secondsLeft));
//
//            if (secondsLeft <= 10) {
//                timerLabel.setTextFill(Color.RED);
//            } else if (secondsLeft <= 20) {
//                timerLabel.setTextFill(Color.ORANGE);
//            }
//
//            if (secondsLeft <= 0) {
//                notifyHourglassEnd();
//            }
//        }));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//
//        timeline.play();
//    }
//
//    public void notifyFlipHourglass() {
//        Platform.runLater(() -> {
//            isRunning = true;
//            secondsLeft = 60;
//            timerLabel.setText("60");
//            timerLabel.setTextFill(Color.BLACK); // Reset color
//            getChildren().setAll(timerLabel);
//            timeline.playFromStart();
//        });
//    }
//
//    public void notifyHourglassEnd() {
//        Platform.runLater(() -> {
//            isRunning = false;
//            getChildren().setAll(hourglassButton);
//            timeline.stop();
//        });
//    }
//}
