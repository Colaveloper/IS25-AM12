package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface RemoteController extends Remote {
    void ping() throws RemoteException;

    void newGame(Level level, int numPlayers) throws RemoteException;
    void joinLobby(UUID lobbyID) throws RemoteException;
    void leaveLobby() throws RemoteException;
    // void chooseColor(GameColor color) throws RemoteException;

    // Ship building
    void requestRandComponent() throws RemoteException;
    void requestComponent(int componentID) throws RemoteException;
    void rejectComponent() throws RemoteException;
    void stashComponent() throws RemoteException;
    void grabStashedComponent(int index) throws RemoteException;
    void placeComponent(Point point, Direction orientation) throws RemoteException;
    void flipHourglass() throws RemoteException;
    void placeShipOnFlightBoard(int startingPosition) throws RemoteException;
    void acquireForecast(int deckIndex) throws RemoteException;
    void releaseForecast() throws RemoteException;

    // Ship validity check
    void removeComponent(Point point) throws RemoteException;
    void chooseShipPiece(int pieceIndex) throws RemoteException;

    // Ship init
    void initializeCabin(Point point, CrewType crewType) throws RemoteException;

    // Adventure
    void drawCard() throws RemoteException;
    void activateComponent(Point point) throws RemoteException;
    void loseCrew(Point point) throws RemoteException;
    void grabReward(boolean rewardGrabbed) throws RemoteException;
    void placeGoods(Point point, GoodsType goodsType) throws RemoteException;
    void removeGoods(Point point, GoodsType goodsType) throws RemoteException;
    void loseGoods(Point point) throws RemoteException;
    void useBattery(Point point) throws RemoteException;
    void choosePlanet(int choice) throws RemoteException;
    void goNext() throws RemoteException;

    void giveUp() throws RemoteException;
}
