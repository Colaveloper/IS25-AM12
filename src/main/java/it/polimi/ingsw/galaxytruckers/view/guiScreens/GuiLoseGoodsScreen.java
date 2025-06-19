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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;

public class GuiLoseGoodsScreen extends GuiAdventureScreen {
    private final BorderPane layout;
    private Point selectedPoint;
    private GoodsType selectedGoodsType;
    private GuiShipBoard guiShipBoard;

    public GuiLoseGoodsScreen(ClientModel model, ControllerToServer controller, RemoveGoodsState removeGoodsState) {
        super(model, controller, removeGoodsState);
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
    public Parent getNode() {
        updateLayout();
        return layout;
    }

    private void updateLayout() {
        layout.getChildren().clear();

        ShipBoard currentShip = state.getShipBoard();

        VBox topInfo = new VBox(5);
        topInfo.setPadding(new Insets(10));
        topInfo.setAlignment(Pos.CENTER);

        if (isMyTurn()) {
            Label turnLabel = new Label("Your turn to lose goods");
            turnLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(turnLabel);

            Label instructionLabel = new Label("Select cargo holds to discard goods from");
            instructionLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(instructionLabel);

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
            Label waitingLabel = new Label("Waiting for " + currentShip.getColor() + " ship to lose goods");
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

            Button removeButton = new Button("Lose Good");
            removeButton.setDisable(selectedPoint == null ||
                    !currentShip.getCargoHolds().containsKey(selectedPoint) ||
                    currentShip.getCargoHolds().get(selectedPoint).getGoods().isEmpty());
            removeButton.setOnAction(e -> {
                controller.loseGoods(selectedPoint);
                selectedPoint = null;
                selectedGoodsType = null;
                updateLayout();
            });

            Button nextButton = new Button("Done");
            nextButton.setOnAction(e -> getGuiController().goNext());

            actionButtons.getChildren().addAll(removeButton, nextButton);

            buttons.getChildren().addAll(goodsTypeButtons, actionButtons);
            layout.setBottom(buttons);
        }
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
