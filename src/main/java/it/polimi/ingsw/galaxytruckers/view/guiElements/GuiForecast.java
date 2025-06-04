package it.polimi.ingsw.galaxytruckers.view.guiElements;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class GuiForecast extends HBox {
    private final ControllerToServer controller;
    private ShipBoard[] blockedForecasts;
    private final List<StackPane> slots;

    private static final int SLOT_SIZE = 30;


    public GuiForecast(ShipBoard[] blockedForecasts, ControllerToServer controller) {
        this.controller = controller;
        this.blockedForecasts = blockedForecasts;
        this.slots = new ArrayList<>();

        for (int i = 0; i < blockedForecasts.length; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(SLOT_SIZE, SLOT_SIZE);
            slots.add(slot);
            getChildren().add(slot);

            ShipBoard shipBoard = blockedForecasts[i];
            if (shipBoard == null) {
                slot.getChildren().add(createFreeForecast());
            } else {
                slot.getChildren().add(createTakenForecast(shipBoard));
            }

            int finalI = i;
            slot.setOnMouseClicked(_ -> controller.acquireForecast(finalI));
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
        double width = 60;
        double height = 90;
        double arc = 12;

        Rectangle card = new Rectangle(width, height);
        card.setArcWidth(arc);
        card.setArcHeight(arc);
        card.setFill(fillColor);
        return card;
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            slots.get(deckIndex).getChildren().setAll(createTakenForecast(shipBoard));
        });
    }

    public void notifyReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        Platform.runLater(()-> {
            slots.get(deckIndex).getChildren().setAll(createFreeForecast());
        });
    }
}
