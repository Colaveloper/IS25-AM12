package it.polimi.ingsw.galaxytruckers.network.server.rmi;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiVirtualServer;
import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.ServerController;
import it.polimi.ingsw.galaxytruckers.serverController.ServerControllerInterface;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventQueue;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Questa classe rappresenta la logica del server implementata con tecnologia RMI.
 */
public class RmiServer extends UnicastRemoteObject implements RmiVirtualServer {
    private EventQueue eventQueue;
    private EventHandler handler;
    private static final String serverName = "RMI server";

    final ServerControllerInterface controller;
    final BiMap<String, RmiVirtualClient> nicknameToClient = HashBiMap.create();

    public RmiServer(ServerControllerInterface controller) throws RemoteException {
        super();
        this.controller = controller;
    }

    public static void start(ServerControllerInterface serverController) throws RemoteException {
//        final String serverName = "GalacticServer";
        it.polimi.ingsw.galaxytruckers.network.client.rmi.RmiVirtualServer server = new RmiServer(serverController);
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, server);
        System.out.println(serverName+": started ✅");
    }


    @Override
    public void connect(RmiVirtualClient client) throws RemoteException {
        //TODO. Attenzione, più client possono invocare questo metodo simultaneamente!
        synchronized (this.nicknameToClient) {
            // we use a temporary nickname to immediately use communication by nickname
            // the nickname is then overwritten by one of user's choice
            this.nicknameToClient.put(String.valueOf(client.hashCode()), client);
            System.out.println(serverName+": new unnamed player added ✅");
            System.out.println("Top-secret: temporary nickname is:" + String.valueOf(client.hashCode()));
        }
    }

    @Override
    public void registerNickname(String newNickname) throws RemoteException {
        try {
            controller.registerNickname(newNickname);
//            nicknameToClient.forcePut(newNickname, nicknameToClient.get(tempNickname));
            nicknameToClient.get(newNickname).showNicknameRegistration(newNickname); // TODO: make event
        } catch (IllegalArgumentException | IOException e) {
//            nicknameToClient.get(tempNickname).reportError("Request refused: Nickname already taken"); // TODO: make event
        }
        // TODO: Give another chance for input
    }

    @Override
    public void newGame(Level level, int playerN) throws IOException {

    }

    @Override
    public void requestRandComponent() {

    }

    @Override
    public void requestComponent(int componentID) {

    }

    @Override
    public void grabStashedComponent(int componentID) {

    }

    @Override
    public void flipHourglass() {

    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {

    }

    @Override
    public void acquireForecast(int deckIndex) {

    }

    @Override
    public void rejectComponent() {

    }

    @Override
    public void stashComponent() {

    }

    @Override
    public void placeComponent(Point point) {

    }

    @Override
    public void releaseForecast() {

    }

    @Override
    public void drawCard() throws IOException {
        System.out.println("new card request received");
        // TODO: get cardId from controller
        int cardId = (int) (Math.random()*100);
        synchronized (this.nicknameToClient){
            for(RmiVirtualClient client: nicknameToClient.inverse().keySet()){
                client.showNewCard(cardId);
            }
        }
    }

    @Override
    public void reportError(String error) throws RemoteException {
        synchronized (this.nicknameToClient){
            for(RmiVirtualClient client: nicknameToClient.inverse().keySet()){
                client.reportError(error);
            }
        }
    }

    @Override
    public void registerHandler(EventHandler handler) throws RemoteException {
        this.handler = handler;
    }

    @Override
    public void processEvents() throws IOException {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.dequeue();
            event.accept(handler);
        }
    }

    public void addEvent(Event event) {
        eventQueue.enqueue(event);
    }
}
