package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for all game screens in the GUI version of Galaxy Truckers.
 * This class provides the common layout and functionality for the main gameplay screens,
 * organizing the visual elements such as ship boards, flight boards, statistics, and logs.
 *
 * The screen is divided into several sections:
 * - Top: Flight board showing all players' positions
 * - Left: Miniature views of other players' ships
 * - Center: The current player's ship and action buttons
 * - Right: Game information panel with statistics and log
 */
public abstract class GuiGameScreen extends GuiScreen {
    protected final Map<ShipBoard, GuiShipBoard> guiShipBoards;
    protected final GuiLog guiLog;
    protected final GuiFlightBoard guiFlightBoard;
    protected final PurpleHBox guiButtonBox;
    protected final GuiStatBox guiStatBox;

    /**
     * Constructs a GuiGameScreen with the specified model, controller, and game state.
     * Initializes all the visual components needed for gameplay, including:
     * - Ship boards for all players
     * - Flight board
     * - Game log
     * - Button container
     * - Statistics display
     *
     * @param model The client model containing all game data
     * @param controller The controller for sending commands to the server
     * @param state The current game state
     */
    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);

        guiShipBoards = new HashMap<>();
        for (Player player : model.getPlayers()) {
            GuiController guiController = player.getShipBoard().equals(model.getMyShip())
                    ? getGuiController()
                    : new GuiController() {};
            guiShipBoards.put(player.getShipBoard(), new GuiShipBoard(player.getNickname(), player.getShipBoard(), guiController));
        }

        guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), getGuiController());
        guiLog = new GuiLog();
        guiButtonBox = new PurpleHBox(10);
        guiStatBox = new GuiStatBox(myShipBoard);
    }

    /**
     * Creates and returns the complete game screen layout.
     * Arranges all UI components in a vertical layout with the flight board at the top
     * and the main game area (player ships, controls, and info panel) below.
     *
     * @return A Pane containing the complete game screen UI
     */
    public final Pane getNode() {
        VBox layout = new VBox(10);

        PurpleVBox guiFlightBoard = this.guiFlightBoard;

        HBox bottomBox = getBottomBox();
        VBox.setVgrow(bottomBox, Priority.ALWAYS);
        bottomBox.setPrefHeight(0); // parent sets the real height

        layout.getChildren().addAll(guiFlightBoard, bottomBox);
        return layout;
    }

    /**
     * Creates the main horizontal layout containing other players' ships,
     * the current player's ship, and the side information panel.
     *
     * @return An HBox containing the three main vertical sections of the game UI
     */
    private HBox getBottomBox() {
        HBox bottomBox = new HBox(10);

        PurpleVBox othersShipsVBox = getOtherShipsVBox();
        othersShipsVBox.setScaleX(0.6);
        othersShipsVBox.setScaleY(0.6);
        Group rescalingOtherShipsGroup = new Group(othersShipsVBox);

        VBox leftVBox = new PurpleVBox(rescalingOtherShipsGroup);
        VBox.setVgrow(leftVBox, Priority.ALWAYS);

        VBox centralVBox = getCentralVBox();
        HBox.setHgrow(centralVBox, Priority.ALWAYS);
        centralVBox.setPrefWidth(0);

        VBox sidePanel = getSideVBox();
        HBox.setHgrow(sidePanel, Priority.ALWAYS);
        centralVBox.setPrefWidth(0);

        bottomBox.getChildren().addAll(leftVBox, centralVBox, sidePanel);
        return bottomBox;
    }

    /**
     * Creates a vertical panel containing miniature views of all other players' ships.
     * These are scaled down to save space while still providing visibility of other players' progress.
     *
     * @return A VBox containing scaled-down views of other players' ship boards
     */
    private PurpleVBox getOtherShipsVBox() {
        PurpleVBox otherShipsVBox = new PurpleVBox(10);

        for (ShipBoard shipBoard : guiShipBoards.keySet()) {
            if (!shipBoard.equals(myShipBoard)) {
                VBox shipBoardVBox = getShipBoardVBox(shipBoard);
                shipBoardVBox.setMaxWidth(300);
                otherShipsVBox.getChildren().add(shipBoardVBox);
                VBox.setVgrow(otherShipsVBox, Priority.ALWAYS);
                otherShipsVBox.setPrefHeight(0); // parent sets the real height
            }
        }

        return otherShipsVBox;
    }

    /**
     * Creates the central vertical panel containing the current player's ship board
     * and the action buttons below it.
     *
     * @return A VBox containing the player's ship board and action buttons
     */
    private VBox getCentralVBox() {
        VBox centralVBox = new PurpleVBox(10);

        VBox mainShipVBox = getShipBoardVBox(myShipBoard);

        HBox guiButtonBox = this.guiButtonBox;
        VBox.setVgrow(guiButtonBox, Priority.ALWAYS);
        guiButtonBox.setPrefHeight(0); // parent sets the real height

        centralVBox.getChildren().addAll(mainShipVBox, guiButtonBox);
        return centralVBox;
    }

    /**
     * Creates the side information panel containing game-specific controls,
     * player statistics, and the game event log.
     *
     * @return A VBox containing game controls, statistics, and the log
     */
    private VBox getSideVBox() {
        VBox sideVBox = new PurpleVBox(10);
        sideVBox.setAlignment(Pos.CENTER);

        VBox freeUseVBox = getFreeUseVBox();

        GuiStatBox guiStatBox = this.guiStatBox;


        GuiLog guiLog = this.guiLog;
        VBox.setVgrow(guiLog, Priority.ALWAYS);
        guiLog.setMaxHeight(Double.MAX_VALUE);
        sideVBox.getChildren().addAll(freeUseVBox, guiStatBox, guiLog);
        return sideVBox;
    }

    /**
     * Creates a vertical box containing game-specific UI elements that vary by game phase.
     * Subclasses implement this to provide appropriate controls for different game phases.
     *
     * @return A VBox with phase-specific UI elements.
     */
    protected abstract VBox getFreeUseVBox();

    /**
     * Creates a vertical box containing a ship board and associated UI elements.
     * The specific elements included depend on the ship board and game phase.
     *
     * @param shipBoard The ship board to be displayed
     * @return A VBox with the ship board and associated elements like hand or stash
     */
    protected abstract VBox getShipBoardVBox(ShipBoard shipBoard);

    /**
     * Provides the appropriate GuiController for handling user interactions.
     * Different game phases require different controllers to handle phase-specific actions.
     *
     * @return A GuiController implementation appropriate for the current game phase
     */
    protected abstract GuiController getGuiController();

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
        guiStatBox.notifyChange();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        super.notifyRemoveComponent(shipBoard, point);
        guiStatBox.notifyChange();
    }
}