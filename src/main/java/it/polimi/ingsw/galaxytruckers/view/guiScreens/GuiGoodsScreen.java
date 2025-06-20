package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GoodsBuffer;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;

public class GuiGoodsScreen extends GuiAdventureScreen {
    private final GoodsBuffer goodsBuffer;
    private final BorderPane layout;
    private Point selectedPoint;
    private GoodsType selectedGoodsType;
    private GuiShipBoard guiShipBoard;

    public GuiGoodsScreen(ClientModel model, ControllerToServer controller, AddGoodsState addGoodsState) {
        super(model, controller, addGoodsState);
        this.goodsBuffer = addGoodsState.getGoodsBuffer();
        this.layout = new BorderPane();
        this.selectedPoint = null;
        this.selectedGoodsType = null;
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                selectedPoint = point;
                Platform.runLater(() -> updateLayout());
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
        updateLayout();
        return layout;
    }

    private void updateLayout() {
        /* Goods screen that shows available goods
        * user selects the good types they want to either remove or place
        * user selects the cargohold on the ship by clicking on it
        * "done" button to confirm selections made*/
        layout.getChildren().clear();

        ShipBoard currentShip = state.getShipBoard();

        VBox topInfo = new VBox(5);
        topInfo.setPadding(new Insets(10));
        topInfo.setAlignment(Pos.CENTER);

        if (isMyTurn()) {
            Label turnLabel = new Label("Your turn to manage goods");
            turnLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(turnLabel);

            Label bufferLabel = new Label("Goods in buffer:");
            bufferLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(bufferLabel);

            for (GoodsType type : GoodsType.values()) {
                int amount = goodsBuffer.getGoodsBuffer().getOrDefault(type, 0);
                if (amount > 0) {
                    Label goodsLabel = new Label(type + ": " + amount);
                    goodsLabel.setStyle("-fx-text-fill: white;");
                    topInfo.getChildren().add(goodsLabel);
                }
            }

            int cargoHoldsWithGoods = countCargoHoldsWithGoods();
            Label cargoHoldsLabel = new Label("Cargo holds with goods: " + cargoHoldsWithGoods);
            cargoHoldsLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(cargoHoldsLabel);

            if (selectedPoint != null) {
                Label selectedPointLabel = new Label("Selected position: (" + selectedPoint.x + "," + selectedPoint.y + ")");
                selectedPointLabel.setStyle("-fx-text-fill: white;");
                topInfo.getChildren().add(selectedPointLabel);
            }
        } else {
            Label waitingLabel = new Label("Waiting for " + currentShip.getColor() + " ship to manage goods");
            waitingLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(waitingLabel);
        }

        layout.setTop(topInfo);

        guiShipBoard = new GuiShipBoard(currentShip, getGuiController());

        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(guiShipBoard);

        layout.setCenter(centerBox);

        if (isMyTurn()) {
            HBox buttons = new HBox(10);
            buttons.setPadding(new Insets(10));
            buttons.setAlignment(Pos.CENTER);

            VBox goodsTypeButtons = new VBox(5);
            goodsTypeButtons.setPadding(new Insets(5));
            Label selectGoodsLabel = new Label("Select goods type:");
            selectGoodsLabel.setStyle("-fx-text-fill: white;");
            goodsTypeButtons.getChildren().add(selectGoodsLabel);

            for (GoodsType type : GoodsType.values()) {
                Button typeButton = new Button(type.toString());
                typeButton.setOnAction(e -> {
                    selectedGoodsType = type;
                    updateLayout();
                });

                if (type == selectedGoodsType) {
                    typeButton.setStyle("-fx-background-color: lightblue;");
                }

                goodsTypeButtons.getChildren().add(typeButton);
            }

            VBox actionButtons = new VBox(5);
            actionButtons.setPadding(new Insets(5));

            Button placeButton = new Button("Place Good");
            placeButton.setDisable(selectedPoint == null || selectedGoodsType == null ||
                    goodsBuffer.getGoodsBuffer().getOrDefault(selectedGoodsType, 0) <= 0 ||
                    !currentShip.getCargoHolds().containsKey(selectedPoint));
            placeButton.setOnAction(e -> {
                // Check if special cargo in regular hold
                if (selectedGoodsType == GoodsType.RED &&
                    !currentShip.getCargoHolds().get(selectedPoint).isSpecial()) {
                    // Display error message
                    Label errorMsg = new Label("Cannot add special cargo to a regular cargo hold!");
                    errorMsg.setStyle("-fx-text-fill: red;");
                    topInfo.getChildren().add(errorMsg);
                    return;
                }
                controller.placeGoods(selectedPoint, selectedGoodsType);
                selectedPoint = null;
                selectedGoodsType = null;
                updateLayout();
            });

            Button removeButton = new Button("Remove Good");
            removeButton.setDisable(selectedPoint == null || selectedGoodsType == null ||
                    !currentShip.getCargoHolds().containsKey(selectedPoint) ||
                    !currentShip.getCargoHolds().get(selectedPoint).getGoods().containsKey(selectedGoodsType));
            removeButton.setOnAction(e -> {
                controller.removeGoods(selectedPoint, selectedGoodsType);
                selectedPoint = null;
                selectedGoodsType = null;
                updateLayout();
            });

            Button nextButton = new Button("Done");
            nextButton.setOnAction(e -> getGuiController().goNext());

            actionButtons.getChildren().addAll(placeButton, removeButton, nextButton);

            buttons.getChildren().addAll(goodsTypeButtons, actionButtons);
            layout.setBottom(buttons);
        }
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        Platform.runLater(this::updateLayout);
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        Platform.runLater(this::updateLayout);
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
}
