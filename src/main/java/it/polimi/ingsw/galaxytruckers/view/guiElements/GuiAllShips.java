package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GuiAllShips extends HBox {

    Map<ShipBoard, GuiShipHandAndStash> shipMap = new HashMap<>();

    public GuiAllShips(Player mainPlayer, Map<ShipBoard, Player> shipToPlayer, ControllerToServer controller) {
        setSpacing(20);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        setPrefWidth(Region.USE_COMPUTED_SIZE);

        // Main player's large ship view
        ShipBoard mainBoard = shipToPlayer.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mainPlayer))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Main player's ship not found"));

        GuiShipHandAndStash mainView = new GuiShipHandAndStash(mainBoard, controller);
        shipMap.put(mainBoard, mainView);
        mainView.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(mainView, Priority.ALWAYS);

        // VBox for other players
        VBox othersColumn = new VBox(10);
        othersColumn.setAlignment(Pos.CENTER);

        for (Map.Entry<ShipBoard, Player> entry : shipToPlayer.entrySet()) {
            if (!entry.getValue().equals(mainPlayer)) {
                GuiShipHandAndStash guiShipHandAndStash = new GuiShipHandAndStash(entry.getKey(), controller);
                shipMap.put(entry.getKey(), guiShipHandAndStash);
                othersColumn.getChildren().add(guiShipHandAndStash);
            }
        }

        // Style layout proportions
        mainView.setPrefWidth(2 * 300);     // Replace 300 with the estimated width of one view
        othersColumn.setPrefWidth(300);

        this.getChildren().addAll(mainView, othersColumn);
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, int componentId, Point point, Direction orientation) {
        shipMap.get(shipBoard).notifyPlaceComponent(componentId, point, orientation);
        notifyClearHand(shipBoard);
    }

    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        shipMap.get(shipBoard).notifyStash(component);
        notifyClearHand(shipBoard);
    }

    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        shipMap.get(shipBoard).notifyStash(component);
        notifyRemoveComponent(shipBoard, oldPosition);
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        shipMap.get(shipBoard).notifyGrabStashed(index);
        notifySetHand(shipBoard, component);
    }

    public void notifyClearHand(ShipBoard shipBoard) {
        shipMap.get(shipBoard).notifyClearHand();
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point oldPosition) {
        shipMap.get(shipBoard).notifyRemoveComponent(oldPosition);
    }

    public void notifySetHand(ShipBoard shipBoard, Component component) {
        shipMap.get(shipBoard).notifySetHand(component);
    }
}

//        HBox handBox = new HBox(10);
//        handBox.setAlignment(Pos.CENTER);
//
//        Map<FourColors, ObjectProperty<Component>> hand = model.getHand();
//
//        for (Map.Entry<FourColors, ObjectProperty<Component>> entry : hand.entrySet()) {
//            FourColors color = entry.getKey();
//            ObjectProperty<Component> componentProperty = entry.getValue();
//
//            VBox cardBox = new VBox(5);
//            cardBox.setAlignment(Pos.CENTER);
//
//            // HBox for circle + text side-by-side
//            HBox colorBox = new HBox(5);
//            colorBox.setAlignment(Pos.CENTER);
//
//            // Circle with color
//            Circle colorCircle = new Circle(20, color.getJfxColor());
//            colorBox.getChildren().add(colorCircle);
//
//            // Text next to circle with player name from inverse map
//            String playerName = model.getPlayerToColor().inverse().get(color);
//            Text playerText = new Text(playerName);
//            colorBox.getChildren().add(playerText);
//
//            cardBox.getChildren().add(colorBox);
//
//            // Placeholder for the component Node
//            StackPane componentContainer = new StackPane();
//            componentContainer.setPrefSize(60, 60); // Or adjust size as needed
//            // Listener to update on property change
//            componentProperty.addListener((obs, oldVal, newVal) -> {
//                Platform.runLater(() -> {
//                    componentContainer.getChildren().setAll(
//                            newVal != null
//                                    ? new GuiComponent(model, controller, newVal).getNode()
//                                    : new Label("?"));
//                });
//            });
//
//            // Initial content
//            GuiComponent initial = new GuiComponent(model, controller, componentProperty.get());
//            componentContainer.getChildren().add(initial != null ? initial.getNode() : new Label("?"));
//
//            GuiShipBoard shipBoard = new GuiShipBoard(model, controller, color);
//
//            handBox.getChildren().add(cardBox);
//            cardBox.getChildren().add(componentContainer);
//            cardBox.getChildren().add(shipBoard.getNode());
//        }
//
//        return handBox;
