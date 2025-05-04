package it.polimi.ingsw.galaxytruckers.network.client.socket;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualClient;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SocketClient implements VirtualClient {
    private final ClientController controller;
    private final ObjectOutputStream out;
//    private final ObjectInputStream in;
    private static final int PORT = 1235;

    public SocketClient(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
//        this.in = new ObjectInputStream(socket.getInputStream());
        this.controller = new ClientController(new SocketVirtualServer(out));

//        new Thread(this::listenToServer).start();
    }

    public static void main(String[] args) throws Exception {
        String host = args[0]; // e.g., "localhost"
        try (Socket socket = new Socket(host, PORT)) {
            SocketClient client = new SocketClient(socket);
            client.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

//    private void listenToServer() {
//        try {
//            while (true) {
//                Object obj = in.readObject();
//                // Example: handle incoming server messages/events
//                System.out.println("Received: " + obj);
//            }
//        } catch (Exception e) {
//            System.out.println("Disconnected from server.");
//        }
//    }

    private void run() throws IOException {
        controller.showConnectedAndNicknameChoice(String.valueOf(this.hashCode()));
    }

    @Override
    public void showNicknameRegistration(String nickname) throws RemoteException {
        controller.setNickname(nickname);
    }

    @Override
    public void showGameCreation(Level level, int playersNum, UUID game) throws Exception {

    }

    @Override
    public void showGameJoining(String nickname, UUID game) throws Exception {

    }

    @Override
    public void showColorSelection(String nickname, Colors color) throws Exception {

    }

    @Override
    public void setFlightBoard(int loopLength, List<Integer> startingPositions) throws Exception {

    }

    @Override
    public void setShipBoard(Set<Point> shipArea) throws Exception {

    }

    @Override
    public void showStashUpdate(List<Integer> stashedComponentIds) throws Exception {

    }

    @Override
    public void showComponentPositioning(int componentId, int direction, Point position) throws Exception {

    }

    @Override
    public void showUncoveredUpdate(List<Integer> uncoveredComponentIds, int coveredComponents) throws Exception {

    }

    @Override
    public void showForecast(List<Integer> cardsIds) throws Exception {

    }

    @Override
    public void showNewHourglass() throws Exception {

    }

    @Override
    public void showPlayerToPlaceUpdate(Map<String, Integer> playerToPlace) throws Exception {

    }

    @Override
    public void showComponentRemoval(Point position) throws Exception {

    }

    @Override
    public void showStatsUpdate() throws Exception {

    }

    @Override
    public void showUpdateBatteries(Point position, int batteries) throws Exception {

    }

    @Override
    public void showChoice(List<String> choices) throws Exception {

    }

    @Override
    public void showUpdateCrew(Point position, int crew, CrewType crewType) throws Exception {

    }

    @Override
    public void showNewCard(Integer cardId) throws Exception {

    }

    @Override
    public void showProjectile(ProjectileType projectileType, int direction, int roll) throws Exception {

    }

    @Override
    public void showUpdateCargoHold(Point position, List<GoodsType> goods) throws Exception {

    }

    @Override
    public void showUpdateGoodBuffer(List<GoodsType> goods) throws Exception {

    }

    @Override
    public void setSelectablePoints(List<Point> points) throws Exception {

    }

    @Override
    public void showFinalStats() throws Exception {

    }

    @Override
    public void reportError(String details) throws Exception {

    }
}
