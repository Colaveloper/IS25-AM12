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

public class GuiNewCardScreen extends GuiAdventureScreen {
    VBox layout = new VBox(20);
    Button actionButton = new Button("Draw Card");

    public GuiNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState drawCardState) {
        super(model, controller, drawCardState);
    }

    @Override
    public Pane getNode() {
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().add(guiLog);

        if(isMyTurn()) {
            actionButton.setOnAction(e -> getGuiController().drawCard());
            layout.getChildren().add(actionButton);
            guiLog.getChildren().setAll(new Label("Draw and start a new adventure!"));
        } else {
            actionButton.setDisable(true);
            guiLog.getChildren().setAll(new Label("Wait for the leader to draw"));
        }

        Optional<GuiAdventureCard> guiAdventureCard = guiAdventureCard();
        guiAdventureCard.ifPresent(layout.getChildren()::add);

        return layout;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void goNext() {
                if(state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }

            @Override
            public void drawCard() {
                if(state.getAvailableActions().contains(StateActions.DRAW_CARD)) {
                    controller.drawCard();
                }
            }
        };
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        Platform.runLater(()->{
            Optional<GuiAdventureCard> guiAdventureCard = guiAdventureCard();
            guiAdventureCard.ifPresent(layout.getChildren()::add);
            actionButton.setText("Start Adventure");
            actionButton.setOnAction(e -> getGuiController().goNext());
        });
    }
}
