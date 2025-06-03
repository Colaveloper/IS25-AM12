package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class GuiShipHandAndStash extends VBox {

    private final ControllerToServer controller;

    Image emptyAreaImage = null;

    private final GridPane shipGrid;
    private final int minX;
    private final int minY;
    private final GuiHand guiHand;
    private final GuiStash guiStash;

    public final static Path emptyAreaImagePath = Path.of("src/main/resources/textures/tiles/empty_area.png");

    public GuiShipHandAndStash(ShipBoard shipBoard, ControllerToServer controller) {
        this.controller = controller;

        Set<Point> shipArea = shipBoard.getShipArea();
        Map<Point, Component> componentMap = shipBoard.getComponentMap();


        minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        int rows = maxY - minY + 1;
        int cols = maxX - minX + 1;

        shipGrid = new GridPane();
        shipGrid.setHgap(0);
        shipGrid.setVgap(0);

        try (InputStream is = Files.newInputStream(emptyAreaImagePath)) {
            emptyAreaImage = new Image(is);
        } catch (IOException e) {
            System.err.println("Path not found: " + emptyAreaImagePath);
        }

        for (int x = 0; x < cols; x++) {
            Label colLabel = new Label(String.valueOf(minX + x));
            shipGrid.add(colLabel, x + 1, 0);
        }

        for (int y = 0; y < rows; y++) {
            Label rowLabel = new Label(String.valueOf(minY + y));
            shipGrid.add(rowLabel, 0, y + 1);

            for (int x = 0; x < cols; x++) {
                Point currentPoint = new Point(minX + x, minY + y);
                if (shipArea.contains(currentPoint)) {
                    ImageView areaView;
                    if (componentMap.containsKey(currentPoint)) {
                        areaView = new GuiComponent(componentMap.get(currentPoint));
                    } else {
                        areaView = new ImageView();
                        areaView.setFitWidth(50);
                        areaView.setFitHeight(50);
                        areaView.setImage(emptyAreaImage);
                        areaView.setOnMouseClicked((_)->
                                controller.placeComponent(currentPoint, Direction.UP)
                                // TODO: use real direction
                        );
                    }
                    shipGrid.add(areaView, x + 1, y + 1);
                }
            }
        }

        HBox handAndStashBox = new HBox();

        guiHand = new GuiHand(
                shipBoard.getLastPosition()==null ? shipBoard.getLastComponent() : null,
                controller
        );

        guiStash = new GuiStash(shipBoard.getStashedComponents(), controller);

        handAndStashBox.getChildren().addAll(guiHand, guiStash);

        this.getChildren().addAll(shipGrid, handAndStashBox);
    }

    public void notifyPlaceComponent(int componentId, Point point, Direction orientation) {
        shipGrid.getChildren().stream()
                .filter(node -> node instanceof ImageView)
                .filter(node -> {
                    int col = GridPane.getColumnIndex(node) - 1;
                    int row = GridPane.getRowIndex(node) - 1;
                    return col+minX == point.x && row+minY == point.y;
                })
                .findFirst()
                .ifPresent(node ->
                        Platform.runLater(() -> {
                                    shipGrid.getChildren().remove(node);
                                    shipGrid.add(
                                            new GuiComponent(componentId),
                                            GridPane.getColumnIndex(node),
                                            GridPane.getRowIndex(node)
                                    );
                                }
                        )
                );
    }

    public void notifyRemoveComponent(Point oldPosition) {
            shipGrid.getChildren().stream()
                    .filter(node -> {
                        int col = GridPane.getColumnIndex(node) - 1;
                        int row = GridPane.getRowIndex(node) - 1;
                        return col + minX == oldPosition.x && row + minY == oldPosition.y;
                    })
                    .findFirst()
                    .ifPresent(node ->
                            Platform.runLater(() -> {
                                        shipGrid.getChildren().remove(node);
                                ImageView areaView = getEmptyAreaImageView(oldPosition);
                                shipGrid.add(
                                                areaView,
                                                GridPane.getColumnIndex(node),
                                                GridPane.getRowIndex(node)
                                        );
                                    }
                            )
                    );
    }

    private ImageView getEmptyAreaImageView(Point oldPosition) {
        ImageView areaView = new ImageView();
        areaView.setFitWidth(50);
        areaView.setFitHeight(50);
        areaView.setImage(emptyAreaImage);
        areaView.setOnMouseClicked((_)->
                        controller.placeComponent(oldPosition, Direction.UP)
                // TODO: use real direction
        );
        return areaView;
    }

    public void notifyClearHand() {
        guiHand.notifyClearHand();
    }

    public void notifySetHand(Component component) {
        guiHand.notifySetHand(component);
    }

    public void notifyStash(Component component) {
        guiStash.notifyStash(component);
    }

    public void notifyGrabStashed(int index) {
        guiStash.notifyGrab(index);
    }
}