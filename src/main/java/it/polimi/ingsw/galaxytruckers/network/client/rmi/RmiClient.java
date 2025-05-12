//package it.polimi.ingsw.galaxytruckers.network.client.rmi;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
//import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
//import it.polimi.ingsw.galaxytruckers.network.server.rmi.RmiVirtualClient;
//import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;
//import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
//import it.polimi.ingsw.galaxytruckers.serverController.events.NewCardEvent;
//import it.polimi.ingsw.galaxytruckers.view.*;
//
//import java.awt.*;
//import java.io.IOException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.util.*;
//import java.util.List;
//
///**
// * Questa classe rappresenta la logica del client implementata con tecnologia RMI.
// */
//public class RmiClient extends UnicastRemoteObject implements RmiVirtualClient, EventHandler {
//    final RmiVirtualServer server;
//    ClientController controller;
//    private static final String serverName = "RMI server";
//
//    public RmiClient(RmiVirtualServer server) throws RemoteException{
//        super();
//        this.server = server;
//        controller = new ClientController(server);
//    }
//
//    public static void main(String[] args) throws Exception {
//        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
//        RmiVirtualServer server = (RmiVirtualServer) registry.lookup(serverName);
//        new RmiClient(server).run();
//    }
//
//    private void run() throws IOException {
//        server.connect(this);
//        // setting temporary nickName
//        controller.showConnectedAndNicknameChoice(String.valueOf(this.hashCode()));
//    }
//
//    @Override
//    public void showNicknameRegistration(String nickname) throws RemoteException {
//        controller.setNickname(nickname);
//    }
//
//    @Override
//    public void showNewCard(Integer cardId) throws IOException {
//        controller.showNewCard(cardId);
//    }
//
//    @Override
//    public void showGameCreation(Level level, int playersNum, UUID game) throws Exception {
//
//    }
//
//    @Override
//    public void showGameJoining(String nickname, UUID game) throws Exception {
//
//    }
//
//    @Override
//    public void showColorSelection(String nickname, Colors color) throws Exception {
//
//    }
//
//    @Override
//    public void setFlightBoard(int loopLength, List<Integer> startingPositions) throws Exception {
//
//    }
//
//    @Override
//    public void setShipBoard(Set<Point> shipArea) throws Exception {
//
//    }
//
//    @Override
//    public void showStashUpdate(List<Integer> stashedComponentIds) throws Exception {
//
//    }
//
//    @Override
//    public void showComponentPositioning(int componentId, int direction, Point position) throws Exception {
//
//    }
//
//    @Override
//    public void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents) throws Exception {
//
//    }
//
//    @Override
//    public void sendForecastDeck(int deckIndex, List<Integer> cardsIds) throws Exception {
//
//    }
//
//    @Override
//    public void showNewHourglass() throws Exception {
//
//    }
//
//    @Override
//    public void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace) throws Exception {
//
//    }
//
//    @Override
//    public void showComponentRemoval(Point position) throws Exception {
//
//    }
//
//    @Override
//    public void showStatsUpdate() throws Exception {
//
//    }
//
//    @Override
//    public void showUpdateBatteries(Point position, int batteries) throws Exception {
//
//    }
//
//    @Override
//    public void showChoice(List<String> choices) throws Exception {
//
//    }
//
//    @Override
//    public void showUpdateCrew(Point position, int crew, CrewType crewType) throws Exception {
//
//    }
//
//    @Override
//    public void showProjectile(ProjectileType projectileType, int direction, int roll) throws Exception {
//
//    }
//
//    @Override
//    public void showUpdateCargoHold(Point position, List<GoodsType> goods) throws Exception {
//
//    }
//
//    @Override
//    public void showUpdateGoodBuffer(List<GoodsType> goods) throws Exception {
//
//    }
//
//    @Override
//    public void setSelectablePoints(List<Point> points) throws Exception {
//
//    }
//
//    @Override
//    public void showFinalStats() throws Exception {
//
//    }
//
//    @Override
//    public void reportError(String details) throws RemoteException {
//
//    }
//
//    @Override
//    public void handleEvent(Event event) {}
//
//    @Override
//    public void handleEvent(NewCardEvent newCardEvent) throws IOException {
//        controller.showNewCard(newCardEvent.cardId());
//    }
//}
