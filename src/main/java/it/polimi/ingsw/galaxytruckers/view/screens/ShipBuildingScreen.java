package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.Physical;
import it.polimi.ingsw.galaxytruckers.view.Shipboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShipBuildingScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        model.getComponentBank().getDescription().forEach(System.out::println);
        //model.getMyShipBoard().getDescription().forEach(System.out::println);
        model.getAllShips().getDescription().forEach(System.out::println);
        System.out.println("N) to pick new piece");
        System.out.println("select one piece on the board by number or from stashed by letter");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {

    }
}
