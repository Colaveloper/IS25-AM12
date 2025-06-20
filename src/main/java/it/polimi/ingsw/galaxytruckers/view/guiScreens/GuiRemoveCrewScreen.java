package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveCrewState;
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

public class GuiRemoveCrewScreen extends GuiAdventureScreen {
    private final BorderPane layout;
    private Point selectedPoint;
    private GuiShipBoard guiShipBoard;

    public GuiRemoveCrewScreen(ClientModel model, ControllerToServer controller, RemoveCrewState removeCrewState) {
        super(model, controller, removeCrewState);
        this.layout = new BorderPane();
        this.selectedPoint = null;
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
        layout.getChildren().clear();
        ShipBoard currentShip = state.getShipBoard();

        VBox topInfo = new VBox(5);
        topInfo.setPadding(new Insets(10));
        topInfo.setAlignment(Pos.CENTER);

        if (isMyTurn()) {
            Label turnLabel = new Label("Your turn to remove crew members");
            turnLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(turnLabel);

            Label instructionLabel = new Label("Select cabins to remove crew from");
            instructionLabel.setStyle("-fx-text-fill: white;");
            topInfo.getChildren().add(instructionLabel);

            if (selectedPoint != null) {
                Label selectedPointLabel = new Label("Selected position: (" + selectedPoint.x + "," + selectedPoint.y + ")");
                selectedPointLabel.setStyle("-fx-text-fill: white;");
                topInfo.getChildren().add(selectedPointLabel);
            }
        } else {
            Label waitingLabel = new Label("Waiting for " + currentShip.getColor() + " ship to remove crew members");
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

            VBox actionButtons = new VBox(5);
            actionButtons.setPadding(new Insets(5));

            Button removeCrewButton = new Button("Remove Crew");
            removeCrewButton.setDisable(selectedPoint == null ||
                    !currentShip.getCabins().containsKey(selectedPoint) ||
                    currentShip.getCabins().get(selectedPoint).getNumResidents() <= 0);
            removeCrewButton.setOnAction(e -> {
                controller.loseCrew(selectedPoint);
                selectedPoint = null;
                updateLayout();
            });

            Button nextButton = new Button("Done");
            nextButton.setOnAction(e -> getGuiController().goNext());

            actionButtons.getChildren().addAll(removeCrewButton, nextButton);
            buttons.getChildren().add(actionButtons);

            layout.setBottom(buttons);
        }
    }
}
