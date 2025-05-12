package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.EventListener;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer extends EventListener {
    void registerNickname(String myNickname) throws IOException;
    void newGame(Level level, int playerN) throws IOException;
    void drawCard(String nickname) throws IOException;
    void reportError(String error) throws RemoteException;
}
