package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiElements.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiSecondShipBuildingScreen extends GuiShipBuildingScreen {

    private final GuiForecast guiForecast;
    private final GuiHourglass guiHourglass;
    protected final Map<ShipBoard, GuiStash> guiStashes;

    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState state) {
        super(model, controller, state);
        guiForecast = new GuiForecast(state.getBlockedForecasts(), getGuiController());
        guiHourglass = new GuiHourglass(getGuiController());
        guiStashes = new HashMap<>();
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            guiStashes.put(shipBoard, new GuiStash(shipBoard.getStashedComponents(), getGuiController()));
        }
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);

        PurpleVBox shipBoardVBox = guiShipBoards.get(shipBoard);

        HBox handAndStashBox = new HBox();
        handAndStashBox.getChildren().addAll(guiHands.get(shipBoard), guiStashes.get(shipBoard));

        getShipBoardVBox.getChildren().addAll(shipBoardVBox, handAndStashBox);

        return getShipBoardVBox;
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = new VBox(5);
        freeUseVBox.getChildren().addAll(guiForecast, guiComponentBank);
        return freeUseVBox;
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiHands.get(shipBoard).notifyClearHand();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        guiStashes.get(shipBoard).notifyGrab(index);
        guiHands.get(shipBoard).notifySetHand(component);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        guiHourglass.notifyFlipHourglass();
    }

    @Override
    public void notifyHourglassEnd() {
        guiHourglass.notifyHourglassEnd();
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
        guiForecast.notifyReleaseForecast(deckIndex);
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
                if (myShipBoard.getLastComponent() != null) {
                    Direction currentDirection = myShipBoard.getLastComponent().getOrientation();
                    Direction newDirection = currentDirection.getLeft();
                    myShipBoard.getLastComponent().setOrientation(newDirection);
                    guiHands.get(myShipBoard).setComponentDirection(newDirection);
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
                        controller.placeComponent(point, myShipBoard.getLastComponent().getOrientation());
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
