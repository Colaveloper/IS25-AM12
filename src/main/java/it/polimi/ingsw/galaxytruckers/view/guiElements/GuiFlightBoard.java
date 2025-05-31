package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.*;

public class GuiFlightBoard extends GuiElement {
    FlightBoard flightBoard;

    public GuiFlightBoard(FlightBoard flightBoard, ClientController controller) {
        super(controller);
    }

    @Override
    public Node getNode() {
        final int slotSize = 30;
        final double spacing = 5.0;
        final int loopLength = flightBoard.getLoopLength();

        HBox container = new HBox(spacing);
        container.setAlignment(Pos.CENTER);
        List<StackPane> slots = new ArrayList<>();

        for (int i = 0; i < loopLength; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(slotSize, slotSize);
            slots.add(slot);
            container.getChildren().add(slot);
        }

        Runnable update = () -> {
            Set<Integer> starting = new HashSet<>(flightBoard.getStartingPositions());
            Map<ShipBoard, Integer> colorMap = new HashMap<>(flightBoard.getShipToPlace());

            Platform.runLater(() -> {
                for (int i = 0; i < loopLength; i++) {
                    int finalI = i;
                    StackPane slot = slots.get(i);
                    slot.getChildren().clear();

                    Optional<GameColor> colorHere = colorMap.entrySet().stream()
                            .filter(e -> e.getValue() == finalI)
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .map(ShipBoard::getColor);

                    if (colorHere.isPresent()) {
                        Circle circle = new Circle(slotSize / 2.0);
                        circle.setFill(colorHere.get().getJfxColor());
                        slot.getChildren().add(circle);
                    } else {
                        Polygon triangle = new Polygon(
                                0.0, 0.0,
                                0.0, slotSize,
                                slotSize, slotSize / 2.0
                        );
                        if (starting.contains(finalI)) {
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
        };

        update.run();
        flightBoard.addObserver(update::run);
        return container;
    }

}
