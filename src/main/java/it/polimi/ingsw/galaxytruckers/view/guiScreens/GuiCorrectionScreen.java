package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiHighlights;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipCorrectionState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class GuiCorrectionScreen extends GuiGameScreen {
    private final List<Set<Point>> shipPieces;
    private boolean shipValid;
    private boolean shipBroken;
    private Point selectedPoint;
    private Integer selectedPieceIndex;

    public GuiCorrectionScreen(ClientModel model, ControllerToServer controller, ShipCorrectionState state) {
        super(model, controller, state);
        this.shipPieces = new ArrayList<>(state.getShipPieces().getOrDefault(model.getMyShip(), Collections.emptyList()));
        this.shipValid = state.getValidShipBoards().contains(model.getMyShip());
        this.shipBroken = false;
        this.selectedPoint = null;
        this.selectedPieceIndex = null;

        if (shipValid) {
            shipBroken = state.getShipPieces().containsKey(model.getMyShip());
        }

        for (ShipBoard ship : state.getShipPieces().keySet()) {
            shipNotConnected(ship, state.getShipPieces().get(ship));
        }

        String message = "";
        if (shipBroken) {
            message = "Your ship is broken. Choose a piece to keep by clicking on any component in that piece.";
        } else if (!shipValid) {
            message = "Your ship has invalid component positioning. Guick on a component to remove it.";
        } else {
            message = "Your ship is valid. Wait for other players to correct their ships.";
        }
        guiContextBox.getChildren().setAll(new Label(message));
    }

    private void showConfirmationButton(String message) {
        Platform.runLater(() -> {
            guiContextBox.getChildren().clear();

            VBox content = new VBox(10);
            Label label = new Label(message);
            label.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");

            Button confirmButton = new Button("Confirm");
            confirmButton.setOnAction(e -> confirmSelection());

            content.getChildren().addAll(label, confirmButton);
            content.setAlignment(Pos.CENTER);

            guiContextBox.getChildren().add(content);
            guiContextBox.setAlignment(Pos.CENTER);
        });
    }

    private void confirmSelection() {
        if (!shipValid && selectedPoint != null) {
            controller.removeComponent(selectedPoint);
            clearSelection();
        } else if (shipBroken && selectedPieceIndex != null) {
            controller.chooseShipPiece(selectedPieceIndex);
            clearSelection();
        }
    }

    private void clearSelection() {
        if (selectedPoint != null || selectedPieceIndex != null) {
            guiShipBoards.get(model.getMyShip()).clearHighlights();

            if (shipBroken) {
                for (int i = 0; i < shipPieces.size(); i++) {
                    List<GuiHighlights> highlights = GuiHighlights.getSomeColors(shipPieces.size());
                    guiShipBoards.get(model.getMyShip()).highlightPoints(shipPieces.get(i), highlights.get(i + 1).getColor());
                }
            }

            selectedPoint = null;
            selectedPieceIndex = null;

            String message = "";
            if (shipBroken) {
                message = "Your ship is broken. Choose a piece to keep by clicking on any component in that piece.";
            } else if (!shipValid) {
                message = "Your ship has invalid component positioning. Click on a component to remove it.";
            } else {
                message = "Your ship is valid. Wait for other players to correct their ships.";
            }
            guiContextBox.getChildren().setAll(new Label(message));
        }
    }

    private void shipNotConnected(ShipBoard shipBoard, List<Set<Point>> pieces) {
        int numPieces = pieces.size();
        List<GuiHighlights> highlights = GuiHighlights.getSomeColors(numPieces);
        for (int i = 0; i < numPieces; i++) {
            guiShipBoards.get(shipBoard).highlightPoints(pieces.get(i), highlights.get(i + 1).getColor());
        }
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        for (Point piece : removed) {
            guiShipBoards.get(shipBoard).notifyRemoveComponent(piece);
        }

        if (model.getMyShip().equals(shipBoard)) {
            shipBroken = false;
            shipValid = true;
            String message = "Your ship is valid. Wait for other players to correct their ships.";
            guiContextBox.getChildren().setAll(new Label(message));
        }
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> pieces) {
        if (model.getMyShip().equals(shipBoard)) {
            this.shipPieces.clear();
            this.shipPieces.addAll(pieces);
            shipBroken = true;
            shipValid = true;
            String message = "Your ship is broken. Choose a piece to keep by clicking on any component in that piece.";
            guiContextBox.getChildren().setAll(new Label(message));
        }
        shipNotConnected(shipBoard, pieces);
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        if (model.getMyShip().equals(shipBoard)) {
            shipBroken = false;
            shipValid = true;
            String message = "Your ship is valid. Wait for other players to correct their ships.";
            guiContextBox.getChildren().setAll(new Label(message));
        }
        guiShipBoards.get(shipBoard).clearHighlights();
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (state.getAvailableActions().contains(StateActions.REMOVE_COMPONENT) && !shipValid) {
                    selectedPoint = point;
                    guiShipBoards.get(model.getMyShip()).highlightPoints(
                            Collections.singleton(point), javafx.scene.paint.Color.YELLOW);
                    showConfirmationButton("Remove this component?");
                } else if (state.getAvailableActions().contains(StateActions.CHOOSE_SHIP_PIECE) && shipBroken) {
                    IntStream.range(0, shipPieces.size())
                            .filter(i -> shipPieces.get(i).contains(point))
                            .findFirst()
                            .ifPresent(i -> {
                                selectedPieceIndex = i;
                                guiShipBoards.get(model.getMyShip()).clearHighlights();
                                guiShipBoards.get(model.getMyShip()).highlightPoints(
                                        shipPieces.get(i), Color.YELLOW);
                                showConfirmationButton("Keep this piece?");
                            });
                }
            }
        };
    }
}

