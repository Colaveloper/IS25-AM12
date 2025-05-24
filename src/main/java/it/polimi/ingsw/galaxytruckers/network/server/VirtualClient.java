package it.polimi.ingsw.galaxytruckers.network.server;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface VirtualClient {

    //TODO: missing method on client
    /**
     * Sends all the information about a lobby to the client
     *
     * @param lobbyId the id of the lobby whose information is sent to the client
     * @param playerColors map containing all players' nicknames and
     *                     their corresponding color
     */
    void setupLobby(UUID lobbyId, Map<String, GameColor> playerColors);

    /**
     * Signals to the client that the players in their lobby have changed
     *
     * @param playerColors map containing all players' nicknames and
     *                     their corresponding color
     */
    void updateLobbyPlayers(Map<String, GameColor> playerColors);

    /**
     * Signals to the client that a player has successfully stashed
     * a component
     *
     * @param playerName the nickname of the player who stashed a component
     */
    void notifyStashComponent(String playerName, List<Integer> stashComponentIds);

    /**
     * Signals to the client that a player has successfully grabbed a component
     * from their stashing area
     *
     * @param playerName the nickname of the player who grabbed a stashed component
     * @param componentId the identifier of the grabbed component
     * @param stashComponentIds a list of identifiers currently in the stashing area
     */
    void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds);

    /**
     * Signals to the client that the components on a shipboard have changed
     *
     * @param playerName the nickname of the owner of the shipboard that changed
     * @param componentId id of the component added to the ship
     * @param rotation orientation of the component on the ship
     * @param position position of the component on the ship
     */
    void notifyComponentPositioning(String playerName, int componentId, int rotation, Point position);

    /**
     * Signals to the client that a player has successfully rejected
     * a component
     *
     * @param playerName the nickname of the player who has rejected the component
     * @param componentId the rejected component identifier
     */
    void notifyComponentRejection(String playerName, int componentId);

    /**
     * Signals to the client that a player has successfully requested
     * a face-down component from the bank
     *
     * @param playerName the nickname of the player who has requested the component
     * @param componentId the requested component identifier
     */
    void notifyFaceDownComponentRequest(String playerName, int componentId);

    /**
     * Signals to the client that a player has successfully requested
     * a face-up component from the bank
     *
     * @param playerName the nickname of the player who has requested the component
     * @param componentId the requested component identifier
     */
    void notifyFaceUpComponentRequest(String playerName, int componentId);

    /**
     * Signals to the client that a player has successfully peeked at a
     * forecast deck
     *
     * @param playerName the nickname of the player who peeked at the forecast deck
     * @param deckIndex the index of the forecast deck that is now blocked
     */
    void notifyPeekForecast(String playerName, int deckIndex);
    /**
     * Signals to the client that the ship building phase has started
     */

    /**
     * Signals to the client that a player has stopped peeking at a forecast deck
     * that is now available again
     *
     * @param playerName the nickname of the player who released the
     *                   forecast deck
     * @param deckIndex the index of the forecast deck that is available
     *                  again
     */
    void notifyReleaseForecast(String playerName, int deckIndex);

    /**
     * Sends to the client the information about a forecast deck that the
     * player has decided to peek at
     *
     * @param deckCardIds a list containing the ids of cards included in
     *                    the peeked forecast deck
     */
    void sendForecastDeck(List<Integer> deckCardIds);

    /**
     * Signals to the client that a player has successfully flipped the hourglass
     *
     * @param playerName the nickname of the player who flipped the hourglass
     * @param isLast is {@code true} if the hourglass has been flipped to the last spot
     */
    void notifyHourglassFlipped(String playerName, boolean isLast);

    /**
     * Signals to the client that the hourglass has run out
     */
    void notifyHourglassEnd();

    /**
     * Signals to the client that the building phase has started
     */
    void notifyStartBuilding(Level level, int playersN);


    /**
     * Signals to the client that the contents of a cabin on a player's shipboard have
     * changed
     *
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated cabin
     * @param numResidents the new number of residents in the cabin
     * @param crewType the new cabin crew type
     */
    void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType);

    /**
     * Signals to the client that a player has finished building and has successfully
     * placed his ship on the flight-board
     *
     * @param playerName the nickname of the player who finished building
     * @param position the new position on the flightBoard
     */
    void notifyPlayerPosition(String playerName, int position);

    /**
     * Notifies that a component has been removed from a ship.
     *
     * @param playerName the player's nickname
     * @param position the position of the removed component
     */
    void notifyComponentRemoval(String playerName, Point position);

    /**
     * Signals to the client that multiple disconnected components of
     * a player's ship have been removed
     *
     * @param playerName the name of the player
     * @param positions the positions of removed components
     */
    void notifyShipPieceRemoval(String playerName, List<Point> positions);

    //TODO: missing method on the client
    /**
     * Signals to the client that their ship is not connected and that they should
     * choose one of the given pieces to keep
     *
     * @param playerName
     * @param shipPieces a list containing the ship's connected subsets
     */
    void showShipPieces(String playerName, List<Set<Point>> shipPieces);

    //TODO: missing method on the client
    /**
     * Signals to the client that the list of valid ships has changed
     *
     * @param invalidPlayers a list containing the nicknames of players whose
     *                       ships are not valid
     */
    void notifyInvalidShipsUpdate(List<String> invalidPlayers);

    /**
     * Signals to the client that the stats of a player have changed
     *
     * @param playerName the player with updated stats
     * @param statType the specific stat being updated
     * @param value the new stat value
     */
    void notifyShipStatUpdate(String playerName, StatType statType, int value);

    /**
     * Signals to the client that a new card has been drawn
     *
     * @param cardId the id of the newly drawn card
     */
    void notifyNewCard(int cardId);

    /**
     * Signals to the client that the given player can select the given
     * points for their next action
     *
     * @param playerName the name of the player
     * @param selectablePoints a list containing all selectable points
     */
    void notifySelection(String playerName, List<Point> selectablePoints);

    /**
     * Signals to the client that the contents of a cargo hold component on a
     * player's ship have changed
     *
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated cargo hold component
     * @param cargo the new contents of the cargo hold component
     */
    void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType,Integer> cargo);

    //TODO: missing method on the client
    /**
     * Signals to the client that the contents of a battery component on a player's
     * ship have changed
     *
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated battery component
     * @param numBatteries the new number of batteries
     */
    void notifyBatteryUpdate(String playerName, Point point, int numBatteries);

    /**
     * Signals to the client that a player has chosen a planet
     * and should now load goods on their ship
     *
     * @param playerName the name of the player
     * @param planetId the id of the chosen planet
     * @param cargoPositions the positions of cargo hold components
     *                       on the player's ship
     */
    void notifyPlanetChoice(String playerName, int planetId, List<Point> cargoPositions);

    /**
     * Signals to the client that a good of the given type
     * has been grabbed from the buffer
     *
     * @param type the type of the grabbed good
     */
    void updateGoodsBuffer(GoodsType type);

    /**
     * Signals to the client that there is a projectile that needs to be handled
     *
     * @param projectileType    the type of projectile
     * @param direction         the direction it is coming from
     * @param roll              the result of the dice roll
     * @param selectablePoints  list of component position that can be activated
     * @param batteries         position of batteries components
     */
    void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries);

    /**
     * Signals to the client that the game has ended and sends the
     * final scores
     *
     * @param playerToScore a map containing the final score of each player
     */
    void showFinalScores(Map<String, Integer> playerToScore);

    /**
     * Signals to the client that some players have surrendered
     *
     * @param playerNames the nicknames of the players who have surrendered
     */
    void notifySurrender(List<String> playerNames);

    void notifyPlayerDisconnection(String playerName);
}
