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

/**
 * GUI component representing the flight board in the game.
 * <p>
 * This class visually displays the flight board, including ship positions and the flight loop.
 * It uses a horizontal box to represent the board and updates the display based on the current state of the {@link FlightBoard} model.
 * </p>
 */
public class GuiFlightBoard extends PurpleVBox {
    private final GuiController controller;
    private final List<Integer> startingPositions;
    private final int loopLength;
    private final Map<ShipBoard, Integer> shipToPlace; // doesn't use modulus

    private static final int SLOT_SIZE = 30;

    /**
     * The horizontal box containing the visual slots of the flight board.
     */
    private final HBox flightBoardHBox;

    /**
     * Constructs a GuiFlightBoard for the given flight board model and controller.
     *
     * @param flightBoard the flight board model to represent
     * @param controller the GUI controller handling actions
     */
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

    /**
     * Updates the visual representation of the flight board slots and ship positions.
     */
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
                                        controller.placeShipOnFlightBoard(position);
                                    });
                                } else {
                                    triangle.setFill(Color.TRANSPARENT);
                                    triangle.setStroke(Color.WHITE);
                                }
                                slot.getChildren().add(triangle);
                            });
        }
    }

    /**
     * Notifies the GUI to update the flight board display when a ship's position changes.
     * <p>
     * This method is typically called when a ship moves to a new position on the flight board.
     * It schedules a UI update on the JavaFX application thread.
     * </p>
     *
     * @param shipBoard the ship whose position has changed
     * @param position the new position of the ship on the flight board
     */
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        Platform.runLater(this::updateFlightBoardHBox);
    }
}
