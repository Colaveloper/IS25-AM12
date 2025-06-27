package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.view.Screen;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.GameState;
import javafx.scene.layout.Pane;

/**
 * Abstract base class for all GUI screens in the Galaxy Truckers game.
 * This class extends the generic Screen class and provides common functionality
 * and data that all GUI-based screens need to operate.
 *
 * Each concrete implementation of GuiScreen represents a different screen or view
 * in the game's graphical user interface, such as menus, game boards, or dialogs.
 */
public abstract class GuiScreen extends Screen {
    protected final ClientModel model;
    protected final ClientControllerInterface controller;
    protected final GameState state;
    protected ShipBoard myShipBoard;

    /**
     * Constructs a GuiScreen with the specified model, controller, and game state.
     *
     * @param model The client model containing all game data
     * @param controller The controller for sending commands to the server
     * @param state The current game state, can be null for screens not associated with a game state
     */
    public GuiScreen(ClientModel model, ClientControllerInterface controller, GameState state) {
        this.model = model;
        this.controller = controller;
        this.state = state;
        if (state != null) this.myShipBoard = model.getMyShip();
    }

    /**
     * Constructs a GuiScreen with the specified model and controller, but no game state.
     * This constructor is typically used for screens that don't require a specific game state,
     * such as the main menu or connection screens.
     *
     * @param model The client model containing all game data
     * @param controller The controller for sending commands to the server
     */
    public GuiScreen(ClientModel model, ClientControllerInterface controller) {
        this(model, controller, null);
    }

    /**
     * Gets the JavaFX Pane that represents this screen's visual elements.
     * Each concrete implementation must provide the specific Pane that contains
     * all the visual components for that screen.
     *
     * @return A JavaFX Pane containing this screen's UI elements
     */
    public abstract Pane getNode();
}
