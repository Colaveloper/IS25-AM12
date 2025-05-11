package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.screens.ScreenStrategy;
import javafx.beans.property.Property;

import java.io.IOException;
import java.util.Scanner;

public class CliView implements View, ChangeListener {
    Scanner scanner;
    String input;
    static ClientModel model;
    static VirtualServer server;
    ScreenStrategy strategy;

    public CliView() {
        startInputLoop();
        scanner = new Scanner(System.in);
    }

    @Override
    public void setModel(ClientModel model) {
        CliView.model = model;
        model.getComponentBank().setChangeListener(this);
        for(Shipboard shipboard : model.getShipboards().values()) {
            shipboard.setChangeListener(this);
        }
        model.getAllShips().setChangeListener(this);
    }

    @Override
    public void setServer(VirtualServer server) {
        CliView.server = server;
    }

    @Override
    public void setScreen(ScreenStrategy newStrategy) throws IOException {
//        if (!newStrategy.equals(strategy)) {
            strategy = newStrategy;

            // clearing the console (not supported in intellij, use Windows terminal)
            System.out.print("\033[H\033[2J");
            System.out.flush();
            System.out.println("\n");

            // showing the visualization
            System.out.println("█".repeat(150));
            strategy.showCLI(model);
            System.out.flush();
//        }
    }

    public void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                input = scanner.nextLine();

                // letting the user correct format errors
                while (!strategy.isLegalInput(model, input)) {
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                };

                try {
                    strategy.parseAndInvoke(model, input, server);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    // TODO: remove
    public void refresh() {
        strategy.showCLI(model);
    }

    @Override
    public void onChanged() throws IOException { // refreshing
        this.setScreen(strategy);
    }
}
