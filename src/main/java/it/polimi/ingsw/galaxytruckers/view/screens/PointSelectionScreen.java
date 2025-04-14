package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.ClientModel;

import java.awt.*;
import java.rmi.RemoteException;

public class PointSelectionScreen implements ScreenStrategy {

    @Override
    public void showCLI(ClientModel model) {

        model.getFlightBoard().getDescription().forEach(System.out::println);

        System.out.println("Please write the coordinates of one of the following points, then press enter.");
        System.out.println("For example: 4 2");

        model.getMyShipBoard().getDescription().forEach(System.out::println);

    }

    @Override
    public boolean isLegalInput(ClientModel model, String input) {
        if (!input.matches("\\d+ \\d+")) return false;

        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        Point p = new Point(x, y);
        return model.getSelectablePoints().contains(p);
    }

    @Override
    public void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws RemoteException {

    }
}
