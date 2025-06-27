package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.CircularToggleButton;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipInitializationState;
import it.polimi.ingsw.galaxytruckers.client.model.state.StateActions;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * GUI screen for initializing crew members on the ship board.
 * This screen allows players to place different types of alien crew members
 * in appropriate cabins on their ship board during the initialization phase.
 */
public class GuiCrewInitializationScreen extends GuiGameScreen {
    private final ObjectProperty<CrewType> currentCrewType;
    private final Map<CrewType, Set<Point>> crewTypeToPoints;

    /**
     * Constructs a new crew initialization screen.
     *
     * @param model The client model containing game state
     * @param controller The controller for communicating with the server
     * @param state The ship initialization state containing valid crew placements
     */
    public GuiCrewInitializationScreen(ClientModel model, ClientControllerInterface controller, ShipInitializationState state) {
        super(model, controller, state);
        currentCrewType = new SimpleObjectProperty<>(null);
        crewTypeToPoints = state.getCrewtypeToPoints().getOrDefault(myShipBoard, new HashMap<>());

        HBox crewButtons = new HBox();

        if (crewTypeToPoints.containsKey(CrewType.PURPLE)) {
            CircularToggleButton purpleAlienButton = new CircularToggleButton(Color.PURPLE);
            purpleAlienButton.setOnAction(_ -> currentCrewType.set(CrewType.PURPLE));
            purpleAlienButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                    ()-> currentCrewType.get() != null && currentCrewType.get() == CrewType.PURPLE,
                    currentCrewType
            ));
            crewButtons.getChildren().add(purpleAlienButton);
        }
        if (crewTypeToPoints.containsKey(CrewType.BROWN)) {
            CircularToggleButton brownAlienButton = new CircularToggleButton(Color.BROWN);
            brownAlienButton.setOnAction(_ -> currentCrewType.set(CrewType.BROWN));
            brownAlienButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                    ()-> currentCrewType.get() != null && currentCrewType.get() == CrewType.BROWN,
                    currentCrewType
            ));
            crewButtons.getChildren().add(brownAlienButton);
        }

        Button okButton = new Button("OK");
        okButton.setOnAction(_->getGuiController().goNext());

        guiButtonBox.getChildren().addAll(crewButtons, okButton);
    }

    /**
     * {@inheritDoc}
     * Returns an empty VBox since this screen doesn't use the free use area.
     *
     * @return An empty VBox
     */
    @Override
    protected VBox getFreeUseVBox() {
        return new VBox();
    }

    /**
     * {@inheritDoc}
     * Creates and returns a VBox containing the ship board GUI elements.
     *
     * @param shipBoard The ship board to display
     * @return A VBox containing the ship board GUI elements
     */
    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);
        getShipBoardVBox.getChildren().addAll(guiShipBoards.get(shipBoard));
        return getShipBoardVBox;
    }

    /**
     * {@inheritDoc}
     * Updates the GUI when a cabin is initialized with crew members.
     *
     * @param shipBoard The ship board containing the initialized cabin
     * @param point The position of the initialized cabin
     * @param crewType The type of crew placed in the cabin
     * @param numResidents The number of crew members in the cabin
     */
    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
        guiStatBox.notifyChange();
    }

    /**
     * {@inheritDoc}
     * Returns a GUI controller that handles point clicks for placing crew members
     * and navigation to the next screen.
     *
     * @return A GUI controller for this screen
     */
    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            /**
             * Handles a click on a point on the ship board.
             * Validates if the selected crew type can be placed at the clicked position
             * and sends the appropriate command to the server if valid.
             *
             * @param point The point on the ship board that was clicked
             */
            @Override
            public void handlePointPress(Point point) {
                if (currentCrewType.get() != null) {
                    if (
                            !myShipBoard.getCabins().containsKey(point)
                    ) {
                        guiLog.log("Cannot place an alien outside of a cabin");
                    } else if (
                            !crewTypeToPoints.get(currentCrewType.get()).contains(point)
                    ) {
                        guiLog.log("Cabin not appropriate for a " + currentCrewType + " alien");
                    } else {
                        if (state.getAvailableActions().contains(StateActions.INITIALIZE_CABIN)) {
                            controller.initializeCabin(point, currentCrewType.get());
                        }
                    }
                }
            }

            @Override
            public void goNext() {
                if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }
        };
    }
}
