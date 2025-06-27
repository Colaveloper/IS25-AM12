package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.client.model.state.StateActions;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Set;

/**
 * Screen for the component activation phase of the game.
 * This screen allows players to activate components on their ship by spending batteries.
 * Players can click on components to activate them or on batteries to spend them.
 * The screen provides visual feedback about the activation state and controls to complete the activation phase.
 */
public abstract class GuiActivationScreen extends GuiAdventureScreen {
    private final IntegerProperty batteriesToSpend;

    /**
     * Constructs a GuiActivationScreen with the specified model, controller, and game state.
     * Initializes the battery counter and sets up the UI elements for the activation phase.
     * If it's the player's turn, a "FINISH ACTIVATION" button is displayed, which is enabled
     * only when all required batteries have been spent.
     *
     * @param model The client model containing all game data
     * @param controller The controller for sending commands to the server
     * @param gameState The current adventure state
     */
    public GuiActivationScreen(ClientModel model, ClientControllerInterface controller, AdventureState gameState) {
        super(model, controller, gameState);
        batteriesToSpend = new SimpleIntegerProperty(0);
        if (isMyTurn()) {
            Button goNextButton = new Button("FINISH ACTIVATION");
            goNextButton.setOnAction(_ -> getGuiController().goNext());
            goNextButton.disableProperty().bind(
                    Bindings.createBooleanBinding(() -> (batteriesToSpend.get() != 0), batteriesToSpend)
            );
            goNextButton.setVisible(true);
            guiButtonBox.getChildren().add(goNextButton);
            guiLog.log("Spend the batteries you want to activate components, then press OK");
        } else {
            guiLog.log("Wait for your turn");
        }
    }

    /**
     * Provides a custom GuiController implementation for the activation phase.
     * This controller handles clicks on components and batteries, allowing players to
     * activate components or spend batteries based on the current game state.
     *
     * @return A GuiController implementation for handling activation phase interactions
     */
    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (isMyTurn()) {
                    if (model.getMyShip().getActivatables().containsKey(point)) {
                        if (!model.getMyShip().getActivatables().get(point).isActive()) {
                            if (state.getAvailableActions().contains(StateActions.ACTIVATE_COMPONENT)) {
                                controller.activateComponent(point);
                            }
                        } else {
                            guiLog.log("This component is already active");
                        }
                    } else if (model.getMyShip().getBatteries().containsKey(point)) {
                        if (model.getMyShip().getBatteries().get(point).getNumBatteries() > 0) {
                            if (state.getAvailableActions().contains(StateActions.SPEND_BATTERIES)) {
                                controller.useBattery(point);
                            }
                        } else {
                            guiLog.log("Out of batteries at this position");
                        }
                    }
                }
            }

            @Override
            public void goNext() {
                if (isMyTurn()) {
                    if(batteriesToSpend.get() > 0) {
                        guiLog.log("You need to use " + batteriesToSpend.get() + " batteries");
                    } else if(batteriesToSpend.get() < 0) {
                        guiLog.log("You need to activate " + (batteriesToSpend.get() * (-1)) + " components");
                    } else {
                        if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                            controller.goNext();
                        }
                    }
                }
            }
        };
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        batteriesToSpend.add(-1);
        guiShipBoards.get(shipBoard).highlightPoints(Set.of(point), Color.BLUE);
        guiStatBox.notifyChange();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        batteriesToSpend.add(1);
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
        guiShipBoards.get(shipBoard).highlightPoints(Set.of(point), Color.GREEN);
        guiStatBox.notifyChange();
    }
}
