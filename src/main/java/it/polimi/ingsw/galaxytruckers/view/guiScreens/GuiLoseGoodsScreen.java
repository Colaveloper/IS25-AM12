package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.guiElements.PurpleVBox;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.awt.*;

/**
 * GUI screen for handling the loss of goods during adverse game events.
 * This screen allows players to select cargo holds and choose which goods
 * to discard. Players potentially lose batteries if they have no goods to lose.
 */
public class GuiLoseGoodsScreen extends GuiAdventureScreen {
    private final ObjectProperty<Point> selectedPoint;
    private final ObjectProperty<GoodsType> selectedGoodsType;
    private VBox cargoInfoBox = new VBox(2);

    /**
     * Constructs a new lose goods screen.
     *
     * @param model            The client model containing game state
     * @param controller       The controller for communicating with the server
     * @param removeGoodsState The state containing valid actions for this phase
     */
    public GuiLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState removeGoodsState) {
        super(model, controller, removeGoodsState);
        this.selectedPoint = new SimpleObjectProperty<>();
        this.selectedGoodsType = new SimpleObjectProperty<>();
        if (isMyTurn()) {
            guiLog.log("Your turn to lose goods");
            guiLog.log("Select a cargo hold to remove goods from");
            guiLog.log("Select a goods type to remove");
            guiLog.log("Then select remove action");
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
        if (isMyTurn()) {
            guiButtonBox.getChildren().clear();
            guiButtonBox.getChildren().add(getButtonsBox());
            updateButtonsState();
        }
    }

    /**
     * Updates the state of buttons based on whether the player has goods to lose.
     * If no goods are available to lose, informs the player they can proceed.
     */
    private void updateButtonsState() {
        boolean hasGoodsToLose = countCargoHoldsWithGoods() > 0;
        if (!hasGoodsToLose) {
            guiLog.log("No goods to lose. You can proceed to the next screen (batteries will be used if available).");
        }
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
                if (state.getAvailableActions().contains(StateActions.REMOVE_GOOD)) {
                    if (myShipBoard.getCargoHolds().containsKey(point)) {
                        selectedPoint.set(point);
                        guiLog.log("Action to be performed at "+ point.x + "," + point.y);
                    } else {
                        guiLog.log("No cargo hold at " + point.x + "," + point.y);
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
     * {@inheritDoc}
     * Creates and returns a VBox containing the cargo information display
     * along with the standard free-use area.
     *
     * @return A VBox containing the free-use area with cargo information
     */
    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = super.getFreeUseVBox();
        freeUseVBox.setAlignment(Pos.CENTER);

        if (isMyTurn()) {
            updateCargoInfoBox();
            freeUseVBox.getChildren().add(cargoInfoBox);
        }

        return freeUseVBox;
    }

    /**
     * {@inheritDoc}
     * Creates and returns a VBox containing the ship board for a specific ship.
     *
     * @param shipBoard The ship board to display
     * @return A VBox containing the ship board
     */
    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox shipBoardVBox = new VBox(5);
        shipBoardVBox.setAlignment(Pos.CENTER);

        PurpleVBox guiShipBoardBox = guiShipBoards.get(shipBoard);
        shipBoardVBox.getChildren().add(guiShipBoardBox);

        return shipBoardVBox;
    }

    /**
     * Updates the cargo information box with details about the selected cargo hold.
     * Shows the position and contents of the selected cargo hold.
     */
    private void updateCargoInfoBox() {
        cargoInfoBox.getChildren().clear();
        cargoInfoBox.getChildren().add(new Label("Cargo holds with goods: " + countCargoHoldsWithGoods()));

        if (selectedPoint.get() != null && myShipBoard.getCargoHolds().containsKey(selectedPoint.get())) {
            CargoHold cargoHold = myShipBoard.getCargoHolds().get(selectedPoint.get());
            Label positionLabel = new Label("Selected position: (" + selectedPoint.get().x + "," + selectedPoint.get().y + ")");
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
    }

    /**
     * Creates and returns an HBox containing goods type selection buttons
     * and action buttons for removing goods or proceeding to the next phase.
     *
     * @return An HBox containing goods management buttons
     */
    private HBox getButtonsBox() {
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(10));
        buttonsBox.setAlignment(Pos.CENTER);

        VBox goodsTypeBox = new VBox(5);
        goodsTypeBox.setPadding(new Insets(5));
        Label selectGoodsLabel = new Label("Select goods type:");
        selectGoodsLabel.setTextFill(Color.WHITE);
        goodsTypeBox.getChildren().add(selectGoodsLabel);

        for (GoodsType type : GoodsType.values()) {
            Button typeButton = new Button(type.toString());
            typeButton.setOnAction(_ -> {
                guiLog.log("The action will be performed on a "+type+" good");
                selectedGoodsType.set(type);
            });
            Background defaultBackground = new Background(new BackgroundFill(
                    Color.LIGHTGRAY, new CornerRadii(5.0), null
            ));
            Background focusedBackground = new Background(new BackgroundFill(
                    switch (type) {
                        case RED -> Color.RED;
                        case BLUE -> Color.BLUE;
                        case GREEN -> Color.GREEN;
                        case YELLOW -> Color.GOLD;
                    }, new CornerRadii(5.0), null));
            typeButton.backgroundProperty().bind(
                Bindings.createObjectBinding(
                        () -> (selectedGoodsType.get() == type
                                ? focusedBackground
                                : defaultBackground
                        ), selectedGoodsType
                )
            );
            goodsTypeBox.getChildren().add(typeButton);
        }

        VBox actionButtons = new VBox(5);
        actionButtons.setPadding(new Insets(5));

        Button removeButton = new Button("Lose Good");
        removeButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (
                                selectedPoint.get() == null ||
                                selectedGoodsType.get() == null ||
                                !myShipBoard.getCargoHolds().containsKey(selectedPoint.get()) ||
                                !myShipBoard.getCargoHolds().get(selectedPoint.get()).getGoods().containsKey(selectedGoodsType.get())
                        ), selectedGoodsType, selectedPoint
                )
        );

        removeButton.setOnAction(_ -> {
            controller.loseGoods(selectedPoint.get());
            selectedPoint.set(null);
            selectedGoodsType.set(null);
        });

        Button nextButton = new Button("Done");
        nextButton.setOnAction(_ -> {
            boolean hasGoodsToLose = countCargoHoldsWithGoods() > 0;

            // only prevent proceeding if player has goods
            if (hasGoodsToLose) {
                guiLog.log("You need to lose goods before proceeding.");
                return;
            }

            // automatically use batteries if available
            int batteries = countBatteries();
            if (batteries > 0) {
                for (Point batteryPoint : myShipBoard.getBatteries().keySet()) {
                    guiLog.log("Using battery at position (" + batteryPoint.x + "," + batteryPoint.y + ")");
                    controller.loseGoods(batteryPoint);
                    break;
                }
            }

            getGuiController().goNext();
        });

        actionButtons.getChildren().addAll(removeButton, nextButton);
        buttonsBox.getChildren().addAll(goodsTypeBox, actionButtons);
        return buttonsBox;
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
        if (isMyTurn()) {
            updateCargoInfoBox();
        }
    }
}
