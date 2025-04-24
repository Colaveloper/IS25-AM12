package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;

import java.io.IOException;
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
        input = scanner.nextLine();

        // letting the user correct format errors
        while (!strategy.isLegalInput(model, input)) {
            System.out.println("Invalid format, please check your input");
            input = scanner.nextLine();
        };

        strategy.parseAndInvoke(model, input, server);
    }
}
