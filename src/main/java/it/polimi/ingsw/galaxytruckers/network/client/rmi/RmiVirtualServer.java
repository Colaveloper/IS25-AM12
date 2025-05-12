//package it.polimi.ingsw.galaxytruckers.network.client.rmi;
//
//import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiVirtualClient;
//import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
//
//import java.io.IOException;
//import java.rmi.Remote;
//import java.rmi.RemoteException;
//
//
///**
// * Questa interfaccia specializza l'interfaccia VirtualServer per la tecnologia Socket
// */
//public interface RmiVirtualServer extends Remote, VirtualServer {
//
//    public void connect(RmiVirtualClient client) throws RemoteException;
//
//    // metodi controller:
//    @Override
//    public void drawCard(String nickname) throws IOException;
//
//    @Override
//    public void registerNickname(String newNickname) throws RemoteException;
//}
