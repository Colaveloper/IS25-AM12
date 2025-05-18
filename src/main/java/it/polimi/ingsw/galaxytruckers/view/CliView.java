package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.io.IOException;
import java.util.Scanner;

public class CliView implements View, InvalidationListener {
    Scanner scanner;
    String input;
    static ClientModel model;
    static ClientController controller;
    CliScreen screen;

    public CliView() {
        startInputLoop();
        scanner = new Scanner(System.in);
    }

    @Override
    public void setModel(ClientModel model) {
        CliView.model = model;
    }

    @Override
    public void setController(ClientController controller) {
        CliView.controller = controller;
    }

    @Override
    public void setScreen(ScreenFactory screenFactory) {
        try {
            screen = screenFactory.getCliScreen(model, controller);
        } catch (IOException e) {
            controller.reportError("Unable to create the screen due to an IO error: "+e.getMessage());
        }
        screen.addListener(this);
        this.refreshScreen();
    }

    @Override
    public void invalidated(Observable observable) {
        refreshScreen();
    }

    private void refreshScreen() {
        // clearing the console (not supported in intellij, use Windows terminal)
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\n");

        // showing the visualization
        System.out.println("█".repeat(150));
        try {
            screen.getDescription().forEach(System.out::println);
        } catch (IOException e) {
            controller.reportError("Unable to show the screen due to an IO error: "+e.getMessage());
        }
        System.out.flush();
    }

    public void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                input = scanner.nextLine();

                // letting the user correct format errors
                while (!screen.isLegalInput(input)) {
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                };

                screen.parseAndInvoke(input);
            }
        }).start();
    }
}
