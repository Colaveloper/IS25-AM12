package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.CircularToggleButton;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChoosePlanetState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Map;

public class GuiPlanetScreen extends GuiAdventureScreen {
    private final ObjectProperty<ShipBoard> currentShipBoard;
    private final ListProperty<ShipBoard> planetToShip;

    public GuiPlanetScreen(ClientModel model, ControllerToServer controller, ChoosePlanetState choosePlanetState) {
        super(model, controller, choosePlanetState);
        guiLog.log(isMyTurn() ? "Your turn to choose a planet" : "Waiting for other player");

        currentShipBoard = new SimpleObjectProperty<>(choosePlanetState.getShipBoard());
        planetToShip = new SimpleListProperty<>(FXCollections.observableArrayList(choosePlanetState.getOptions()));

        VBox verticalButtonBox = new VBox(10);
        verticalButtonBox.setAlignment(Pos.CENTER);

        HBox planetsButtons = new HBox(10);
        planetsButtons.setAlignment(Pos.CENTER);

        for (int i = 0; i < planetToShip.size(); i++) {
            CircularToggleButton planetButton = createPlanetButton(i);
            planetsButtons.getChildren().add(planetButton);
        }
        guiButtonBox.getChildren().add(planetsButtons);

        Button skipBtn = new Button("Skip");
        skipBtn.setOnAction(e -> getGuiController().goNext());
        skipBtn.disableProperty().bind(Bindings.createBooleanBinding(
                () -> currentShipBoard.get() != myShipBoard,
                currentShipBoard
        ));

        verticalButtonBox.getChildren().addAll(planetsButtons, skipBtn);
        guiButtonBox.getChildren().add(verticalButtonBox);
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void goNext() {
                if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }

            @Override
            public void choosePlanet(int i) {
                if (state.getAvailableActions().contains(StateActions.CHOOSE_PLANET)) {
                    controller.choosePlanet(i);
                }
            }
        };
    }

    private CircularToggleButton createPlanetButton(int planetIndex) {
        CircularToggleButton planetButton = new CircularToggleButton(Color.GRAY);
        planetButton.setText(String.valueOf(planetIndex + 1));
        planetButton.setOnAction(_-> getGuiController().choosePlanet(planetIndex));
        planetButton.isActiveProperty().bind(Bindings.createBooleanBinding(
                () -> planetToShip.get(planetIndex) != null,
                planetToShip
        ));
        planetToShip.addListener((obs, oldVal, newVal) -> {
            if (planetToShip.get(planetIndex) != null) {
                planetButton.setColor(switch (planetToShip.get(planetIndex).getColor()) {
                    case RED -> Color.RED;
                    case BLUE -> Color.BLUE;
                    case YELLOW -> Color.YELLOW;
                    case GREEN -> Color.GREEN;
                });
            }
        });
        currentShipBoard.addListener((obs, oldVal, newVal) -> {
            planetButton.setDisable(
                            currentShipBoard.get() == null ||
                            !currentShipBoard.get().equals(myShipBoard)
            );
        });

        return planetButton;
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        Platform.runLater(() -> {
            planetToShip.set(choice, shipBoard);
            currentShipBoard.set(nextShipBoard);
        });
    }

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        Platform.runLater(() -> {
            currentShipBoard.set(shipBoard);
        });
    }
}