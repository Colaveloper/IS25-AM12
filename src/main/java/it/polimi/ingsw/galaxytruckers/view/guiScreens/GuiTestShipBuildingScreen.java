package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import it.polimi.ingsw.galaxytruckers.view.model.state.TestShipBuildingState;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.Map;

public class GuiTestShipBuildingScreen extends GuiShipBuildingScreen {

    public GuiTestShipBuildingScreen(ClientModel model, ControllerToServer controller, TestShipBuildingState state) {
        super(model, controller, state);
    }

    @Override
    protected VBox getFullShip(ShipBoard shipBoard) {
        VBox layout = new VBox(5);
        layout.setAlignment(Pos.CENTER);

        layout.getChildren().add(guiShipBoards.get(shipBoard));

        HBox handBox = new HBox();
        handBox.getChildren().add(guiHands.get(shipBoard));
        layout.getChildren().add(handBox);

        Player player = null;
        for (Map.Entry<ShipBoard, Player> entry : model.getShipToPlayer().entrySet()) {
            if (entry.getKey().equals(shipBoard)) {
                player = entry.getValue();
                break;
            }
        }

        if (player != null) {
            Label nicknameLabel = new Label(player.getNickname());
            nicknameLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;"
            );
            layout.getChildren().add(nicknameLabel);
        }

        return layout;
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox(20); // Added spacing between components
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
            getStyledFlightBoard(), // Place the styled flightboard at the top
            guiComponentBank,
            getGuiAllShips()
        );
        return layout;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void placeShipOnFlightboard(int position) {
                if (state.getAvailableActions().contains(StateActions.PLACE_SHIP_FOR_TEST)) {
                    controller.placeShipOnFlightBoard();
                }
            }

            @Override
            public void requestRandComponent() {
                if (state.getAvailableActions().contains(StateActions.REQUEST_RAND_COMPONENT)) {
                    controller.requestRandComponent();
                }
            }

            @Override
            public void rejectComponent() {
                if (state.getAvailableActions().contains(StateActions.REJECT_COMPONENT)) {
                    controller.rejectComponent();
                }
            }

            @Override
            public void requestComponent(int id) {
                if (state.getAvailableActions().contains(StateActions.REQUEST_COMPONENT)) {
                    controller.requestComponent(id);
                }
            }

            @Override
            public void rotateHandComponent() {
                if (model.getMyShip().getLastComponent() != null) {
                    Direction currentDirection = model.getMyShip().getLastComponent().getOrientation();
                    Direction newDirection = currentDirection.getLeft();
                    model.getMyShip().getLastComponent().setOrientation(newDirection);
                    guiHands.get(model.getMyShip()).rotateComponent(newDirection.getAngle());
                }
            }

            @Override
            public void handlePointPress(Point point) {
                // clicking on a component that has been placed on the ship
                if (myShipBoard.getComponentMap().containsKey(point)) {
                    // we grab the component only if it hasn't been welded
                    if (myShipBoard.getLastPosition() != null &&
                            myShipBoard.getLastPosition().equals(point) &&
                            state.getAvailableActions().contains(StateActions.GRAB_PLACED_COMPONENT)) {
                        Component component = myShipBoard.getComponentMap().get(point);
                        controller.grabPlacedComponent();
                        guiShipBoards.get(myShipBoard).notifyRemoveComponent(point);
                        guiHands.get(myShipBoard).notifySetHand(component);
                    }
                }
                // place the component on an empty space on the ship
                else if (myShipBoard.getShipArea().contains(point) &&
                        !myShipBoard.getComponentMap().containsKey(point) &&
                        myShipBoard.getLastComponent() != null &&
                        myShipBoard.getLastPosition() == null) {
                    if (state.getAvailableActions().contains(StateActions.PLACE_COMPONENT)) {
                        Direction orientation = myShipBoard.getLastComponent().getOrientation();
                        controller.placeComponent(point, orientation);
                    }
                }
            }
        };
    }
}
