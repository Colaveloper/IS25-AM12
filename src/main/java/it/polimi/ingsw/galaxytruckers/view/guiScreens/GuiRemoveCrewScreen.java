package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveCrewState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.Set;

public class GuiRemoveCrewScreen extends GuiAdventureScreen {
    private final ObjectProperty<Point> selectedPoint;
    private final VBox cabinInfoBox;

    public GuiRemoveCrewScreen(ClientModel model, ControllerToServer controller, RemoveCrewState removeCrewState) {
        super(model, controller, removeCrewState);
        this.selectedPoint = new SimpleObjectProperty<>();
        this.cabinInfoBox = new VBox(2);
        updateCabinInfoBox();

        if (isMyTurn()) {
            guiLog.log("Your turn to remove crew members");
            guiLog.log("Select a cabin to remove crew from");
            guiLog.log("Then select remove action");
        } else {
            guiLog.log("Wait for others to remove crew members");
        }

        if (isMyTurn()) {
            setupButtonBox();
        }
    }

    private void setupButtonBox() {
        guiButtonBox.getChildren().clear();

        VBox actionButtons = new VBox(5);
        actionButtons.setPadding(new Insets(5));

        Button removeButton = new Button("Remove Crew");
        removeButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> (
                                selectedPoint.get() == null ||
                                !myShipBoard.getCabins().containsKey(selectedPoint.get()) ||
                                myShipBoard.getCabins().get(selectedPoint.get()).getNumResidents() <= 0
                        ), selectedPoint
                )
        );

        removeButton.setOnAction(_ -> {
            controller.loseCrew(selectedPoint.get());
            selectedPoint.set(null);
        });

        Button nextButton = new Button("Done");
        nextButton.setOnAction(_ -> getGuiController().goNext());

        actionButtons.getChildren().addAll(removeButton, nextButton);
        guiButtonBox.getChildren().add(actionButtons);
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (state.getAvailableActions().contains(StateActions.LOSE_CREW)) {
                    if (myShipBoard.getCabins().containsKey(point)) {
                        selectedPoint.set(point);
                        updateCabinInfoBox();
                        guiLog.log("Action to be performed at "+ point.x + "," + point.y);
                        guiShipBoards.get(myShipBoard).highlightPoints(Set.of(point), Color.YELLOW);
                    } else {
                        guiLog.log("No cabin at " + point.x + "," + point.y);
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

    private void updateCabinInfoBox() {
        cabinInfoBox.getChildren().clear();
        Label cabinsWithCrewLabel = new Label("Cabins with crew: " + countCabinsWithCrew());
        cabinsWithCrewLabel.setTextFill(Color.WHITE);
        cabinInfoBox.getChildren().add(cabinsWithCrewLabel);

        if (selectedPoint.get() != null && myShipBoard.getCabins().containsKey(selectedPoint.get())) {
            Label positionLabel = new Label("Selected position: (" + selectedPoint.get().x + "," + selectedPoint.get().y + ")");
            positionLabel.setTextFill(Color.WHITE);
            cabinInfoBox.getChildren().add(positionLabel);

            int crewCount = myShipBoard.getCabins().get(selectedPoint.get()).getNumResidents();
            Label crewLabel = new Label("Crew members: " + crewCount);
            crewLabel.setTextFill(Color.WHITE);
            cabinInfoBox.getChildren().add(crewLabel);
        }
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = super.getFreeUseVBox();

        if (isMyTurn()) {
            freeUseVBox.getChildren().add(cabinInfoBox);
        }

        return freeUseVBox;
    }

    private int countCabinsWithCrew() {
        ShipBoard currentShip = state.getShipBoard();
        int count = 0;
        for (var cabin : currentShip.getCabins().values()) {
            if (cabin.getNumResidents() > 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        super.notifyComponentChange(shipBoard, point);
        guiShipBoards.get(myShipBoard).clearHighlights();
        updateCabinInfoBox();
    }
}
