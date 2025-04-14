package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screen.ScreenStrategy;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class CliView extends View{
    Scanner scanner;
    String input;

    public CliView(ClientModel model, VirtualServer server) {
        super(model, server);
        scanner = new Scanner(System.in);
    }

    public void run(ScreenStrategy strategy) throws IOException {
        // TODO: clearing the console

        // showing the visualization
        strategy.showCLI(model);

        // checking user input
        do {
            input = scanner.nextLine();
        } while (!strategy.isLegalInput(model, input));

        strategy.parseAndInvoke(model, input, server);
    }
}
