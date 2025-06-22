package it.polimi.ingsw.galaxytruckers.view.guiElements;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GuiForecast extends PurpleHBox {
    private final GuiController controller;
    private final List<StackPane> slots;
    private final ShipBoard[] blockedForecasts;
    private boolean meWatching;

    private static final int SLOT_SIZE = 30;
    double cardWidth = 60;
    double cardHeight = 90;


    public GuiForecast(ShipBoard[] blockedForecasts, GuiController controller) {
        super(10);
        this.controller = controller;
        this.slots = new ArrayList<>();
        this.blockedForecasts = blockedForecasts;

        for (int i = 0; i < blockedForecasts.length; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(SLOT_SIZE, SLOT_SIZE);
            slots.add(slot);
            getChildren().add(slot);

        }
        updateCoveredDecks();
    }

    private void updateCoveredDecks() {
        for (int i = 0; i < blockedForecasts.length; i++) {

            ShipBoard shipBoard = blockedForecasts[i];
            if (shipBoard == null) {
                slots.get(i).getChildren().add(createFreeForecast());
            } else {
                slots.get(i).getChildren().add(createTakenForecast(shipBoard));
            }

            int finalI = i;
            slots.get(i).setOnMouseClicked(_ -> controller.acquireForecast(finalI));
        }
    }

    private Rectangle createFreeForecast() {
        return createCardBase(Color.LIGHTGRAY);
    }

    private Rectangle createTakenForecast(ShipBoard shipBoard) {
        Rectangle card = createCardBase(shipBoard.getColor().getJfxColor());
        card.setOnMouseClicked(_->controller.releaseForecast());
        return card;
    }

    private Rectangle createCardBase(Color fillColor) {
        double arc = 12;

        Rectangle card = new Rectangle(cardWidth, cardHeight);
        card.setArcWidth(arc);
        card.setArcHeight(arc);
        card.setFill(fillColor);
        return card;
    }

    public void notifyOtherPeekForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            if (meWatching) return;
            updateCoveredDecks();
        });
    }

    public void notifyOtherReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            if (meWatching) return;
            updateCoveredDecks();
        });
    }

    public void notifyMePeekForecast() {
        meWatching = true;
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        Platform.runLater(() -> {
            for (int i = 0; i < slots.size(); i++) {
                AdventureCard adventureCard = adventureCards.get(i);
                ImageView adventureCardView = new GuiAdventureCard(adventureCard.getId());
                adventureCardView.setFitWidth(cardWidth);
                adventureCardView.setFitHeight(cardHeight);
                slots.get(i).getChildren().setAll(adventureCardView);
            }
        });
    }

    public void notifyMeReleaseForecast() {
        Platform.runLater(()-> {
            meWatching = false;
            for (StackPane slot : slots) {
                slot.getChildren().setAll(createFreeForecast());
            }
        });
    }

}
