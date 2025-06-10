package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
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
    private final GuiController controller;
    private final List<Integer> startingPositions;
    private final int loopLength;
    private final Map<ShipBoard, Integer> shipToPlace;

    private static final int SLOT_SIZE = 30;

    private final List<StackPane> slots;

    public GuiFlightBoard(FlightBoard flightBoard, GuiController controller) {
        this.controller = controller;
        this.loopLength = flightBoard.getLoopLength();
        this.startingPositions = flightBoard.getStartingPositions();
        this.shipToPlace = flightBoard.getShipToPlace();
        this.slots = new ArrayList<>();

        // each slot has either a triangle (empty) or a circle (ship)
        for (int i = 0; i < loopLength; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(SLOT_SIZE, SLOT_SIZE);
            slots.add(slot);
            getChildren().add(slot);
        }

        for (int i = 0; i < loopLength; i++) {
            int pos = i; // effectively final
            shipToPlace.entrySet().stream()
                    .filter(e -> e.getValue() == pos)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .ifPresentOrElse(
                            (s)-> putShip(s, pos),
                            () -> removeShip(null, pos)
                    );
        }

        setSpacing(5);
        setAlignment(Pos.CENTER);
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        removeShip(shipBoard, shipToPlace.get(shipBoard));
        putShip(shipBoard, position);
    }

    private void putShip(ShipBoard shipBoard, int position) {
        Platform.runLater(()-> {
            slots.get(position).getChildren().clear();
            Circle circle = new Circle(SLOT_SIZE / 2.0);
            circle.setFill(shipBoard.getColor().getJfxColor());
            slots.get(position).getChildren().add(circle);
            shipToPlace.put(shipBoard, position);
        });
    }

    private void removeShip(ShipBoard shipBoard, int position) {
        Platform.runLater(()-> {
            slots.get(position).getChildren().clear();
            Polygon triangle = new Polygon(
                    0.0, 0.0,
                    0.0, SLOT_SIZE,
                    SLOT_SIZE, SLOT_SIZE / 2.0
            );
            if (startingPositions.contains(position)) {
                triangle.setFill(Color.WHITE);
                triangle.setStroke(null);
                triangle.onMouseClickedProperty().set(event -> {
                    controller.placeShipOnFlightboard(position);
                });
            } else {
                triangle.setFill(Color.TRANSPARENT);
                triangle.setStroke(Color.WHITE);
            }
            slots.get(position).getChildren().add(triangle);
            if (shipBoard != null) {
                shipToPlace.remove(shipBoard);
            }
        });
    }
}
