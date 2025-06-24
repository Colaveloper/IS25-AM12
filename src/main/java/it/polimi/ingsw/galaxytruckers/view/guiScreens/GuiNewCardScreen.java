package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * GUI screen for drawing new adventure cards.
 * This screen allows the leader player to draw a new adventure card
 * and then start the adventure, while other players wait for the leader's action.
 */
public class GuiNewCardScreen extends GuiAdventureScreen {
    Button actionButton = new Button("Draw Card");

    /**
     * Constructs a new card drawing screen.
     *
     * @param model         The client model containing game state
     * @param controller    The controller for communicating with the server
     * @param drawCardState The state containing valid actions for this phase
     */
    public GuiNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState drawCardState) {
        super(model, controller, drawCardState);

        if(isMyTurn()) {
            actionButton.setOnAction(e -> getGuiController().drawCard());
            guiButtonBox.getChildren().clear();
            guiButtonBox.getChildren().add(actionButton);
            guiLog.log("Draw and start a new adventure!");
        } else {
            guiLog.log("Wait for the leader to draw and start the next adventure");
        }
    }

    /**
     * {@inheritDoc}
     * Creates and returns a GUI controller that handles drawing cards
     * and navigation to the next screen.
     *
     * @return A GUI controller for this screen
     */
    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            /**
             * Handles navigation to the next screen if allowed by the current state.
             */
            @Override
            public void goNext() {
                if(state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }

            /**
             * Handles drawing a card if allowed by the current state.
             */
            @Override
            public void drawCard() {
                if(state.getAvailableActions().contains(StateActions.DRAW_CARD)) {
                    controller.drawCard();
                }
            }
        };
    }

    /**
     * {@inheritDoc}
     * Updates the GUI when a card is drawn, displaying the card and
     * changing the action button to allow starting the adventure.
     *
     * @param adventureCard The adventure card that was drawn
     */
    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        Platform.runLater(()->{
            guiAdventureCard().ifPresent((guiAdventureCard)-> {
                guiAdventureCard.setFitHeight(400);
                cardBox.getChildren().clear();
                cardBox.getChildren().add(guiAdventureCard);
            });
            actionButton.setText("Start Adventure");
            actionButton.setOnAction(e -> getGuiController().goNext());
        });
    }
}
