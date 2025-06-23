package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

/**
 * Abstract base class for different view implementations in the Galaxy Truckers game.
 * This class implements the Model-View-Controller pattern by connecting to both the model
 * and controller components. It also acts as an observer of the model, receiving and
 * processing updates when the model changes.
 *
 * <p>Concrete implementations of this class (CLI and GUI views) handle the actual
 * rendering and user interaction aspects specific to each view type.</p>
 *
 * @param <S> The type of Screen used by this view, must extend the Screen class
 */
public abstract class View<S extends Screen> implements ModelObserver, ErrorReporter {
    /** The client model containing the game data being observed */
    protected final ClientModel model;

    /** The client controller for handling user commands */
    protected final ClientController controller;

    /** The factory for creating screen instances appropriate for this view type */
    protected final ScreenFactory<S> screenFactory;

    /**
     * Constructs a new View with the specified model, controller, and screen factory.
     * Registers this view as an observer of the model to receive updates.
     *
     * @param model The client model containing game data
     * @param controller The client controller for handling user commands
     * @param screenFactory The factory for creating screen instances
     */
    protected View(ClientModel model, ClientController controller, ScreenFactory<S> screenFactory) {
        this.model = model;
        this.controller = controller;
        this.screenFactory = screenFactory;

        model.addObserver(this);
    }
}
