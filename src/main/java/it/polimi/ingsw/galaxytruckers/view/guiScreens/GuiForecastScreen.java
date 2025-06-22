package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipBuildingState;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class GuiForecastScreen extends GuiGameScreen {
    HBox cardsHBox = new HBox(20);

    public GuiForecastScreen(ClientModel model, ControllerToServer controller, ShipBuildingState state) {
        super(model, controller, state);

        Button releaseButton = new Button("Release");
        releaseButton.setOnAction(e -> controller.releaseForecast());

        guiButtonBox.getChildren().clear();
        guiButtonBox.getChildren().add(releaseButton);
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        Platform.runLater(() -> {
            for (AdventureCard adventureCard : adventureCards) {
                cardsHBox.getChildren().add(new GuiAdventureCard(adventureCard.getId()));
            }
        });
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox cardsVBox = new VBox(20);
        cardsVBox.setAlignment(Pos.CENTER);

        cardsHBox.setAlignment(Pos.CENTER);

        cardsVBox.getChildren().add(cardsHBox);
        return cardsVBox;
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        return guiShipBoards.get(shipBoard);
    }

    @Override
    protected GuiController getGuiController() {
        return null;
    }
}
