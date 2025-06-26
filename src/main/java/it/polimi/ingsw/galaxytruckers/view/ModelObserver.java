package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Observer interface for model changes in the Galaxy Truckers game.
 * Implements the Observer pattern to notify view components about model state changes.
 * Contains methods for receiving notifications about various game events, state changes,
 * and player actions that affect the model.
 */
public interface ModelObserver {
    //region Event update methods

    /**
     * Notifies the observer that a player has joined the game
     *
     * @param player the player who joined
     */
    void notifyPlayerJoin(Player player);

    /**
     * Notifies the observer of a change in the meta state of the game.
     *
     * @param metaState The new meta state of the game
     */
    void notifyMetaState(MetaState metaState);

    /**
     * Notifies the observer of a change in the current gameplay state.
     *
     * @param gameState The new game state
     */
    void notifyCurrentState(GameState gameState);

    /**
     * Notifies the observer about a newly created lobby.
     *
     * @param lobby The newly created lobby
     */
    void notifyNewLobby(Lobby lobby);

    /**
     * Notifies the observer about a removed lobby.
     *
     * @param uuid The UUID of the removed lobby
     */
    void notifyRemoveLobby(UUID uuid);

    /**
     * Notifies the observer that a random component has been requested.
     *
     * @param shipBoard The ship board requesting the component
     * @param component The randomly selected component
     */
    void notifyRequestRandComponent(ShipBoard shipBoard, Component component);

    /**
     * Notifies the observer that a specific component has been requested from the rejected pile of components.
     *
     * @param shipBoard The ship board requesting the component
     * @param component The requested component
     */
    void notifyRequestComponent(ShipBoard shipBoard, Component component);

    /**
     * Notifies the observer that a component has been rejected.
     *
     * @param shipBoard The ship board where the component was rejected
     * @param component The rejected component
     */
    void notifyRejectComponent(ShipBoard shipBoard, Component component);

