package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class GuiNewCardScreen extends GuiAdventureScreen {
    public HBox layout;

    public GuiNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState drawCardState) {
        super(model, controller, drawCardState);
        layout = new HBox();
    }

    @Override
    public Parent getNode() {
        updateLayout();
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
        Platform.runLater(this::updateLayout);
    }

    private void updateLayout() {
        layout.getChildren().clear();

        Optional<GuiAdventureCard> guiAdventureCard = guiAdventureCard();
        guiAdventureCard.ifPresent(layout.getChildren()::add);

        if (isMyTurn()) {
            Button actionButton;
            if (guiAdventureCard.isPresent()) {
                actionButton = new Button("Start Adventure");
                actionButton.setOnAction(e -> getGuiController().goNext());
            } else {
                actionButton = new Button("Draw Card");
                actionButton.setOnAction(e -> getGuiController().drawCard());
            }
            layout.getChildren().add(actionButton);
        }
    }
}
