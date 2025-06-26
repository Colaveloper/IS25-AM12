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

/**
 * GUI screen for the planet selection phase of the adventure.
 * This screen allows players to choose planets by selecting corresponding
 * colored buttons. Each player can only choose planets when it's their turn,
 * and can skip their turn if desired.
 */
public class GuiPlanetScreen extends GuiAdventureScreen {
    private final ObjectProperty<ShipBoard> currentShipBoard;
    private final ListProperty<ShipBoard> planetToShip;

    /**
     * Constructs a new planet selection screen.
     *
     * @param model             The client model containing game state
     * @param controller        The controller for communicating with the server
     * @param choosePlanetState The state containing valid actions and planet options for this phase
     */
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

    /**
     * {@inheritDoc}
     * Creates and returns a GUI controller that handles planet selection
     * and navigation to the next screen.
     *
     * @return A GUI controller for this screen
     */
    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            /**
             * Handles navigation to the next screen if allowed by the current state.
             */
            @Override
            public void goNext() {
                if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }

            /**
             * Handles planet selection for a specific planet index.
             *
             * @param i The index of the planet to choose
             */
            @Override
            public void choosePlanet(int i) {
                if (state.getAvailableActions().contains(StateActions.CHOOSE_PLANET)) {
                    controller.choosePlanet(i);
                }
            }
        };
    }

    /**
     * Creates a circular toggle button representing a planet that can be selected.
     * The button is colored according to the ship that has chosen it, and is
     * disabled when it's not the player's turn or the planet is already taken.
     *
     * @param planetIndex The index of the planet this button represents
     * @return A CircularToggleButton configured for planet selection
     */
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

    /**
     * {@inheritDoc}
     * Updates the GUI when a player chooses a planet, updating the button colors
     * and enabling/disabling controls based on whose turn it is next.
     *
     * @param shipBoard The ship board that made the choice
     * @param choice The index of the chosen planet
     * @param nextShipBoard The ship board that can choose next
     */
    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        Platform.runLater(() -> {
            planetToShip.set(choice, shipBoard);
            currentShipBoard.set(nextShipBoard);
            guiLog.log("It's " + model.getPlayerByShip(state.getShipBoard()).getNickname() + "'s turn now.");
        });
    }

    /**
     * {@inheritDoc}
     * Updates the GUI when the current player changes, enabling/disabling
     * controls based on whose turn it is.
     *
     * @param shipBoard The ship board that can now make choices
     */
    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        Platform.runLater(() -> {
            currentShipBoard.set(shipBoard);
            guiLog.log("It's " + model.getPlayerByShip(state.getShipBoard()).getNickname() + "'s turn now.");
        });
    }
}