    /**
     * Notifies the observer that a component has been rejected, including its old position.
     *
     * @param shipBoard   The ship board where the component was rejected
     * @param component   The rejected component
     * @param oldPosition The previous position of the component
     */
    void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition);

    /**
     * Notifies the observer that a component has been stashed.
     *
     * @param shipBoard The ship board where the component was stashed
     * @param component The stashed component
     */
    void notifyStashComponent(ShipBoard shipBoard, Component component);

    /**
     * Notifies the observer that a component has been stashed, including its old position.
     *
     * @param shipBoard   The ship board where the component was stashed
     * @param component   The stashed component
     * @param oldPosition The previous position of the component
     */
    void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition);

    /**
     * Notifies the observer that a placed component has been grabbed.
     *
     * @param shipBoard    The ship board where the component was grabbed from
     * @param prevPosition The previous position of the grabbed component
     */
    void notifyGrabPlacedComponent(ShipBoard shipBoard, Point prevPosition);

    /**
     * Notifies the observer that a stashed component has been grabbed.
     *
     * @param shipBoard The ship board where the component was grabbed from
     * @param index     The index of the stashed component
     * @param component The grabbed component
     */
    void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component);

    /**
     * Notifies the observer that a component has been placed on the board.
     *
     * @param shipBoard   The ship board where the component was placed
     * @param newPoint    The position where the component was placed
     * @param orientation The orientation of the placed component
     */
    void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation);

    /**
     * Notifies the observer that a component has been placed, including its old position.
     *
     * @param shipBoard   The ship board where the component was placed
     * @param newPoint    The new position of the component
     * @param orientation The orientation of the component
     * @param oldPosition The previous position of the component
     */
    void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition);

    /**
     * Notifies the observer that the hourglass has been flipped.
     *
     * @param shipBoard The ship board associated with the hourglass
     */
    void notifyFlipHourglass(ShipBoard shipBoard);

    /**
     * Notifies the observer that the hourglass timer has ended.
     */
    void notifyHourglassEnd();

    /**
     * Notifies the observer of a ship's position update on the flight board.
     *
     * @param shipBoard The ship board being updated
     * @param position  The new position on the flight board
     */
    void notifyFlightBoardPosition(ShipBoard shipBoard, int position);

    /**
     * Notifies the observer that a forecast card has been peeked at.
     *
     * @param shipBoard The ship board associated with the forecast
     * @param deckIndex The index of the deck being peeked at
     */
    void notifyPeekForecast(ShipBoard shipBoard, int deckIndex);

    /**
     * Sets the forecast deck of adventure cards.
     *
     * @param adventureCards The list of adventure cards for the forecast deck
     */
    void setForecastDeck(List<AdventureCard> adventureCards);

    /**
     * Notifies the observer that a forecast has been released.
     *
     * @param shipBoard The ship board associated with the forecast
     * @param index     The index of the released forecast
     */
    void notifyReleaseForecast(ShipBoard shipBoard, int index);

    /**
     * Notifies the observer that a component has been removed from the board.
     *
     * @param shipBoard The ship board where the component was removed
     * @param point     The position of the removed component
     */
    void notifyRemoveComponent(ShipBoard shipBoard, Point point);

    /**
     * Notifies the observer that a ship piece has been chosen.
     *
     * @param shipBoard  The ship board where the piece was chosen
     * @param pieceIndex The index of the chosen piece
     * @param removed    List of points where components were removed
     */
    void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed);

    /**
     * Notifies the observer that the ship is not properly connected.
     *
     * @param shipBoard  The ship board that is not connected
     * @param shipPieces Sets of points representing disconnected ship pieces
     */
    void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces);

    /**
     * Notifies the observer that the ship has been validated.
     *
     * @param shipBoard The validated ship board
     */
    void notifyShipValidated(ShipBoard shipBoard);

    /**
     * Notifies the observer that a cabin has been initialized.
     *
     * @param shipBoard    The ship board where the cabin was initialized
     * @param point        The position of the cabin
     * @param crewType     The type of crew assigned to the cabin
     * @param numResidents The number of residents in the cabin
     */
    void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents);

    /**
     * Notifies the observer that an adventure card has been drawn.
     *
     * @param adventureCard The drawn adventure card
     */
    void notifyDrawCard(AdventureCard adventureCard);

    /**
     * Notifies the observer that a component has been activated.
     *
     * @param shipBoard The ship board where the component was activated
     * @param point     The position of the activated component
     */
    void notifyActivateComponent(ShipBoard shipBoard, Point point);

    /**
     * Notifies the observer that a crew member has been lost.
     *
     * @param shipBoard The ship board where the crew was lost
     * @param point     The position where the crew was lost
     */
    void notifyLoseCrew(ShipBoard shipBoard, Point point);

    /**
     * Notifies the observer that a reward has been grabbed or not.
     *
     * @param shipBoard      The ship board associated with the reward
     * @param rewardGrabbed  Whether the reward was grabbed (true) or not (false)
     */
    void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed);

    /**
     * Notifies the observer that goods have been placed on the ship.
     *
     * @param shipBoard The ship board where goods were placed
     * @param point     The position where goods were placed
     * @param goodsType The type of goods placed
     */
    void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    /**
     * Notifies the observer that goods have been removed from the ship.
     *
     * @param shipBoard The ship board where goods were removed
     * @param point     The position where goods were removed
     * @param goodsType The type of goods removed
     */
    void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType);

    /**
     * Notifies the observer that a battery has been used.
     *
     * @param shipBoard The ship board where the battery was used
     * @param point     The position of the used battery
     */
    void notifyUseBattery(ShipBoard shipBoard, Point point);

    /**
     * Notifies the observer that a planet has been chosen.
     *
     * @param shipBoard     The current ship board
     * @param choice        The index of the chosen planet
     * @param nextShipBoard The next ship board after the choice
     */
    void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard);

    /**
     * Notifies the observer of the current player update.
     *
     * @param shipBoard The ship board of the current player
     */
    void notifyCurrentPlayerUpdate(ShipBoard shipBoard);

    /**
     * Notifies the observer that a player has given up.
     *
     * @param player The player who gave up
     */
    void notifyGiveUp(Player player);

    /**
     * Sets the final scores for all players.
     *
     * @param finalScores A map of players to their final scores
     */
    void setFinalScores(Map<Player, Integer> finalScores);

    //endregion
}
