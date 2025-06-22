package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.CircularToggleButton;
import it.polimi.ingsw.galaxytruckers.view.guiElements.PurpleHBox;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GoodsBuffer;
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
import java.util.Set;

public class GuiGoodsScreen extends GuiAdventureScreen {
    private final GoodsBuffer goodsBuffer;
    private final PurpleHBox bufferInfoBox = new PurpleHBox(5);
    private final ObjectProperty<Point> selectedPoint;
    private final ObjectProperty<GoodsType> selectedGoodsType;

    public GuiGoodsScreen(ClientModel model, ControllerToServer controller, AddGoodsState addGoodsState) {
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

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
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

            @Override
            public void goNext() {
                if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }
        };
    }

    @Override
    protected final VBox getFreeUseVBox() {
        VBox superBox = super.getFreeUseVBox();
        superBox.getChildren().add(bufferInfoBox);
        updateBufferBox();
        return superBox;
    }

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

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        super.notifyComponentChange(shipBoard, point);
        Platform.runLater(this::updateBufferBox);
    }
}
