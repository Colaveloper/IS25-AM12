package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.RemoveGoodsState;
import it.polimi.ingsw.galaxytruckers.client.model.state.StateActions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Set;

/**
 * GUI screen for handling the loss of goods during adverse game events.
 * This screen allows players to select cargo holds and choose which goods
 * to discard. Players potentially lose batteries if they have no goods to lose.
 */
public class GuiLoseGoodsScreen extends GuiAdventureScreen {
    private final ObjectProperty<Point> selectedPoint;
    private VBox cargoInfoBox = new VBox(2);

    /**
     * Constructs a new lose goods screen.
     *
     * @param model            The client model containing game state
     * @param controller       The controller for communicating with the server
     * @param removeGoodsState The state containing valid actions for this phase
     */
    public GuiLoseGoodsScreen(ClientModel model, ClientControllerInterface controller, RemoveGoodsState removeGoodsState) {
        super(model, controller, removeGoodsState);
        this.selectedPoint = new SimpleObjectProperty<>();
        ObjectProperty<GoodsType> selectedGoodsType = new SimpleObjectProperty<>();
        updateCargoInfoBox();
        if (isMyTurn()) {
            guiLog.log("Your turn to lose goods");
            guiLog.log("Select a cargo hold to remove goods from");
            guiLog.log("Select batteries to use if you have no goods to lose");
            setupButtonBox();
        } else {
            guiLog.log("Wait for others to lose goods");
        }
    }

    /**
     * Sets up the button box with goods type selection and action buttons.
     * This is only done when it's the current player's turn.
     */
    private void setupButtonBox() {
        guiButtonBox.getChildren().clear();

        VBox actionButtons = new VBox(5);
        actionButtons.setPadding(new Insets(5));

        Button loseGoodOrBatteryButton = new Button("Lose Good/Battery");
        // disable the button if there are no goods or batteries to lose
        loseGoodOrBatteryButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (selectedPoint.get() == null ||
                                (!myShipBoard.getCargoHolds().containsKey(selectedPoint.get()) ||
                                        myShipBoard.getCargoHolds().get(selectedPoint.get()).getGoods().isEmpty()) &&
                                myShipBoard.getNumBatteries() <= 0),
                        selectedPoint
                )
        );

        loseGoodOrBatteryButton.setOnAction(_ ->{
            guiShipBoards.get(myShipBoard).clearHighlights();
            controller.loseGoods(selectedPoint.get());
            selectedPoint.set(null);
        });

        Button nextButton = new Button("Done");
        nextButton.setOnAction(_ -> getGuiController().goNext());

        actionButtons.getChildren().addAll(loseGoodOrBatteryButton, nextButton);
        guiButtonBox.getChildren().add(actionButtons);
    }

    /**
     * {@inheritDoc}
     * Creates and returns a GUI controller that handles point clicks for selecting
     * cargo holds and navigation to the next screen.
     *
     * @return A GUI controller for this screen
     */
    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (state.getAvailableActions().contains(StateActions.LOSE_GOOD)) {
                    if (myShipBoard.getCargoHolds().containsKey(point) || myShipBoard.getBatteries().containsKey(point)) {
                        guiShipBoards.get(myShipBoard).clearHighlights();
                        selectedPoint.set(point);
                        updateCargoInfoBox();
                        guiLog.log("Action to be performed at "+ point.x + "," + point.y);
                        guiShipBoards.get(myShipBoard).highlightPoints(Set.of(point), Color.YELLOW);
                    } else {
                        guiLog.log("No cargo hold or battery at " + point.x + "," + point.y);
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

    /**
     * Updates the cargo information box with details about the selected cargo hold.
     * Shows the position and contents of the selected cargo hold.
     */
    private void updateCargoInfoBox() {
        Platform.runLater(() -> {
            cargoInfoBox.getChildren().clear();
            Label cargoHoldsLabel = new Label("Cargo holds with goods: " + countCargoHoldsWithGoods());
            cargoHoldsLabel.setTextFill(Color.WHITE);
            Label batteriesLabel = new Label("Batteries available: " + countBatteries());
            batteriesLabel.setTextFill(Color.WHITE);
            cargoInfoBox.getChildren().add(cargoHoldsLabel);
            cargoInfoBox.getChildren().add(batteriesLabel);

            if (selectedPoint.get() != null && myShipBoard.getCargoHolds().containsKey(selectedPoint.get())) {
                CargoHold cargoHold = myShipBoard.getCargoHolds().get(selectedPoint.get());
                Label positionLabel = new Label("Cargo hold at: (" + selectedPoint.get().x + "," + selectedPoint.get().y + ")");
                positionLabel.setTextFill(Color.WHITE);
                cargoInfoBox.getChildren().add(positionLabel);

                for (GoodsType type : cargoHold.getGoods().keySet()) {
                    int amount = cargoHold.getGoods().getOrDefault(type, 0);
                    if (amount > 0) {
                        Label goodsLabel = new Label(type + ": " + amount);
                        goodsLabel.setTextFill(Color.WHITE);
                        cargoInfoBox.getChildren().add(goodsLabel);
                    }
                }
            }

            if (selectedPoint.get() != null && myShipBoard.getBatteries().containsKey(selectedPoint.get())) {
                Label batteryLabel = new Label("Battery at: (" + selectedPoint.get().x + "," + selectedPoint.get().y + ")");
                batteryLabel.setTextFill(Color.WHITE);
                cargoInfoBox.getChildren().add(batteryLabel);
            }
        });
    }
    /**
     * {@inheritDoc}
     * Creates and returns a VBox containing the cargo information display
     * along with the standard free-use area.
     *
     * @return A VBox containing the free-use area with cargo information
     */
    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = super.getFreeUseVBox();

        if (isMyTurn()) {
            freeUseVBox.getChildren().add(cargoInfoBox);
        }

        return freeUseVBox;
    }

    /**
     * Counts the total number of batteries available on the player's ship.
     *
     * @return The total number of batteries available
     */
    private int countBatteries() {
        int totalBatteries = 0;
        for (Point point : myShipBoard.getBatteries().keySet()) {
            totalBatteries += myShipBoard.getBatteries().get(point).getNumBatteries();
        }
        return totalBatteries;
    }

    /**
     * Counts the number of cargo holds that contain goods on the current ship.
     *
     * @return The number of cargo holds containing goods
     */
    private int countCargoHoldsWithGoods() {
        ShipBoard currentShip = state.getShipBoard();
        int count = 0;
        for (CargoHold cargoHold : currentShip.getCargoHolds().values()) {
            if (!cargoHold.getGoods().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     * Updates the GUI when a component changes on the ship board,
     * specifically refreshing the cargo information display.
     *
     * @param shipBoard The ship board containing the changed component
     * @param point The position of the changed component
     */
    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        super.notifyComponentChange(shipBoard, point);
        guiShipBoards.get(myShipBoard).clearHighlights();
        updateCargoInfoBox();
    }

    /**
     * {@inheritDoc}
     *
     * @param shipBoard The ship board containing the changed component
     * @param point The position of the changed component
     */
    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        super.notifyUseBattery(shipBoard, point);
        notifyComponentChange(shipBoard, point);
        guiShipBoards.get(myShipBoard).clearHighlights();
        updateCargoInfoBox();
    }
}
