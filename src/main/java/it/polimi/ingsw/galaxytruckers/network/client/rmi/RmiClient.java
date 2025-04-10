package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.server.rmi.VirtualClientRmi;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.NewCardVisualization;

import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.List;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia RMI.
 */
public class RmiClient extends UnicastRemoteObject implements VirtualClientRmi {
    final VirtualServerRmi server;
    final ClientGameModel clientModel;
    final View view;

    public RmiClient(VirtualServerRmi server) throws RemoteException{
        super();
        this.server = server;
        clientModel = new ClientGameModel();
        this.view = new CliView(clientModel); // TODO: let the player choose his view
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        final String serverName = "GalacticServer";
        Registry registry = LocateRegistry.getRegistry(args[0], 1234);
        VirtualServerRmi server = (VirtualServerRmi) registry.lookup(serverName);
        new RmiClient(server).run();
    }

    private void run() throws IOException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws IOException {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("Insert command: ");
            String command = scan.nextLine();
            if (command.equals("submit")) {
                server.drawCard();
            }
        }
    }

    @Override
    public void showNewCard(Integer cardId) throws IOException {
        System.out.println("new card! the id is:"+cardId);
        clientModel.setCurrentCard(cardId);
        //TODO:sync
        view.show(new NewCardVisualization());
    }




    //TODO:if RmiClient creates the model this is necessary----------------------------------------------------
    public void setModelPersonalNickname(String nickname) {
        clientModel.setMyNickname(nickname);
    }

    public void setModelShipboards(Map<String, Shipboard> shipboards) {
        clientModel.setPlayerToShip(shipboards);
    }
    //TODO: ----------------------------IMPORTANT----------------------------------------------------------------





    @Override
    public void showNicknameRegistration(String nickname) throws Exception {

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
    public void reportError(String details) throws RemoteException {

    }
}
