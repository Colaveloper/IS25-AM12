package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;

import java.io.IOException;
import java.util.Scanner;

public class CliView implements View{
    Scanner scanner;
    String input;
    static ClientModel model;
    static VirtualServer server;

    public CliView() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void setModel(ClientModel model) {
        CliView.model = model;
    }

    @Override
    public void setServer(VirtualServer server) {
        CliView.server = server;
    }

    @Override
    public void run(ScreenStrategy strategy) throws IOException {
        // clearing the console (not supported in intellij, use Windows terminal)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // showing the visualization
        strategy.showCLI(model);
        System.out.flush();

        input = scanner.nextLine();

        // letting the user correct format errors
        while (!strategy.isLegalInput(model, input)) {
            System.out.println("Invalid format, please check your input");
            input = scanner.nextLine();
        };

        strategy.parseAndInvoke(model, input, server);
    }
}
