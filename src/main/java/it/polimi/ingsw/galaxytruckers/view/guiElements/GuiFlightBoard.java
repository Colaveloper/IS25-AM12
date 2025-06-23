package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;

public class GuiFlightBoard extends PurpleVBox {
    private final GuiController controller;
    private final List<Integer> startingPositions;
    private final int loopLength;
    private final Map<ShipBoard, Integer> shipToPlace; // doesn't use modulus

    private static final int SLOT_SIZE = 30;

    private final HBox flightBoardHBox;

    public GuiFlightBoard(FlightBoard flightBoard, GuiController controller) {
        this.controller = controller;
        this.loopLength = flightBoard.getLoopLength();
        this.startingPositions = flightBoard.getStartingPositions();
        this.shipToPlace = flightBoard.getShipToPlace();

        Label flightBoardLabel = new Label("FlightBoard");
        flightBoardLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        flightBoardLabel.setTextFill(Color.WHITE);

        flightBoardHBox = new HBox();
        flightBoardHBox.setMaxWidth(Double.MAX_VALUE);
        flightBoardHBox.setMaxHeight(30);
        flightBoardHBox.setSpacing(5);
        flightBoardHBox.setAlignment(Pos.CENTER);
        updateFlightBoardHBox();

        getChildren().addAll(flightBoardLabel, flightBoardHBox);
    }

    private void updateFlightBoardHBox() {
        flightBoardHBox.getChildren().clear();

        for (int i = 0; i < loopLength; i++) {
            StackPane slot = new StackPane();
            slot.setPrefSize(SLOT_SIZE, SLOT_SIZE);
            flightBoardHBox.getChildren().add(slot);

            int position = i;

            shipToPlace.entrySet().stream()
                    .filter(e -> Math.floorMod(e.getValue(), loopLength) == position)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .ifPresentOrElse(
                            (s)-> {
                                Circle circle = new Circle(SLOT_SIZE / 2.0);
                                circle.setFill(s.getColor().getJfxColor());
                                slot.getChildren().add(circle);
                            },
                            () -> {
                                Polygon triangle = new Polygon(
                                        0.0, 0.0,
                                        0.0, SLOT_SIZE,
                                        SLOT_SIZE, SLOT_SIZE / 2.0
                                );
                                if (startingPositions.contains(position)) {
                                    triangle.setFill(Color.WHITE);
                                    triangle.setStroke(null);
                                    triangle.onMouseClickedProperty().set(_ -> {
                                        controller.placeShipOnFlightboard(position);
                                    });
                                } else {
                                    triangle.setFill(Color.TRANSPARENT);
                                    triangle.setStroke(Color.WHITE);
                                }
                                slot.getChildren().add(triangle);
                            });
        }
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        Platform.runLater(this::updateFlightBoardHBox);
    }
}
