package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.PointPressHandler;
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

    public GuiAllShips(Player mainPlayer, Map<ShipBoard, Player> shipToPlayer, ControllerToServer controller, PointPressHandler pointPressHandler) {
        setSpacing(20);
        setPrefHeight(Region.USE_COMPUTED_SIZE);
        setPrefWidth(Region.USE_COMPUTED_SIZE);

        // Main player's large ship view
        ShipBoard mainBoard = shipToPlayer.entrySet().stream()
                .filter(entry -> entry.getValue().equals(mainPlayer))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Main player's ship not found"));

        GuiShipHandAndStash mainView = new GuiShipHandAndStash(mainBoard, controller, pointPressHandler);
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
