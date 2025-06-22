package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.Optional;

public abstract class GuiAdventureScreen extends GuiGameScreen {

    protected final AdventureState state;
    
    public GuiAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        this.state = gameState;
    }

    protected boolean isMyTurn() {
        return state.getShipBoard() == myShipBoard;
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox cardBox = new VBox(5);
        cardBox.setAlignment(Pos.CENTER);
        guiAdventureCard().ifPresent((guiAdventureCard)->{
            guiAdventureCard.setFitHeight(200);
            cardBox.getChildren().add(guiAdventureCard);
        });
        return cardBox;
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);
        getShipBoardVBox.getChildren().addAll(guiShipBoards.get(shipBoard));
        return getShipBoardVBox;
    }

    protected Optional<GuiAdventureCard> guiAdventureCard() {
        return state.getCurrentCard() == null ? Optional.empty() : Optional.of(new GuiAdventureCard(state.getCurrentCard().getId()));
    }
}
