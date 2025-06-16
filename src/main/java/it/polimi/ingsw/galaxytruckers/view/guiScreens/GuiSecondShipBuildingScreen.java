package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiElements.*;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiHourglass;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiSecondShipBuildingScreen extends GuiGameScreen {

    private final GuiComponentBank guiComponentBank;
    private final GuiForecast guiForecast;
    private final GuiHourglass guiHourglass;
    protected final Map<ShipBoard, GuiHand> guiHands;
    protected final Map<ShipBoard, GuiStash> guiStashes;
    private Direction lastComponentDirection;

    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState state) {
        super(model, controller, state);
        guiComponentBank = new GuiComponentBank(state.getComponentBank(), getGuiController());
        guiForecast = new GuiForecast(state.getBlockedForecasts(), getGuiController());
        guiHourglass = new GuiHourglass(getGuiController());
        guiHands = new HashMap<>();
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            guiHands.put(shipBoard, new GuiHand(
                    shipBoard.getLastPosition()==null ? shipBoard.getLastComponent() : null,
                    getGuiController()
            ));
        }
        guiStashes = new HashMap<>();
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            guiStashes.put(shipBoard, new GuiStash(shipBoard.getStashedComponents(), getGuiController()));
        }
        resetLastComponentDirection();
    }

    @Override
    protected VBox getFullShip(ShipBoard shipBoard) {
        VBox layout = new VBox();
        HBox handAndStashBox = new HBox();
        handAndStashBox.getChildren().addAll(guiHands.get(shipBoard), guiStashes.get(shipBoard));
        layout.getChildren().addAll(guiShipBoards.get(shipBoard), handAndStashBox);
        return layout;
    }


    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(guiComponentBank, guiForecast, guiHourglass, guiFlightBoard, getAllShips());
        return layout;
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        resetLastComponentDirection();
        guiComponentBank.notifyRequestRandComponent();
        guiHands.get(shipBoard).notifySetHand(component);
    }


    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        resetLastComponentDirection();
        guiComponentBank.notifyRequestComponent(component);
        guiHands.get(shipBoard).notifySetHand(component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiHands.get(shipBoard).notifyClearHand();
        resetLastComponentDirection();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
        resetLastComponentDirection();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRejectComponent(component);
        guiHands.get(shipBoard).notifyClearHand();
        resetLastComponentDirection();

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiComponentBank.notifyRejectComponent(component);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
        resetLastComponentDirection();
    }
    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        resetLastComponentDirection();
        guiStashes.get(shipBoard).notifyGrab(index);
        guiHands.get(shipBoard).notifySetHand(component);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        int placedComponentId = shipBoard.getComponentMap().get(point).getId();
        guiShipBoards.get(shipBoard).notifyPlaceComponent(placedComponentId, point, orientation);
        guiHands.get(shipBoard).notifyClearHand();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation, Point oldPosition) {
        int placedComponentId = shipBoard.getComponentMap().get(point).getId();
        guiShipBoards.get(shipBoard).notifyPlaceComponent(placedComponentId, point, orientation);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard)
    {
        guiHourglass.notifyFlipHourglass();
    }

    @Override
    public void notifyHourglassEnd() {
        guiHourglass.notifyHourglassEnd();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        guiFlightBoard.notifyFlightBoardPosition(shipBoard, position);
    }
    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        guiForecast.notifyPeekForecast(shipBoard, deckIndex);
    }
    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        throw new RuntimeException("NOOOOOOOOO");
    }
    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        guiForecast.notifyReleaseForecast(shipBoard, deckIndex);
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyRemoveComponent(point);
    }

    private void resetLastComponentDirection() {
        lastComponentDirection = Direction.UP;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void placeShipOnFlightboard(int position) {
                if (state.getAvailableActions().contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD)) {
                    controller.placeShipOnFlightboard(position);
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
                    lastComponentDirection = lastComponentDirection.getLeft();
                    guiHands.get(model.getMyShip()).rotateComponent(lastComponentDirection.getAngle());
                }
            }

            @Override
            public void flipHourglass() {
                if (state.getAvailableActions().contains(StateActions.FLIP_HOURGLASS)) {
                    controller.flipHourglass();
                }
            }

            @Override
            public void stashComponent() {
                if (state.getAvailableActions().contains(StateActions.STASH_COMPONENT)) {
                    controller.stashComponent();
                }
            }

            @Override
            public void grabStashedComponent(int i) {
                if (state.getAvailableActions().contains(StateActions.GRAB_STASHED_COMPONENT)) {
                    controller.grabStashedComponent(i);
                }
            }

            @Override
            public void handlePointPress(Point point) {
                if (
                        model.getMyShip().getLastPosition() != null
                        && model.getMyShip().getLastPosition().equals(point)
                ) {
                    lastComponentDirection = lastComponentDirection.getLeft();
                    guiShipBoards.get(model.getMyShip()).getGuiComponent(point).setRotate(lastComponentDirection.getAngle());
                } else if (
                        model.getMyShip().getShipArea().contains(point)
                        && !model.getMyShip().getComponentMap().containsKey(point)
                ) {
                    if (state.getAvailableActions().contains(StateActions.PLACE_COMPONENT)) {
                        controller.placeComponent(point, lastComponentDirection);
                    }
                }
            }

            @Override
            public void acquireForecast(int finalI) {
                if (state.getAvailableActions().contains(StateActions.ACQUIRE_FORECAST)) {
                    controller.acquireForecast(finalI);
                }
            }

            @Override
            public void releaseForecast() {
                if (state.getAvailableActions().contains(StateActions.RELEASE_FORECAST)) {
                    controller.releaseForecast();
                }
            }
        };
    }
}

