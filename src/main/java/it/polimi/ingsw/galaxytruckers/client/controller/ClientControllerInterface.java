package it.polimi.ingsw.galaxytruckers.client.controller;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;

import java.awt.*;
import java.util.UUID;

/**
 * Interface for client-to-server controller actions and client-side model updates.
 */
public interface ClientControllerInterface {

    /**
     * Clears the model, deleting game data and resetting the state to JOIN_OR_CREATE
     */
    void clearModel();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void registerNickname(String input);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void flipHourglass();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void releaseForecast();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void requestNewGame(Level level, int playersN);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void showGameCreation();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void joinLobby(UUID lobbyID);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void requestRandComponent();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void requestComponent(int componentId);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void stashComponent();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void grabPlacedComponent();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void grabStashedComponent(int index);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void acquireForecast(int index);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void rejectComponent();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void placeComponent(Point point, Direction orientation);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void goNext();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void chooseShipPiece(int choice);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void activateComponent(Point point);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void removeComponent(Point point);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void useBattery(Point point);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void initializeCabin(Point point, CrewType crewType);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void placeShipOnFlightboard(int startingPosition);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void placeShipOnFlightBoard();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void drawCard();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void placeGoods(Point point, GoodsType good);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void removeGoods(Point point, GoodsType good);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void loseGoods(Point p);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void choosePlanet(int choice);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void loseCrew(Point p);

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void grabReward();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void giveUp();

    /**
     * Calls the corresponding method on {@link VirtualServer}.
     */
    void quit();
}
