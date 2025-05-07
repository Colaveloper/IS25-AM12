package it.polimi.ingsw.galaxytruckers.network.shared;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.UUID;

public interface VirtualServer extends EventListener {
    void reportError(String error) throws RemoteException;

    // SETUP
    void registerNickname(String myNickname) throws IOException;
    void newGame(Level level, int playerN) throws IOException;

    // BUILDING
    void requestRandComponent() throws IOException;
    void requestComponent(int componentID);
    void grabStashedComponent(int componentID);
    void flipHourglass();
    void placeShipOnFlightBoard(int startingPosition);

    void acquireForecast(int deckIndex);
    void rejectComponent();
    void stashComponent();
    void placeComponent(Point point);

    void releaseForecast();


    void drawCard() throws IOException;
}
