package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.rmi.RemoteException;

public class NewCardScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        System.out.println("Current Leader: "+model.getCurrentLeader()+" just drew: " + model.getCardName());
        model.getMyShipBoard().getDescription().forEach(System.out::println);
        model.getCurrentCard().getDescription().forEach(System.out::println);
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws RemoteException {
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {
        root.getChildren().clear();
        root.getChildren().add(model.getCurrentCard().getNode());
    }
}