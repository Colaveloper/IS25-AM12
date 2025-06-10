package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCardRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class GuiForecastScreen extends GuiScreen {
    HBox cardsBox;

    public GuiForecastScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public Parent getNode() {

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        cardsBox = new HBox(20);
        cardsBox.setAlignment(Pos.CENTER);

        Button releaseButton = new Button("Release");
        releaseButton.setOnAction(e -> controller.releaseForecast());

        layout.getChildren().addAll(cardsBox, releaseButton);
        return layout;
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        Platform.runLater(() -> {
            GuiAdventureCardRegistry guiAdventureCardRegistry = GuiAdventureCardRegistry.getInstance();
            for (AdventureCard adventureCard : adventureCards) {
                ImageView cardView = new ImageView();
                cardView.setFitWidth(200);
                cardView.setPreserveRatio(true);
                cardView.setImage(guiAdventureCardRegistry.getImage(adventureCard.getId()));
                cardsBox.getChildren().add(cardView);
            }
        });
    }
}
