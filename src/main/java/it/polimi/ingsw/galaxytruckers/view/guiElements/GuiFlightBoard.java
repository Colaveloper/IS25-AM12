package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.*;

public class GuiFlightBoard extends HBox {
    private static final int SLOT_SIZE = 30;
    private static final double SPACING = 5.0;

    private final FlightBoard flightBoard;
    private final List<StackPane> slots;

    public GuiFlightBoard(FlightBoard flightBoard, ControllerToServer controller) {
        super(SPACING);
        this.flightBoard = flightBoard;
        this.slots = new ArrayList<>();
        setAlignment(Pos.CENTER);

        int loopLength = flightBoard.getLoopLength();
        for (int i = 0; i < loopLength; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(SLOT_SIZE, SLOT_SIZE);
            slots.add(slot);
            getChildren().add(slot);
        }

        // Initial rendering and register observer
        updateSlots();
        flightBoard.addObserver(this::updateSlots);
    }

    private void updateSlots() {
        Set<Integer> startingPositions = new HashSet<>(flightBoard.getStartingPositions());
        Map<ShipBoard, Integer> shipPositions = new HashMap<>(flightBoard.getShipToPlace());

        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            int loopLength = flightBoard.getLoopLength();
            for (int i = 0; i < loopLength; i++) {
                StackPane slot = slots.get(i);
                slot.getChildren().clear();

                int pos = i;
                Optional<GameColor> maybeColor = shipPositions.entrySet().stream()
                        .filter(e -> e.getValue() == pos)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .map(ShipBoard::getColor);

                if (maybeColor.isPresent()) {
                    Circle circle = new Circle(SLOT_SIZE / 2.0);
                    circle.setFill(maybeColor.get().getJfxColor());
                    slot.getChildren().add(circle);
                } else {
                    Polygon triangle = new Polygon(
                            0.0, 0.0,
                            0.0, SLOT_SIZE,
                            SLOT_SIZE, SLOT_SIZE / 2.0
                    );
                    if (startingPositions.contains(pos)) {
                        triangle.setFill(Color.BLACK);
                        triangle.setStroke(null);
                    } else {
                        triangle.setFill(Color.TRANSPARENT);
                        triangle.setStroke(Color.BLACK);
                    }
                    slot.getChildren().add(triangle);
                }
            }
        });
    }
}
