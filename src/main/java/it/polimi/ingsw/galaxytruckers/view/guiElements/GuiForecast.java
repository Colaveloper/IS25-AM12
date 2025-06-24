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

/**
 * GUI component representing the forecast area in the game.
 * <p>
 * This class displays the forecast slots, showing which forecasts are blocked by ships and which are free.
 * Users can interact with the slots to acquire forecasts. The display updates automatically based on the
 * current state of the blocked forecasts.
 * </p>
 */
public class GuiForecast extends PurpleHBox {
    private final GuiController controller;
    private final List<StackPane> slots;
    private final ShipBoard[] blockedForecasts;
    private boolean meWatching;

    private static final int SLOT_SIZE = 30;
    double cardWidth = 60;
    double cardHeight = 90;

    /**
     * Constructs a GuiForecast for the given blocked forecasts and controller.
     *
     * @param blockedForecasts array of ships blocking each forecast slot (null if free)
     * @param controller the GUI controller handling actions
     */
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

    /**
     * Updates the display of the forecast slots based on the current blocked forecasts.
     */
    private void updateCoveredDecks() {
        for (int i = 0; i < blockedForecasts.length; i++) {
            slots.get(i).getChildren().clear();

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

    /**
     * Notifies the GUI that another player is peeking at a forecast slot.
     * <p>
     * If the current user is not watching, this method updates the forecast display.
     * </p>
     * @param shipBoard the ship peeking at the forecast
     * @param deckIndex the index of the forecast deck being peeked at
     */
    public void notifyOtherPeekForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            if (meWatching) return;
            updateCoveredDecks();
        });
    }

    /**
     * Notifies the GUI that another player has released a forecast slot.
     * <p>
     * If the current user is not watching, this method updates the forecast display.
     * </p>
     * @param shipBoard the ship releasing the forecast
     * @param deckIndex the index of the forecast deck being released
     */
    public void notifyOtherReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            if (meWatching) return;
            updateCoveredDecks();
        });
    }

    /**
     * Notifies the GUI that the current user is peeking at the forecast area.
     * Sets the internal flag to indicate the user is watching.
     */
    public void notifyMePeekForecast() {
        meWatching = true;
    }

    /**
     * Sets the forecast deck to display the given adventure cards.
     * <p>
     * This method updates the forecast slots to show the specified adventure cards.
     * </p>
     * @param adventureCards the list of adventure cards to display
     */
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

    /**
     * Notifies the GUI that the current user has released the forecast area.
     * <p>
     * This method resets the internal watching flag and updates the forecast display.
     * </p>
     */
    public void notifyMeReleaseForecast() {
        Platform.runLater(()-> {
            meWatching = false;
            updateCoveredDecks();
        });
    }
}
