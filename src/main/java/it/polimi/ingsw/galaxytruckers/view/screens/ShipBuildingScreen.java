package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.io.IOException;

public class ShipBuildingScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        model.getComponentBank().getDescription().forEach(System.out::println);
        model.getMyShipBoard().getDescription().forEach(System.out::println);
        System.out.println("N) to pick new piece");
        System.out.println("select one piece by number");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {

    }
}
