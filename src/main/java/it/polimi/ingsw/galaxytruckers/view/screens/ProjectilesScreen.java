package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ProjectilesScreen implements ScreenStrategy {
    @Override
    public void showCLI(ClientModel model) {
        model.getMyShipBoard().getDescription().forEach(System.out::println);
        System.out.println(model.getCurrentProjectile().getDescription());
    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        return false; // TODO
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
    // TODO
    }

    @Override
    public void showGUI(ClientModel model, Pane root) {

    }
}
