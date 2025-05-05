package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ShipBuildingScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        model.getComponentBank().getDescription().forEach(System.out::println);
        //model.getMyShipBoard().getDescription().forEach(System.out::println);
        model.getAllShips().getDescription().forEach(System.out::println);
        System.out.println("N) to pick new piece\tS) stash current component");
        System.out.println("select one piece on the board by number OR from stashed by letter");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {

    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {

    }
}
