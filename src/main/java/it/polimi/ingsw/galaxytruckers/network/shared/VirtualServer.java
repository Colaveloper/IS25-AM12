package it.polimi.ingsw.galaxytruckers.network.shared;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.IOException;
import java.rmi.RemoteException;

public interface VirtualServer extends EventListener {
    void registerNickname(String tempNickname, String newNickname) throws IOException;
    void newGame(Level level, int playerN) throws IOException;
    void drawCard(String nickname) throws IOException;
    void reportError(String error) throws RemoteException;
}
