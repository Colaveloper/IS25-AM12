package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GrabRewardState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * GUI screen for handling the reward grabbing phase of the game
 */
public class GuiRewardScreen extends GuiAdventureScreen {

    public GuiRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState grabRewardState) {
        super(model, controller, grabRewardState);
        setupButtons();
        updateLog();
    }

    /**
     * Sets up the buttons for grabbing rewards or skipping
     */
    private void setupButtons() {
        guiButtonBox.getChildren().clear();

        if (isMyTurn()) {
            HBox buttonsBox = getButtonsBox();
            guiButtonBox.getChildren().add(buttonsBox);
        }
    }

    private HBox getButtonsBox() {
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        Button grabRewardButton = new Button("Grab Reward");
        grabRewardButton.setOnAction(event -> {
            controller.grabReward();
            guiLog.log("You grabbed the reward!");
        });

        Button skipButton = new Button("Skip");
        skipButton.setOnAction(event -> {
            controller.goNext();
            guiLog.log("You skipped the reward.");
        });

        grabRewardButton.setDisable(!isMyTurn());
        skipButton.setDisable(!isMyTurn());

        buttonsBox.getChildren().addAll(grabRewardButton, skipButton);
        return buttonsBox;
    }

    /**
     * Updates the log with the current state information
     */
    private void updateLog() {
        if (isMyTurn()) {
            guiLog.log("Your turn to decide whether to take the reward.");
        } else {
            guiLog.log("Waiting for other players' ship to decide about the reward.");
        }
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void goNext() {
                controller.goNext();
            }
        };
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox cardBox = new VBox(10);
        cardBox.setAlignment(Pos.CENTER);

        Text title = new Text("Reward Phase");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);
        cardBox.getChildren().add(title);

        Label turnInfo = new Label(isMyTurn()
                ? "Your turn to decide about the reward"
                : "Waiting for other players' decision");
        turnInfo.setTextFill(Color.WHITE);
        cardBox.getChildren().add(turnInfo);

        return cardBox;
    }
}
