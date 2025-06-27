package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.PurpleVBox;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.StateActions;
import it.polimi.ingsw.galaxytruckers.client.model.state.TestShipBuildingState;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.awt.*;

/**
 * GUI screen for the test ship building phase of the game.
 * <p>
 * This screen manages the UI and logic for ship building in the test flight, allowing players to
 * interact with their ship board and hand. It extends GuiShipBuildingScreen and customizes the
 * layout for the test phase, providing a simplified interface for testing ship construction.
 * </p>
 *
 * @author (your name or team)
 */
public class GuiTestShipBuildingScreen extends GuiShipBuildingScreen {

    /**
     * Constructs a new GuiTestShipBuildingScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param state the test ship building state
     */
    public GuiTestShipBuildingScreen(ClientModel model, ClientControllerInterface controller, TestShipBuildingState state) {
        super(model, controller, state);
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);

        PurpleVBox shipBoardVBox = guiShipBoards.get(shipBoard);

        getShipBoardVBox.getChildren().addAll(shipBoardVBox, guiHands.get(shipBoard));

        return getShipBoardVBox;
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = new VBox(5);
        freeUseVBox.getChildren().add(guiComponentBank);
        return freeUseVBox;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void placeShipOnFlightBoard(int position) {
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
                if (myShipBoard.getLastComponent() != null) {
                    Direction currentDirection = myShipBoard.getLastComponent().getOrientation();
                    Direction newDirection = currentDirection.getLeft();
                    myShipBoard.getLastComponent().setOrientation(newDirection);
                    guiHands.get(myShipBoard).setComponentDirection(newDirection);
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
        };
    }
}
