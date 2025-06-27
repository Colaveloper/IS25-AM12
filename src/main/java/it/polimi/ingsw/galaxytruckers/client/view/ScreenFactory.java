package it.polimi.ingsw.galaxytruckers.client.view;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
//import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiSecondShipBuildingScreen;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.MetaState;
import it.polimi.ingsw.galaxytruckers.client.model.state.*;

/**
 * Factory interface for creating Screen instances based on different game states.
 * Implements the Factory Method design pattern to create appropriate screen objects
 * for either CLI or GUI implementations depending on the concrete factory used.
 *
 * @param <S> The type of Screen to be created, must extend the Screen class
 */
public interface ScreenFactory<S extends Screen> {
    /**
     * Creates a screen based on a meta state (non-gameplay state like login or lobby).
     *
     * @param metaState   The current meta state of the client
     * @param model       The client model containing game data
     * @param controller  The controller for communicating with the server
     * @return            A screen instance appropriate for the given meta state
     */
    S createScreen(MetaState metaState, ClientModel model, ClientControllerInterface controller);

    /**
     * Creates a screen based on a game state (active gameplay state).
     *
     * @param gameState   The current game state of the client
     * @param model       The client model containing game data
     * @param controller  The controller for communicating with the server
     * @return            A screen instance appropriate for the given game state
     */
    S createScreen(GameState gameState, ClientModel model, ClientControllerInterface controller);
}
