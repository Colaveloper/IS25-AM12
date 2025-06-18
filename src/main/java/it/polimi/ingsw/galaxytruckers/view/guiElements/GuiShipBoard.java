package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GuiShipBoard extends GridPane {
    private final int minX;
    private final int minY;
    private final GuiController controller;

    Image emptyAreaImage = null;
    public final static Path emptyAreaImagePath = Path.of("src/main/resources/textures/tiles/empty_area.png");

    public GuiShipBoard(ShipBoard shipBoard, GuiController controller) {
        this.controller = controller;

        Set<Point> shipArea = shipBoard.getShipArea();
        Map<Point, Component> componentMap = shipBoard.getComponentMap();

        minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        int rows = maxY - minY + 1;
        int cols = maxX - minX + 1;

        this.setHgap(0);
        this.setVgap(0);

        try (InputStream is = Files.newInputStream(emptyAreaImagePath)) {
            emptyAreaImage = new Image(is);
        } catch (IOException e) {
            System.err.println("Path not found: " + emptyAreaImagePath);
        }

        for (int x = 0; x < cols; x++) {
            javafx.scene.control.Label colLabel = new javafx.scene.control.Label(String.valueOf(minX + x));
            this.add(colLabel, x + 1, 0);
        }

        for (int y = 0; y < rows; y++) {
            javafx.scene.control.Label rowLabel = new Label(String.valueOf(minY + y));
            this.add(rowLabel, 0, y + 1);

            for (int x = 0; x < cols; x++) {
                Point currentPoint = new Point(minX + x, minY + y);
                if (shipArea.contains(currentPoint)) {
                    ImageView areaView;
                    if (componentMap.containsKey(currentPoint)) {
                        // COMPONENT ALREADY PLACED AND WELDED
                        areaView = new GuiComponent(componentMap.get(currentPoint));
                    } else {
                        // FREE AREA
                        areaView = new ImageView();
                        areaView.setFitWidth(50);
                        areaView.setFitHeight(50);
                        areaView.setImage(emptyAreaImage);
                    }
                    areaView.setOnMouseClicked(_->controller.handlePointPress(currentPoint));
                    this.add(areaView, x + 1, y + 1);
                }
            }
        }
    }

    public void notifyPlaceComponent(int componentId, Point point, Direction orientation) {
        Platform.runLater(() -> {
            Optional<Node> toReplace = this.getChildren().stream()
                    .filter(node -> node instanceof ImageView)
                    .filter(node -> {
                        Integer col = GridPane.getColumnIndex(node);
                        Integer row = GridPane.getRowIndex(node);
                        if (col == null || row == null) return false;
                        col -= 1;
                        row -= 1;
                        return col + minX == point.x && row + minY == point.y;
                    })
                    .findFirst();

            toReplace.ifPresent(node -> {
                this.getChildren().remove(node);
                GuiComponent guiComponent = new GuiComponent(componentId);
                guiComponent.setRotate(orientation.getAngle());
                guiComponent.setOnMouseClicked(_ -> controller.handlePointPress(point));
                this.add(guiComponent, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            });
        });
    }

    public void notifyRemoveComponent(Point oldPosition) {
        Platform.runLater(() -> {
            Optional<Node> toReplace = this.getChildren().stream()
                    .filter(node -> {
                        Integer col = GridPane.getColumnIndex(node);
                        Integer row = GridPane.getRowIndex(node);
                        if (col == null || row == null) return false;
                        col -= 1;
                        row -= 1;
                        return col + minX == oldPosition.x && row + minY == oldPosition.y;
                    })
                    .findFirst();

            toReplace.ifPresent(node -> {
                this.getChildren().remove(node);
                ImageView areaView = getEmptyAreaImageView(oldPosition);
                this.add(areaView, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            });
        });
    }

    private ImageView getEmptyAreaImageView(Point position) {
        ImageView areaView = new ImageView();
        areaView.setFitWidth(50);
        areaView.setFitHeight(50);
        areaView.setImage(emptyAreaImage);
        areaView.setOnMouseClicked(_-> controller.handlePointPress(position));
        return areaView;
    }

    public GuiComponent getGuiComponent(Point position) {
        return getChildren().stream()
                .filter(node -> node instanceof GuiComponent)
                .map(node -> (GuiComponent) node)
                .filter(node -> {
                    int col = GridPane.getColumnIndex(node) - 1;
                    int row = GridPane.getRowIndex(node) - 1;
                    return col + minX == position.x && row + minY == position.y;
                })
                .findFirst()
                .orElse(null); // or throw exception if needed
    }
}
