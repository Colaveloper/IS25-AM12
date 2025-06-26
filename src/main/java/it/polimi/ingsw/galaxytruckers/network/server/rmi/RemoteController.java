package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

/**
 * Remote interface for the controller, allowing remote method invocation (RMI).
 * This interface defines methods that can be called by clients to interact with the game.
 */
public interface RemoteController extends Remote {
    /**
     * Pings the server to check if it is reachable.
     * @throws RemoteException if there is an error during the remote method call
     */
    void ping() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#requestNewGame(Level, int)}.
     *
     * @param level the game level
     * @param numPlayers the number of players
     * @throws RemoteException if there is an error during the remote method call
     */
    void newGame(Level level, int numPlayers) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#joinLobby(UUID)}.
     *
     * @param lobbyID the UUID of the lobby to join
     * @throws RemoteException if there is an error during the remote method call
     */
    void joinLobby(UUID lobbyID) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#leaveLobby()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void leaveLobby() throws RemoteException;

    // void chooseColor(GameColor color) throws RemoteException;

    // Ship building

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#requestRandComponent()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void requestRandComponent() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#requestComponent(int)}.
     *
     * @param componentID the id of the component to request
     * @throws RemoteException if there is an error during the remote method call
     */
    void requestComponent(int componentID) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#rejectComponent()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void rejectComponent() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#stashComponent()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void stashComponent() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#grabPlacedComponent()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void grabPlacedComponent() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#grabStashedComponent(int)}.
     *
     * @param index the index of the stashed component to grab
     * @throws RemoteException if there is an error during the remote method call
     */
    void grabStashedComponent(int index) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#placeComponent(Point, Direction)}.
     *
     * @param point the coordinates where the component should be placed
     * @param orientation the direction the component should face
     * @throws RemoteException if there is an error during the remote method call
     */
    void placeComponent(Point point, Direction orientation) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#flipHourglass()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void flipHourglass() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#placeShipOnFlightBoard(int)}.
     *
     * @param startingPosition the position on the flight board where the ship should be placed
     * @throws RemoteException if there is an error during the remote method call
     */
    void placeShipOnFlightBoard(int startingPosition) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#placeShipOnFlightBoard()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void placeShipOnFlightBoard() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#acquireForecast(int)}.
     *
     * @param deckIndex the index of the deck to view the forecast from
     * @throws RemoteException if there is an error during the remote method call
     */
    void acquireForecast(int deckIndex) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#releaseForecast()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void releaseForecast() throws RemoteException;

    // Ship validity check

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#removeComponent(Point)}.
     *
     * @param point the coordinates of the component to remove
     * @throws RemoteException if there is an error during the remote method call
     */
    void removeComponent(Point point) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#chooseShipPiece(int)}.
     *
     * @param pieceIndex the index of the piece to choose
     * @throws RemoteException if there is an error during the remote method call
     */
    void chooseShipPiece(int pieceIndex) throws RemoteException;

    // Ship init

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#initializeCabin(Point, CrewType)}.
     *
     * @param point the coordinates where the cabin should be placed
     * @param crewType the type of crew to place in the cabin
     * @throws RemoteException if there is an error during the remote method call
     */
    void initializeCabin(Point point, CrewType crewType) throws RemoteException;

    // Adventure

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#drawCard()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void drawCard() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#activateComponent(Point)}.
     *
     * @param point the coordinates of the component to activate
     * @throws RemoteException if there is an error during the remote method call
     */
    void activateComponent(Point point) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#loseCrew(Point)}.
     *
     * @param point the coordinates where the crew is lost
     * @throws RemoteException if there is an error during the remote method call
     */
    void loseCrew(Point point) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#grabReward()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void grabReward() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#placeGoods(Point, GoodsType)}.
     *
     * @param point the coordinates where the goods should be placed
     * @param goodsType the type of goods to place
     * @throws RemoteException if there is an error during the remote method call
     */
    void placeGoods(Point point, GoodsType goodsType) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#removeGoods(Point, GoodsType)}.
     *
     * @param point the coordinates from which to remove the goods
     * @param goodsType the type of goods to remove
     * @throws RemoteException if there is an error during the remote method call
     */
    void removeGoods(Point point, GoodsType goodsType) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#loseGoods(Point)}.
     *
     * @param point the coordinates where the goods are lost
     * @throws RemoteException if there is an error during the remote method call
     */
    void loseGoods(Point point) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#useBattery(Point)}.
     *
     * @param point the coordinates of the battery to use
     * @throws RemoteException if there is an error during the remote method call
     */
    void useBattery(Point point) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#choosePlanet(int)}.
     *
     * @param choice the index of the chosen planet
     * @throws RemoteException if there is an error during the remote method call
     */
    void choosePlanet(int choice) throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#goNext()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void goNext() throws RemoteException;

    /**
     * Remote version of {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer#giveUp()}.
     *
     * @throws RemoteException if there is an error during the remote method call
     */
    void giveUp() throws RemoteException;
}
