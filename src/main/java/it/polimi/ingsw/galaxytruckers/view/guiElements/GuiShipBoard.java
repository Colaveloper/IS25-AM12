package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiController;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class GuiShipBoard extends PurpleVBox {
    private int minX;
    private int minY;
    private final GuiController controller;
    private final GridPane shipGrid;

    Image emptyAreaImage = null;
    public final static Path emptyAreaImagePath = Path.of("src/main/resources/textures/tiles/empty_area.png");

    public GuiShipBoard(String name, ShipBoard shipBoard, GuiController controller) {
        this.controller = controller;

        super(5);
        setAlignment(Pos.CENTER);

        shipGrid = new GridPane();
        setShipGrid(shipBoard);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        this.getChildren().addAll(nameLabel, shipGrid);
    }

    private void setShipGrid(ShipBoard shipBoard) {
        Set<Point> shipArea = shipBoard.getShipArea();
        Map<Point, Component> componentMap = shipBoard.getComponentMap();

        minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        int rows = maxY - minY + 1;
        int cols = maxX - minX + 1;

        shipGrid.setHgap(0);
        shipGrid.setVgap(0);

        try (InputStream is = Files.newInputStream(emptyAreaImagePath)) {
            emptyAreaImage = new Image(is);
        } catch (IOException e) {
            System.err.println("Path not found: " + emptyAreaImagePath);
        }

        for (int x = 0; x < cols; x++) {
            javafx.scene.control.Label colLabel = new javafx.scene.control.Label(String.valueOf(minX + x));
            shipGrid.add(colLabel, x + 1, 0);
        }

        for (int y = 0; y < rows; y++) {
            javafx.scene.control.Label rowLabel = new Label(String.valueOf(minY + y));
            shipGrid.add(rowLabel, 0, y + 1);

            for (int x = 0; x < cols; x++) {
                Point currentPoint = new Point(minX + x, minY + y);
                if (shipArea.contains(currentPoint)) {
                    StackPane areaPane;
                    if (componentMap.containsKey(currentPoint)) {
                        areaPane = GuiComponent.of(componentMap.get(currentPoint));
                        areaPane.setMaxSize(50, 50);
                        areaPane.setOnMouseClicked(_->controller.handlePointPress(currentPoint));
                    } else {
                        areaPane = getEmptyAreaPane(currentPoint);
                    }
                    shipGrid.add(areaPane, x + 1, y + 1);
                }
            }
        }
    }

    public void notifyPlaceComponent(Component component, Point point, Direction orientation) {
        Platform.runLater(() -> {
            Optional<Node> toReplace = shipGrid.getChildren().stream()
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
                shipGrid.getChildren().remove(node);
                GuiComponent guiComponent = new GuiComponent(component);
                guiComponent.setMaxSize(50, 50);
                guiComponent.setOnMouseClicked(_ -> controller.handlePointPress(point));
                shipGrid.add(guiComponent, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            });
        });
    }

    public void notifyRemoveComponent(Point oldPosition) {
        Platform.runLater(() -> {
            Optional<Node> toReplace = shipGrid.getChildren().stream()
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
                shipGrid.getChildren().remove(node);
                StackPane areaView = getEmptyAreaPane(oldPosition);
                shipGrid.add(areaView, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            });
        });
    }

    private StackPane getEmptyAreaPane(Point position) {
        StackPane areaPane = new StackPane();

        ImageView freeAreaView = new ImageView(emptyAreaImage);
        freeAreaView.setFitWidth(50);
        freeAreaView.setFitHeight(50);
//        freeAreaView.fitWidthProperty().bind(areaPane.widthProperty());
//        freeAreaView.fitHeightProperty().bind(areaPane.heightProperty());

        areaPane.getChildren().add(freeAreaView);
        areaPane.setMaxSize(50, 50);
        areaPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        areaPane.setOnMouseClicked(_-> controller.handlePointPress(position));
        return areaPane;
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

    public void notifyComponentChange(Point point) {
        shipGrid.getChildren().stream()
                .filter(node -> node instanceof GuiComponent)
                .map(node -> (GuiComponent) node)
                .filter(node -> {
                    int col = GridPane.getColumnIndex(node) - 1;
                    int row = GridPane.getRowIndex(node) - 1;
                    return col + minX == point.x && row + minY == point.y;
                })
                .findFirst()
                .ifPresent(GuiComponent::notifyContentChange);
    }

    public void highlightPoints(Set<Point> points, Color color) {
        Platform.runLater(() -> {
            for (Point point : points) {
                getChildren().stream()
                        .filter(node -> node instanceof GuiComponent)
                        .map(node -> (GuiComponent) node)
                        .filter(node -> {
                            Integer col = GridPane.getColumnIndex(node);
                            Integer row = GridPane.getRowIndex(node);
                            if (col == null || row == null) return false;
                            col -= 1;
                            row -= 1;
                            return col + minX == point.x && row + minY == point.y;
                        })
                        .findFirst()
                        .ifPresent(component -> component.setHighlight(color));
            }
        });
    }

    public void clearHighlights() {
        Platform.runLater(() -> {
            getChildren().stream()
                    .filter(node -> node instanceof GuiComponent)
                    .map(node -> (GuiComponent) node)
                    .forEach(GuiComponent::clearHighlight);
        });
    }
}
