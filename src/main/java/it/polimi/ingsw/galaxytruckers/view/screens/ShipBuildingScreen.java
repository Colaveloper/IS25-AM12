package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.io.IOException;

public class ShipBuildingScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {
        model.getShipBuilder().getDescription().forEach(System.out::println);
        model.getMyShipBoard().getDescription().forEach(System.out::println);
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {

    }
}
