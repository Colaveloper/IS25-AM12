package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.io.IOException;

public class PlanetScreen implements ScreenStrategy{

    @Override
    public void showCLI(ClientModel model) {
        model.getPlanets().getDescription().forEach(System.out::println);
        System.out.println("Please write the number of the planet you wish to land on, then press enter.");
        System.out.println("For example, to land on the second planet type: 2");
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false; // TODO
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        // TODO
    }
}
