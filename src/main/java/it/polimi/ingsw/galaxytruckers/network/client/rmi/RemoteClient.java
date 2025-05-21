package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface RemoteClient extends Remote {

    /**
     * Sends all the information about a lobby to the client
     * @param lobbyId the id of the lobby whose information is sent to the client
     * @param playerColors map containing all players' nicknames and
     *                     their corresponding color
     */
    void setupLobby(UUID lobbyId, Map<String, FourColors> playerColors) throws RemoteException;

    /**
     * Signals to the client that the players in their lobby have changed
     * @param playerColors map containing all players' nicknames and
     *                     their corresponding color
     */
    void updateLobbyPlayers(Map<String, FourColors> playerColors) throws RemoteException;

    /**
     * Signals to the client that the ship building phase has started
     */
    void notifyStartBuilding(Level level, int playersN) throws RemoteException;

    /**
     * Signals to the client that a player has successfully requested
     * a face-down component from the bank
     *
     * @param playerName  the nickname of the player who has requested the component
     * @param componentId the requested component identifier
     */
    void notifyFaceDownComponentRequest(String playerName, int componentId) throws RemoteException;

    /**
     * Signals to the client that a player has successfully requested
     * a face-up component from the bank
     *
     * @param playerName  the nickname of the player who has requested the component
     * @param componentId the requested component identifier
     */
    void notifyFaceUpComponentRequest(String playerName, int componentId) throws RemoteException;

    /**
     * Signals to the client that a player has successfully rejected
     * a component
     *
     * @param playerName  the nickname of the player who has rejected the component
     * @param componentId the rejected component identifier
     */
    void notifyComponentRejection(String playerName, int componentId) throws RemoteException;

    /**
     * Signals to the client that a player has successfully stashed
     * a component
     * @param playerName the nickname of the player who stashed a component
     */
    void notifyStashComponent(String playerName, List<Integer> stashComponentIds) throws RemoteException;

    /**
     * Signals to the client that a player has successfully grabbed a component
     * from their stashing area
     * @param playerName the nickname of the player who grabbed a stashed component
     * @param componentId the identifier of the grabbed component
     * @param stashComponentIds a list of identifiers currently in the stashing area
     */
    void notifyGrabFromStash(String playerName, int componentId, List<Integer> stashComponentIds) throws RemoteException;

    /**
     * Signals to the client that a player has successfully flipped the hourglass
     * @param playerName the nickname of the player who flipped the hourglass
     * @param isLast is {@code true} if the hourglass has been flipped to the last spot
     */
    void notifyHourglassFlipped(String playerName, boolean isLast) throws RemoteException;

    /**
     * Signals to the client that the components on a shipboard have changed
     * @param playerName the nickname of the owner of the shipboard that changed
     * @param componentId id of the component added to the ship
     * @param rotation orientation of the component on the ship
     * @param position position of the component on the ship
     */
    void notifyShipMapUpdate(String playerName, int componentId, int rotation, Point position) throws RemoteException;

    /**
     * Sends to the client the information about a forecast deck that the
     * player has decided to peek at
     *
     * @param deckCardIds a list containing the ids of cards included in
     *                    the peeked forecast deck
     */
    void sendForecastDeck(List<Integer> deckCardIds) throws RemoteException;

    /**
     * Signals to the client that a player has successfully peeked at a
     * forecast deck
     * @param playerName the nickname of the player who peeked at the forecast deck
     * @param deckIndex the index of the forecast deck that is now blocked
     */
    void notifyPeekForecast(String playerName, int deckIndex) throws RemoteException;

    /**
     * Signals to the client that a player has stopped peeking at a forecast deck
     * that is now available again
     * @param playerName the nickname of the player who released the
     *                   forecast deck
     * @param deckIndex the index of the forecast deck that is available
     *                  again
     */
    void notifyReleaseForecast(String playerName, int deckIndex) throws RemoteException;

    /**
     * Signals to the client that a player has finished building and has successfully
     * placed his ship on the flight-board
     *
     * @param playerName the nickname of the player who finished building
     * @param position
     */
    void notifyPlaceShipOnFlightBoard(String playerName, int position) throws RemoteException;

    /**
     * Signals to the client that their ship is not connected and that they should
     * choose one of the given pieces to keep
     *
     * @param playerName
     * @param shipPieces a list containing the ship's connected subsets
     */
    void showShipPieces(String playerName, List<Set<Point>> shipPieces) throws RemoteException;

    /**
     * Signals to the client that the list of valid ships has changed
     * @param invalidPlayers a list containing the nicknames of players whose
     *                       ships are not valid
     */
    void notifyInvalidShipsUpdate(List<String> invalidPlayers) throws RemoteException;

    /**
     * Signals to the client that the contents of a cabin on a player's shipboard have
     * changed
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated cabin
     * @param numResidents the new number of residents in the cabin
     * @param crewType the new cabin crew type
     */
    void notifyCabinUpdate(String playerName, Point point, int numResidents, CrewType crewType) throws RemoteException;

    /**
     * Signals to the client that the contents of a battery component on a player's
     * ship have changed
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated battery component
     * @param numBatteries the new number of batteries
     */
    void notifyBatteryUpdate(String playerName, Point point, int numBatteries) throws RemoteException;

    /**
     * Signals to the client that the contents of a cargo hold component on a
     * player's ship have changed
     * @param playerName the nickname of the owner of the ship that changed
     * @param point the position of the updated cargo hold component
     * @param cargo the new contents of the cargo hold component
     */
    void notifyCargoHoldUpdate(String playerName, Point point, Map<GoodsType,Integer> cargo) throws RemoteException;

    /**
     * Signals to the client that the stats of a player have changed
     * @param playerName the player with updated stats
     * @param statType the specific stat being updated
     * @param value the new stat value
     */
    void notifyShipStatUpdate(String playerName, StatType statType, int value) throws RemoteException;

    /**
     * Signals to the client that a new card has been drawn
     * @param cardId the id of the newly drawn card
     */
    void notifyNewCard(int cardId) throws RemoteException;

    /**
     * Signals to the client that some players have surrendered
     * @param playerNames the nicknames of the players who have surrendered
     */
    void notifySurrender(List<String> playerNames) throws RemoteException;

    void notifyHourglassEnd() throws RemoteException;

    void notifyComponentRemoval(String playerName, Point position) throws RemoteException;

    void notifyShipPieceRemoval(String playerName, List<Point> positions) throws RemoteException;

    void notifySelection(String playerName, List<Point> selectablePoints) throws RemoteException;

    void notifyPlanetChoice(String playerName, int planetId, List<Point> cargoPositions) throws RemoteException;

    void updateGoodsBuffer(GoodsType type) throws RemoteException;

    void showProjectile(ProjectileType projectileType, int direction, int roll, List<Point> selectablePoints, List<Point> batteries) throws RemoteException;

    void showFinalScores(Map<String, Integer> playerToScore) throws RemoteException;

    void notifyPlayerDisconnection(String playerName);

}
