package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public interface VirtualServer {
    void registerNickname(String myNickname);
    void newGame(Level level, int playerN);
    void drawCard();
}
