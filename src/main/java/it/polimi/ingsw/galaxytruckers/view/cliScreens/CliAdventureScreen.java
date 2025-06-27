package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all CLI screens in the adventure phase of the game.
 * Provides common functionality for adventure-related screens including turn management,
 * ship board tracking, and adventure card display.
 */
public abstract class CliAdventureScreen extends CliScreen{

    /**
     * Flag indicating whether it is the current player's turn.
     */
    protected final boolean isMyTurn;
    /**
     * Flag indicating whether the player has surrendered and is out of the game.
     */
    protected final boolean imOut;
    /**
     * The ship board of the current player.
     */
    protected final ShipBoard currentShip;
    /**
     * The current adventure card being played.
     */
    protected final CliAdventureCard currentAdventureCard;

    /**
     * Creates a new adventure screen with the given game state.
     * Initializes the screen with the current player's ship status,
     * turn information, and active adventure card.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current adventure phase state
     */
    public CliAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        imOut = gameState.getImOut();   //if player surrendered
        currentShip = gameState.getShipBoard(); // ship of the current player playing
        isMyTurn = currentShip == myShipBoard;
        AdventureCard adventureCard = model.getGame().getCurrentCard();
        if(adventureCard != null) this.currentAdventureCard = new CliAdventureCard(adventureCard);
        else this.currentAdventureCard = null;
    }

    /**
     * Generates a formatted description of the current adventure card.
     * Creates a bordered display with the card's description and title.
     *
     * @return A list of strings containing the formatted card description
     */
    protected List<String> printCurrentCard() {
        return DescriptionUtils.borderAndTitle(currentAdventureCard.getDescription(), "Current card");
    }
}
