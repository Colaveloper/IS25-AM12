package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.Lobby;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract class representing a view screen in the Galaxy Truckers game.
 * This class defines methods for various game notifications and updates
 * that can be rendered on the screen. Concrete implementations should
 * provide specific rendering logic for both GUI and TUI.
 */
public abstract class Screen {
    /**
     * Notifies the screen that a random component has been requested.
     *
     * @param shipBoard The ship board where the component is requested
     * @param component The randomly selected component
     */
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {}

    /**
     * Notifies the screen that a specific component has been requested.
     *
     * @param shipBoard The ship board where the component is requested
     * @param component The requested component
     */
    public void notifyRequestComponent(ShipBoard shipBoard, Component component){}

    /**
     * Notifies the screen that a component has been rejected.
     *
     * @param shipBoard The ship board where the component was rejected
     * @param component The rejected component
     */
    public void notifyRejectComponent(ShipBoard shipBoard, Component component){}

    /**
     * Notifies the screen that a component has been stashed.
     *
     * @param shipBoard The ship board where the component was stashed
     * @param component The stashed component
     */
    public void notifyStashComponent(ShipBoard shipBoard, Component component){}

    /**
     * Notifies the screen that a stashed component has been grabbed.
     *
     * @param shipBoard The ship board where the component was grabbed from
     * @param index     The index of the stashed component
     * @param component The grabbed component
     */
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component){}

    /**
     * Notifies the screen that a placed component has been grabbed.
     *
     * @param shipBoard    The ship board where the component was grabbed from
     * @param prevPosition The previous position of the grabbed component
     */
    public void notifyGrabPlacedComponent(ShipBoard shipBoard, Point prevPosition) {}

    /**
     * Notifies the screen that a component has been placed on the board.
     *
     * @param shipBoard   The ship board where the component was placed
     * @param point       The position where the component was placed
     * @param orientation The orientation of the placed component
     */
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation){}

    /**
     * Notifies the screen that the hourglass has been flipped.
     *
     * @param shipBoard The ship board associated with the hourglass
     */
    public void notifyFlipHourglass(ShipBoard shipBoard){}

    /**
     * Notifies the screen that the hourglass timer has ended.
     */
    public void notifyHourglassEnd(){}

    /**
     * Notifies the screen of a ship's position update on the flight board.
     *
     * @param shipBoard The ship board being updated
     * @param position  The new position on the flight board
     */
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position){}

    /**
     * Notifies the screen that a forecast card has been peeked at.
     *
     * @param shipBoard The ship board associated with the forecast
     * @param deckIndex The index of the deck being peeked at
     */
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex){}

    /**
     * Sets the forecast deck of adventure cards.
     *
     * @param adventureCards The list of adventure cards for the forecast deck
     */
    public void setForecastDeck(List<AdventureCard> adventureCards){}

    /**
     * Notifies the screen that a forecast has been released.
     *
     * @param shipBoard The ship board associated with the forecast
     * @param index     The index of the released forecast
     */
    public void notifyReleaseForecast(ShipBoard shipBoard, int index){}

    /**
     * Notifies the screen that a component has been removed from the board.
     *
     * @param shipBoard The ship board where the component was removed
     * @param point     The position of the removed component
     */
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point){}

    /**
     * Notifies the screen that a ship piece has been chosen.
     *
     * @param shipBoard   The ship board where the piece was chosen
     * @param pieceIndex  The index of the chosen piece
     * @param removed     List of points where components were removed
     */
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed){}

    /**
     * Notifies the screen that the ship is not properly connected.
     *
     * @param shipBoard  The ship board that is not connected
     * @param shipPieces Sets of points representing disconnected ship pieces
     */
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces){}

    /**
     * Notifies the screen that the ship has been validated.
     *
     * @param shipBoard The validated ship board
     */
    public void notifyShipValidated(ShipBoard shipBoard){}

    /**
     * Notifies the screen that a cabin has been initialized.
     *
     * @param shipBoard    The ship board where the cabin was initialized
     * @param point        The position of the cabin
     * @param crewType     The type of crew assigned to the cabin
     * @param numResidents The number of residents in the cabin
     */
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents){}

    /**
     * Notifies the screen that an adventure card has been drawn.
     *
     * @param adventureCard The drawn adventure card
     */
    public void notifyDrawCard(AdventureCard adventureCard){}

    /**
     * Notifies the screen that a component has been activated.
     *
     * @param shipBoard The ship board where the component was activated
     * @param point     The position of the activated component
     */
    public void notifyActivateComponent(ShipBoard shipBoard, Point point){}

    /**
     * Notifies the screen that a crew member has been lost.
     *
     * @param shipBoard The ship board where the crew was lost
     * @param point     The position where the crew was lost
     */
    public void notifyLoseCrew(ShipBoard shipBoard, Point point){}

    /**
     * Notifies the screen that a reward has been grabbed or not.
     *
     * @param shipBoard      The ship board associated with the reward
     * @param rewardGrabbed  Whether the reward was grabbed (true) or not (false)
     */
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed){}

    /**
     * Notifies the screen that goods have been placed on the ship.
     *
     * @param shipBoard The ship board where goods were placed
     * @param point     The position where goods were placed
     * @param goodsType The type of goods placed
     */
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}

    /**
     * Notifies the screen that goods have been removed from the ship.
     *
     * @param shipBoard The ship board where goods were removed
     * @param point     The position where goods were removed
     * @param goodsType The type of goods removed
     */
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType){}

    /**
     * Notifies the screen that a battery has been used.
     *
     * @param shipBoard The ship board where the battery was used
     * @param point     The position of the used battery
     */
    public void notifyUseBattery(ShipBoard shipBoard, Point point){}

    /**
     * Notifies the screen that a planet has been chosen.
     *
     * @param shipBoard      The current ship board
     * @param choice         The index of the chosen planet
     * @param nextShipBoard  The next ship board after the choice
     */
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard){}

    /**
     * Notifies the screen of the current player update.
     *
     * @param shipBoard The ship board of the current player
     */
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard){}

    /**
     * Notifies the screen that a player has given up.
     *
     * @param player The player who gave up
     */
    public void notifyGiveUp(Player player){}

    /**
     * Sets the final scores for all players.
     *
     * @param finalScores A map of players to their final scores
     */
    public void setFinalScores(Map<Player, Integer> finalScores){}

    /**
     * Notifies the screen that a component has been rejected with its old position.
     *
     * @param shipBoard    The ship board where the component was rejected
     * @param component    The rejected component
     * @param oldPosition  The previous position of the component
     */
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition){}

    /**
     * Notifies the screen that a component has been stashed with its old position.
     *
     * @param shipBoard    The ship board where the component was stashed
     * @param component    The stashed component
     * @param oldPosition  The previous position of the component
     */
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {}

    /**
     * Notifies the screen that a component has been placed with its old position.
     *
     * @param shipBoard    The ship board where the component was placed
     * @param newPoint     The new position of the component
     * @param orientation  The orientation of the component
     * @param oldPosition  The previous position of the component
     */
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition) {}

    /**
     * Notifies the screen that a new lobby has been created.
     *
     * @param lobby The newly created lobby
     */
    public void notifyNewLobby(Lobby lobby){}

    /**
     * Notifies the screen that a lobby has been removed.
     *
     * @param LobbyId The UUID of the removed lobby
     */
    public void notifyRemoveLobby(UUID LobbyId){}

    /**
     * Notifies the screen that a player has joined the game.
     *
     * @param player the player who joined
     */
    public void notifyPlayerJoin(Player player){}

    /**
     * Notifies the screen that a component has changed.
     *
     * @param shipBoard The ship board where the component changed
     * @param point     The position of the changed component
     */
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {}
}
