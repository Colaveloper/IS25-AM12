package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class GoodsScreen implements ScreenStrategy{

    @Override
    public void showCLI(ClientModel model) {
        // show the goods buffer
        model.getGoodsBuffer().getDescription().forEach(System.out::println);

        // show the player's shipboard
        model.getMyShipBoard().getDescription().forEach(System.out::println);

        // TODO: also select where to place the good on the shipboard
        System.out.println("Type the index of the good you want, then press enter.");
        System.out.println("For example, to grab the second good in the buffer, type: 2");

    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        // TODO: complete this method
        return false;
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException {
        // TODO: complete this method
    }

    @Override
    public void showGUI(ClientModel model, Pane root, VirtualServer server) {

    }
}
