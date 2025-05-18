package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Interface for client-side controller used to receive updates from the server
 * during the game lifecycle in Galaxy Truckers.
 */
public interface ClientControllerInterface {

    //-----------------------------LOBBY SETUP----------------------------------

    /**
     * Updates the client with the current players and their assigned colors.
     * Color assignment is automatic for now
     *
     * @param playerToColor a map linking player nicknames to their chosen colors
     * @throws IOException if a communication error occurs
     */
    void updateLobbyPlayers(Map<String, Colors> playerToColor) throws IOException;


    //-----------------------------BUILDING PHASE----------------------------------

    /**
     * Notifies the client about the available components in the stash of a player, called when anyone places a component in stashed.
     * sets current his component in stashed, clears his current component
     *
     * @param playerName         the name of the player
     * @param stashComponentIds  list of component IDs currently in the stash
     * @throws IOException if a communication error occurs
     */
    void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws IOException;

    /**
     * Notifies that a player has taken a component from the stash, called when anyone picks from stashed.
     * removes it from his stashed, sets it as his current
     *
     * @param playerName         the name of the player
     * @param componentId        the ID of the grabbed component
     * @param stashComponentIds  updated list of all components in the stash
     * @throws IOException if a communication error occurs
     */
    void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws IOException;

    /**
     * Notifies that a player has positioned a component on their ship.
     *
     * @param nickname     the player's nickname
     * @param componentId  the ID of the placed component
     * @param direction    the rotation of the component
     * @param position     the grid position where the component was placed
     * @throws IOException if a communication error occurs
     */
    void notifyComponentPositioning(String nickname, int componentId, int direction, Point position) throws IOException;

    /**
     * Notifies that a component has been rejected by a player.
     * adds it to face ups, clears his current component
     *
     * @param playerName   the name of the player
     * @param componentId  the ID of the rejected component
     * @throws IOException if a communication error occurs
     */
    void notifyComponentRejection(String playerName, int componentId) throws IOException;

    /**
     * Notifies that a player has requested a face-down component
     * sets covered--, sets his current component to the one picked
     *
     * @param playerName   the name of the player
     * @param componentId  the ID of the requested component
     * @throws IOException if a communication error occurs
     */
    void notifyFaceDownComponentRequest(String playerName, int componentId) throws IOException;

    /**
     * Notifies that a player has requested a face-up component.
     * removes it from available face ups
     *
     * @param playerName   the name of the player
     * @param componentId  the ID of the requested component
     * @throws IOException if a communication error occurs
     */
    void notifyFaceUpComponentRequest(String playerName, int componentId) throws IOException;

    /**
     * Notifies that another player is peeking at a forecast deck and locks it until released.
     *
     * @param playerName  the name of the player
     * @param deckIndex   the index of the forecast deck
     * @throws IOException if a communication error occurs
     */
    void notifyPeekForecast(String playerName, int deckIndex) throws IOException;

    /**
     * Notifies that anyone has released a forecast deck and sets it as free
     * also called for who has the deck in hand to change his screen
     *
     * @param playerName  the name of the player
     * @param deckIndex   the index of the forecast deck
     * @throws IOException if a communication error occurs
     */
    void notifyReleaseForecast(String playerName, int deckIndex) throws IOException;

    /**
     * Sends the forecast deck to one client.
     * this is called only for the client who asked for the forecast
     * other clients get notifyPeekForecast to lock that deck instead
     *
     * @param deckCardIds list of card IDs in the forecast deck
     * @throws IOException if a communication error occurs
     */
    void sendForecastDeck(List<Integer> deckCardIds) throws IOException;

    /**
     * Notifies that a player has flipped the hourglass.
     *
     * @param playerName the name of the player
     * @param isLast     true if it was the last hourglass//todo not sure how this works
     */
    void notifyHourglassFlipped(String playerName, boolean isLast);

    /**
     * Notifies that the hourglass countdown has ended.
     * //todo start ship validation controls
     */
    void notifyHourglassEnd();

    /**
     * Notifies that a cabin component has been updated.
     * this is for cabin initialisation only, notifyShipStatusUpdate is used to set crew size afterward
     *
     * @param nickname  the player's nickname
     * @param position  the position of the cabin
     * @param crew      number of crew members
     * @param crewType  type of crew
     * @throws IOException if a communication error occurs
     */
    void notifyCabinUpdate(String nickname, Point position, int crew, CrewType crewType) throws IOException;


    //-----------------------------BOTH BUILDING AND ADVENTURE----------------------------------

