package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.UUID;

/**
 * Interface for client-to-server controller actions.
 * Each method calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
 */
public interface ControllerToServer {

    void clearModel();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void registerNickname(String input);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void flipHourglass();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void releaseForecast();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void requestNewGame(Level level, int playersN);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void showGameCreation();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void joinLobby(UUID lobbyID);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void requestRandComponent();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void requestComponent(int componentId);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void stashComponent();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void grabPlacedComponent();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void grabStashedComponent(int index);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void acquireForecast(int index);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void rejectComponent();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void placeComponent(Point point, Direction orientation);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void goNext();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void chooseShipPiece(int choice);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void activateComponent(Point point);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void removeComponent(Point point);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void useBattery(Point point);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void initializeCabin(Point point, CrewType crewType);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void placeShipOnFlightboard(int startingPosition);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void placeShipOnFlightBoard();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void drawCard();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void placeGoods(Point point, GoodsType good);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void removeGoods(Point point, GoodsType good);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void loseGoods(Point p);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void choosePlanet(int choice);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void loseCrew(Point p);

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void grabReward();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void giveUp();

    /**
     * Calls the corresponding method on {@link it.polimi.ingsw.galaxytruckers.network.client.VirtualServer}.
     */
    void quit();
}
