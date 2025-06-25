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

/**
 * GUI element representing a ship board in the game.
 * <p>
 * This class displays the ship's grid, including its components and empty areas, and allows for interaction with the ship's layout.
 * It is used to visually represent a player's ship and its current state in the GUI.
 * </p>
 */
public class GuiShipBoard extends PurpleVBox {
    private int minX;
    private int minY;
    private final GuiController controller;
    private final GridPane shipGrid;

    Image emptyAreaImage = null;
    /**
     * The path to the image used for empty areas of the ship.
     */
    public final static Path emptyAreaImagePath = Path.of("src/main/resources/textures/tiles/empty_area.png");

    /**
     * Constructs a GuiShipBoard for the given ship name, ship board model, and controller.
     *
     * @param name the name of the ship/player
     * @param shipBoard the ship board model to represent
     * @param controller the GUI controller handling actions
     */
    public GuiShipBoard(String name, ShipBoard shipBoard, GuiController controller) {
        super(5);

        this.controller = controller;

        setAlignment(Pos.CENTER);

        shipGrid = new GridPane();
        setShipGrid(shipBoard);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.WHITE);

        this.getChildren().addAll(nameLabel, shipGrid);
    }

    /**
     * Sets up the ship grid based on the current ship board state.
     *
     * @param shipBoard the ship board model to use for layout
     */
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
                        areaPane.setMaxSize(60, 60);
                        areaPane.setOnMouseClicked(_->controller.handlePointPress(currentPoint));
                    } else {
                        areaPane = getEmptyAreaPane(currentPoint);
                    }
                    shipGrid.add(areaPane, x + 1, y + 1);
                }
            }
        }
    }

    /**
     * Notifies the GUI to place a component at the specified point with the given orientation.
     * <p>
     * This method updates the ship grid to visually add the component at the given position.
     * It schedules the update on the JavaFX application thread.
     * </p>
     *
     * @param component the component to place
     * @param point the position on the ship grid
     * @param orientation the orientation of the component
     */
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
                guiComponent.setMaxSize(60, 60);
                guiComponent.setOnMouseClicked(_ -> controller.handlePointPress(point));
                shipGrid.add(guiComponent, GridPane.getColumnIndex(node), GridPane.getRowIndex(node));
            });
        });
    }

    /**
     * Notifies the GUI to remove a component from the specified position.
     * <p>
     * This method updates the ship grid to visually remove the component at the given position and replace it with an empty area.
     * It schedules the update on the JavaFX application thread.
     * </p>
     *
     * @param oldPosition the position from which to remove the component
     */
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
        freeAreaView.setFitWidth(60);
        freeAreaView.setFitHeight(60);

        areaPane.getChildren().add(freeAreaView);
        areaPane.setMaxSize(60, 60);
        areaPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        areaPane.setOnMouseClicked(_-> controller.handlePointPress(position));
        return areaPane;
    }

    /**
     * Notifies the GUI that a component's content has changed at the specified point.
     * <p>
     * This method finds the GuiComponent at the given point and calls its notifyContentChange method.
     * </p>
     * @param point the position of the component whose content has changed
     */
    public void notifyComponentChange(Point point) {
        Platform.runLater(()->{
            shipGrid.getChildren().stream()
                    .filter(node -> node instanceof GuiComponent)
                    .map(node -> (GuiComponent) node)
                    .filter(component -> {
                        Integer col = GridPane.getColumnIndex(component);
                        Integer row = GridPane.getRowIndex(component);
                        if (col == null || row == null) return false;
                        col -= 1;
                        row -= 1;
                        return col + minX == point.x && row + minY == point.y;
                    })
                    .findFirst()
                    .ifPresent(GuiComponent::notifyContentChange);
        });
    }

    /**
     * Highlights the specified points on the ship grid with the given color.
     * <p>
     * This method visually highlights the GuiComponents at the given points using the provided color.
     * </p>
     * @param points the set of points to highlight
     * @param color the color to use for highlighting
     */
    public void highlightPoints(Set<Point> points, Color color) {
        Platform.runLater(() -> {
            for (Point point : points) {
                shipGrid.getChildren().stream()
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

    /**
     * Clears all highlights from the ship grid.
     * <p>
     * This method removes any visual highlights from all GuiComponents on the ship grid.
     * </p>
     */
    public void clearHighlights() {
        Platform.runLater(() -> {
            shipGrid.getChildren().stream()
                    .filter(node -> node instanceof GuiComponent)
                    .map(node -> (GuiComponent) node)
                    .forEach(GuiComponent::clearHighlight);
        });
    }
}
