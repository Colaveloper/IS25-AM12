package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
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

public class GuiLoseGoodsScreen extends GuiAdventureScreen {
    private ObjectProperty<Point> selectedPoint;
    private ObjectProperty<GoodsType> selectedGoodsType;

    public GuiLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState removeGoodsState) {
        super(model, controller, removeGoodsState);
        this.selectedPoint = new SimpleObjectProperty<>();
        this.selectedGoodsType = new SimpleObjectProperty<>();
        if (isMyTurn()) {
            guiContextBox.getChildren().setAll(
                    new Label("Your turn to lose goods"),
                    new Label("Select a cargo hold to remove goods from"),
                    new Label("Select a goods type to remove"),
                    new Label("Then select remove action")
            );
        } else {
            guiContextBox.getChildren().setAll(new Label("Wait for others to lose goods"));
        }
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (state.getAvailableActions().contains(StateActions.REMOVE_GOOD)) {
                    if (myShipBoard.getCargoHolds().containsKey(point)) {
                        selectedPoint.set(point);
                        guiContextBox.getChildren().setAll(new Label("Action to be performed at "+ point.x + "," + point.y));
                    } else {
                        guiContextBox.getChildren().setAll(new Label("No cargo hold at " + point.x + "," + point.y));
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
    public Pane getNode() {
        Pane superPane = super.getNode();

        if (isMyTurn()) {
            HBox bottomBox = new HBox(10);
            bottomBox.setAlignment(Pos.CENTER);

            VBox cargoInfoBox = new VBox(2);
            updateCargoInfoBox(cargoInfoBox);

            bottomBox.getChildren().addAll(cargoInfoBox, getButtonsBox());

            superPane.getChildren().add(bottomBox);
            superPane.setScaleX(0.9);
            superPane.setScaleY(0.9);
        }
        return superPane;
    }

    private void updateCargoInfoBox(VBox cargoInfoBox) {
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
                guiContextBox.getChildren().setAll(new Label("The action will be performed on a "+type+" good"));
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
        nextButton.setOnAction(_ -> getGuiController().goNext());

        actionButtons.getChildren().addAll(removeButton, nextButton);

        buttonsBox.getChildren().addAll(goodsTypeBox, actionButtons);
        return buttonsBox;
    }

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

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        super.notifyComponentChange(shipBoard, point);
        if (isMyTurn()) {
            Platform.runLater(() -> {
                VBox cargoInfoBox = new VBox(2);
                updateCargoInfoBox(cargoInfoBox);
            });
        }
    }
}
