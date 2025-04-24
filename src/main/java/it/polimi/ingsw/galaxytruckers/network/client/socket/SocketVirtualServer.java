package it.polimi.ingsw.galaxytruckers.network.client.socket;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.shared.ClientRequest;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class SocketVirtualServer implements VirtualServer {
    private final ObjectOutputStream out;

    public SocketVirtualServer(ObjectOutputStream out) throws IOException {
        this.out = out;
    }

    @Override
    public void registerNickname(String tempNickname, String newNickname) throws IOException {
        out.writeObject(new ClientRequest(tempNickname, "registerNickname", newNickname));
    }

    @Override
    public void newGame(Level level, int playerN) throws IOException {

    }

    @Override
    public void drawCard(String nickname) throws IOException {
        out.writeObject(new ClientRequest(nickname, "drawCard"));
    }

    @Override
    public void registerHandler(EventHandler handler) throws RemoteException {
    }

    @Override
    public void processEvents() throws IOException {
    }

    @Override
    public void reportError(String error) throws RemoteException {
    }
}