    /**
     * Sets the position of a player during flight.
     * used for: place on board at end of building state, all cards except meteors and epidemic
     *
     * @param playerName the name of the player
     * @param position   the new position on the flight board
     * @throws IOException if a communication error occurs
     */
    void notifyPlayerPosition(String playerName, int position) throws IOException;

    /**
     * Notifies that a component has been removed from a ship.
     *
     * @param nickname      the player's nickname
     * @param positionPoint the position of the removed component
     * @throws IOException  if a communication error occurs
     */
    void notifyComponentRemoval(String nickname, Point positionPoint) throws IOException;

    /**
     * Notifies that a multiple components of a ship have been removed because it was disconnected.
     * //todo ask how it is in server, could be redundant with notifyComponentRemoval
     *
     * @param nickname          the player's nickname
     * @param positionPoints    the position of the removed components
     * @throws IOException      if a communication error occurs
     */
    void notifyShipPieceRemoval(String nickname, List<Point> positionPoints) throws IOException;

    /**
     * Notifies a change in any stat of a ship
     * used for: possibly all cards and end of building state
     * //todo if we show stats during all building state this is called more then once
     *
     * @param nickname  the player's nickname
     * @param statType  the type of stat being updated FIREPOWER,ENGINEPOWER,CREWSIZE,BATTERIES,CREDITS,LOSSES
     * @param value     the new stat value
     * @throws IOException if a communication error occurs
     */
    void notifyShipStatusUpdate(String nickname, StatType statType, int value) throws IOException;


    //-----------------------------ADVENTURE PHASE----------------------------------

    /**
     * Notifies that a new card has been drawn.
     *
     * @param cardId the ID of the new card
     * @throws IOException if a communication error occurs
     */
    void notifyNewCard(int cardId) throws IOException;

    /**
     * Notifies the current player that he can interact with some component
     * behavior is managed looking at the current card
     * used for: pirates, slavers, smugglers (points = cannons)
     *          open space (points = engines)
     * NB: other methods send a list of selectable but need their own methods:
     * meteors uses showProjectile because projectileType and direction are needed anyway
     * planets uses choosePlanet to update the available planets
     * //todo combat zone needs his own method too
     *
     * @param nickname          the player's nickname
     * @param cannonsPositions  selectable cannon points
     * @throws IOException      if a communication error occurs
     */
    void notifySelection(String nickname, List<Point> cannonsPositions) throws IOException;

    /**
     * Notifies that placed OR REMOVED a good in a cargo hold.
     * NB: a player can remove from a position and place in another on the ship, and from ship to buffer and vice versa, so it is always 2 calls
     * used for: smugglers, abandoned station, planets
     *
     * @param nickname  the player's nickname
     * @param position  the position of the cargo hold
     * @param goods     map of goods and their quantities
     * @throws IOException if a communication error occurs
     */
    void notifyCargoHoldUpdate(String nickname, Point position, Map<GoodsType, Integer> goods) throws IOException;

    /**
     * Informs everyone of the chosen planet, then the client loads the goods from the card
     * it is followed by a sequence of updateGoodsBuffer and/or notifyCargoHoldUpdate
     * used for: planets
     *
     * @param planetId          the index of the planet chosen
     * @param cargoPositions    positions of cargo hold component for highlights
     * @throws IOException      if a communication error occurs
     */
    void choosePlanet(int planetId, List<Point> cargoPositions) throws IOException;

    /**
     * Updates the available goods buffer after a good is picked.
     *
     * @param type the type of good picked
     * @throws IOException if a communication error occurs
     */
    void updateGoodsBuffer(GoodsType type) throws IOException;

    /**
     * Displays a projectile being fired, consuming batteries happens in the same screen.
     * used for: meteors, combat zone, pirates
     * //todo: find smart way to display difference between: shield/cannon, batteries and active, inactive for each one
     * //todo can also be done by sending projectile index from the list of that card, this gives client both type and direction
     *
     * @param projectileType    the type of projectile
     * @param direction         the direction it is moving
     * @param roll              the result of the dice roll
     * @param selectablePoints  list of component position that can be activated
     * @param batteries         position of batteries components
     * @throws IOException if a communication error occurs
     */
    void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) throws IOException;


    //-----------------------------ENDGAME PHASE----------------------------------

    /**
     * Displays final statistics at the end of the game.
     * //todo add all info necessary
     */
    void showFinalStats();

    /**
     * Reports an error to the client.
     * //todo i don t know how exceptions are used for the connection but this had to be removed(?)
     *
     * @param details a message describing the error
     * @throws RemoteException if a remote communication error occurs
     */
    void reportError(String details) throws RemoteException;
}