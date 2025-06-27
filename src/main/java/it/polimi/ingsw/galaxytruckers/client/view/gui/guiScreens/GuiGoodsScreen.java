package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.CircularToggleButton;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.PurpleHBox;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.client.model.state.GoodsBuffer;
import it.polimi.ingsw.galaxytruckers.client.model.state.StateActions;
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
import java.util.Set;

/**
 * GUI screen for managing goods during the adventure phase.
 * This screen allows players to place goods in cargo holds or remove them.
 */
public class GuiGoodsScreen extends GuiAdventureScreen {
    private final GoodsBuffer goodsBuffer;
    private final PurpleHBox bufferInfoBox = new PurpleHBox(5);
    private final ObjectProperty<Point> selectedPoint;
    private final ObjectProperty<GoodsType> selectedGoodsType;

    /**
     * Constructs a new goods management screen.
     *
     * @param model         The client model containing game state
     * @param controller    The controller for communicating with the server
     * @param addGoodsState The state containing valid actions and goods buffer for this phase
     */
    public GuiGoodsScreen(ClientModel model, ClientControllerInterface controller, AddGoodsState addGoodsState) {
        super(model, controller, addGoodsState);
        this.goodsBuffer = addGoodsState.getGoodsBuffer();
        this.selectedPoint = new SimpleObjectProperty<>();
        selectedPoint.addListener((_, _, newVal) -> {
            guiShipBoards.get(myShipBoard).clearHighlights();
            if (newVal != null) guiShipBoards.get(myShipBoard).highlightPoints(Set.of(newVal), Color.BLUE);
        });
        this.selectedGoodsType = new SimpleObjectProperty<>();
        if (isMyTurn()) {
            guiLog.log("Your turn to manage goods");
            guiLog.log("Select a position as a source or a destination");
            guiLog.log("Select a goods type to place or remove");
            guiLog.log("Then select the action");
            guiButtonBox.getChildren().setAll(getButtonsBox());
        } else {
            guiLog.log("Wait for others to manage goods");
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
            /**
             * Handles a click on a point on the ship board.
             * Validates if the selected point is a cargo hold and sets it as the current selection.
             *
             * @param point The point on the ship board that was clicked
             */
            @Override
            public void handlePointPress(Point point) {
                if (
                        state.getAvailableActions().contains(StateActions.ADD_GOOD) ||
                        state.getAvailableActions().contains(StateActions.REMOVE_GOOD)
                ) {
                    if (myShipBoard.getCargoHolds().containsKey(point)) {
                        selectedPoint.set(point);
                        guiLog.log("Action to be performed at "+ point.x + "," + point.y);
                    } else {
                        guiLog.log("No cargo hold at " + point.x + "," + point.y);
                    }
                }
            }

            /**
             * Handles navigation to the next screen if allowed by the current state.
             */
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
     * Creates and returns a VBox containing the goods buffer display
     * along with the standard free-use area.
     *
     * @return A VBox containing the free-use area with goods buffer
     */
    @Override
    protected final VBox getFreeUseVBox() {
        VBox superBox = super.getFreeUseVBox();
        superBox.getChildren().add(bufferInfoBox);
        updateBufferBox();
        return superBox;
    }

    /**
     * Updates the buffer display to reflect the current state of the goods buffer.
     * Creates colored circular buttons for each good available in the buffer.
     */
    private void updateBufferBox() {
        bufferInfoBox.getChildren().clear();
        Label bufferLabel = new Label("Buffer:");
        bufferLabel.setTextFill(Color.WHITE);
        bufferLabel.setPadding(new Insets(5));
        bufferInfoBox.getChildren().add(bufferLabel);
        for (GoodsType type : GoodsType.values()) {
            for (int i = 0; i < goodsBuffer.getGoodsBuffer().getOrDefault(type, 0); i++) {
                CircularToggleButton bufferedGood = new CircularToggleButton(switch (type) {
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                    case GREEN -> Color.GREEN;
                    case YELLOW -> Color.GOLD;
                });
                bufferedGood.setScaleX(.5);
                bufferedGood.setScaleY(.5);
                bufferedGood.setActive(true);
                bufferInfoBox.getChildren().add(bufferedGood);
            }
        }
    }

    /**
     * Creates and returns an HBox containing goods type selection buttons
     * and action buttons for placing or removing goods.
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
            CircularToggleButton typeButton = new CircularToggleButton(switch (type) {
                case RED -> Color.RED;
                case BLUE -> Color.BLUE;
                case GREEN -> Color.GREEN;
                case YELLOW -> Color.GOLD;
            });
            typeButton.setOnAction(_ -> {
                guiLog.log("The action will be performed on a "+type+" good");
                selectedGoodsType.set(type);
            });
            typeButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                    ()->selectedGoodsType.get() == type,
                    selectedGoodsType
            ));
            goodsTypeBox.getChildren().add(typeButton);
        }

        VBox actionButtons = new VBox(5);
        actionButtons.setPadding(new Insets(5));

        Button placeButton = new Button("Place Good");
        placeButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (
                                selectedPoint.get() == null ||
                                selectedGoodsType.get() == null ||
                                goodsBuffer.getGoodsBuffer().getOrDefault(selectedGoodsType.get(), 0) <= 0 ||
                                !myShipBoard.getCargoHolds().containsKey(selectedPoint.get())
                        ), selectedPoint, selectedGoodsType
                )
        );
        placeButton.setOnAction(_ -> {
            // Check if special cargo in regular hold
            if (selectedGoodsType.get() == GoodsType.RED &&
                !myShipBoard.getCargoHolds().get(selectedPoint.get()).isSpecial()
            ) {
                guiLog.log("Cannot add special cargo to a regular cargo hold!");
            } else {
                controller.placeGoods(selectedPoint.get(), selectedGoodsType.get());
                selectedPoint.set(null);
                selectedGoodsType.set(null);
            }
        });

        Button removeButton = new Button("Remove Good");
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
            controller.removeGoods(selectedPoint.get(), selectedGoodsType.get());
            selectedPoint.set(null);
            selectedGoodsType.set(null);
        });

        Button nextButton = new Button("Done");
        nextButton.setOnAction(_ -> getGuiController().goNext());

        actionButtons.getChildren().addAll(placeButton, removeButton, nextButton);

        buttonsBox.getChildren().addAll(goodsTypeBox, actionButtons);
        return buttonsBox;
    }

    /**
     * {@inheritDoc}
     * Updates the GUI when a component changes on the ship board,
     * specifically refreshing the buffer display.
     *
     * @param shipBoard The ship board containing the changed component
     * @param point The position of the changed component
     */
    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        super.notifyComponentChange(shipBoard, point);
        Platform.runLater(this::updateBufferBox);
    }
}
