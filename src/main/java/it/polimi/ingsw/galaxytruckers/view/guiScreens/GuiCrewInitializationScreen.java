package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.CircularToggleButton;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiLog;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipInitializationState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuiCrewInitializationScreen extends GuiGameScreen {
    private final ObjectProperty<CrewType> currentCrewType;
    private final Map<CrewType, Set<Point>> crewTypeToPoints;

    public GuiCrewInitializationScreen(ClientModel model, ControllerToServer controller, ShipInitializationState state) {
        super(model, controller, state);
        currentCrewType = new SimpleObjectProperty<>(null);
        crewTypeToPoints = state.getCrewtypeToPoints().getOrDefault(myShipBoard, new HashMap<>());

        HBox crewButtons = new HBox();

        if (crewTypeToPoints.containsKey(CrewType.PURPLE)) {
            CircularToggleButton purpleAlienButton = new CircularToggleButton(Color.PURPLE);
            purpleAlienButton.setOnAction(_ -> currentCrewType.set(CrewType.PURPLE));
            purpleAlienButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                    ()-> currentCrewType.get() != null && currentCrewType.get() == CrewType.PURPLE,
                    currentCrewType
            ));
            crewButtons.getChildren().add(purpleAlienButton);
        }
        if (crewTypeToPoints.containsKey(CrewType.BROWN)) {
            CircularToggleButton brownAlienButton = new CircularToggleButton(Color.BROWN);
            brownAlienButton.setOnAction(_ -> currentCrewType.set(CrewType.BROWN));
            brownAlienButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                    ()-> currentCrewType.get() != null && currentCrewType.get() == CrewType.BROWN,
                    currentCrewType
            ));
            crewButtons.getChildren().add(brownAlienButton);
        }

        Button okButton = new Button("OK");
        okButton.setOnAction(_->getGuiController().goNext());

        guiButtonBox.getChildren().addAll(crewButtons, okButton);
    }

    @Override
    protected VBox getFreeUseVBox() {
        return new VBox();
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);
        getShipBoardVBox.getChildren().addAll(guiShipBoards.get(shipBoard));
        return getShipBoardVBox;
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (currentCrewType.get() != null) {
                    if (
                            !myShipBoard.getCabins().containsKey(point)
                    ) {
                        guiLog.log("Cannot place an alien outside of a cabin");
                    } else if (
                            !crewTypeToPoints.get(currentCrewType.get()).contains(point)
                    ) {
                        guiLog.log("Cabin not appropriate for a " + currentCrewType + " alien");
                    } else {
                        if (state.getAvailableActions().contains(StateActions.INITIALIZE_CABIN)) {
                            controller.initializeCabin(point, currentCrewType.get());
                        }
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
}
