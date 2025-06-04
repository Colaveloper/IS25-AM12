package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GuiShipHandAndStash extends VBox {

    public GuiShipHandAndStash(ShipBoard shipBoard, ControllerToServer controller) {
        Rectangle placeholder = new Rectangle(300, 300); // Adjust size as needed
        placeholder.setFill(Color.BLACK);
        this.getChildren().add(placeholder);
    }
}

//    @Override
//    public Node getNode() {
//        List<List<ObjectProperty<Component>>> ship = model.getShips().get(color);
//        Point upLeft = model.getUpLeft();
//        int height = ship.size();
//        int width = ship.getFirst().size();
//
//        GridPane grid = new GridPane();
//        grid.setHgap(0);
//        grid.setVgap(0);
//
//        // Components in the grid
//        for (int row = 0; row < height; row++) {
//            for (int col = 0; col < width; col++) {
//                ObjectProperty<Component> prop = ship.get(row).get(col);
//                StackPane cell = new StackPane();
//                cell.setPrefSize(50, 50); // adjusted size
//
//                // Initial content
//                Component comp = prop.get();
//                Node node = (comp != null) ? new GuiComponent(model, controller, comp).getNode() : new Label("?");
//
//                cell.getChildren().add(node);
//
//                // Update on change
//                prop.addListener((obs, oldVal, newVal) -> {
//                    Platform.runLater(() -> {
//                        cell.getChildren().setAll(
//                                newVal != null
//                                        ? new GuiComponent(model, controller, newVal).getNode()
//                                        : new Label("?"));
//                    });
//                });
//
//                grid.add(cell, col + 1, row + 1); // shift right and down to make room for labels
//            }
//        }
//
//        // Y-axis labels (row indices)
//        for (int row = 0; row < height; row++) {
//            Label yLabel = new Label(String.valueOf(upLeft.y + row));
//            yLabel.setMinSize(25, 50);  // changed from 30x60 to 25x50
//            yLabel.setAlignment(Pos.CENTER_RIGHT);
//            grid.add(yLabel, 0, row + 1);
//        }
//
//        // X-axis labels (column indices)
//        for (int col = 0; col < width; col++) {
//            Label xLabel = new Label(String.valueOf(upLeft.x + col));
//            xLabel.setMinSize(50, 25); // changed from 60x30 to 50x25
//            xLabel.setAlignment(Pos.TOP_CENTER);
//            grid.add(xLabel, col + 1, 0);
//        }
//
//        // Add corner label (empty)
//        Label corner = new Label();
//        corner.setMinSize(25, 25);  // changed from 30x30 to 25x25
//        grid.add(corner, 0, 0);
//
//        return grid;
//    }
//
//}
