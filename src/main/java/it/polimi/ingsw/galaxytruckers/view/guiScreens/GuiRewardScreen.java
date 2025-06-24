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
 * GUI screen for handling the reward grabbing phase of the game.
 * <p>
 * This screen is shown when a player can grab a reward (credits) or choose to skip during the adventure phase.
 * It displays the available options and updates the log and UI accordingly.
 * </p>
 *
 * @author (your name or team)
 */
public class GuiRewardScreen extends GuiAdventureScreen {

    /**
     * Constructs a new GuiRewardScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param grabRewardState the state containing information about the reward phase
     */
    public GuiRewardScreen(ClientModel model, ControllerToServer controller, GrabRewardState grabRewardState) {
        super(model, controller, grabRewardState);
        setupButtons();
        updateLog();
    }

    /**
     * Sets up the buttons for grabbing rewards or skipping.
     * Adds the buttons to the GUI and binds their actions.
     */
    private void setupButtons() {
        guiButtonBox.getChildren().clear();

        if (isMyTurn()) {
            HBox buttonsBox = getButtonsBox();
            guiButtonBox.getChildren().add(buttonsBox);
        }
    }

    /**
     * Creates and returns an HBox containing the "Grab Reward" and "Skip" buttons.
     * <p>
     * The buttons allow the player to either grab the available reward or skip their turn.
     * Button actions are bound to the appropriate controller methods and log updates.
     * </p>
     *
     * @return HBox with reward action buttons
     */
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

    /**
     * Returns a VBox containing UI elements for the reward phase.
     * <p>
     * This includes a title and information about whose turn it is to decide about the reward.
     * </p>
     *
     * @return VBox with reward phase UI elements
     */
    @Override
    protected VBox getFreeUseVBox() {
        VBox cardBox = super.getFreeUseVBox();
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
