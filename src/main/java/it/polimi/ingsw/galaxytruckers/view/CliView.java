package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;

import java.util.Scanner;

public class CliView extends View {

    String input;
    Scanner scanner;
    CliScreen currentScreen;

    public CliView(ControllerToServer controller, ClientModel model) {
        super(controller, model);
        startInputLoop();
        scanner = new Scanner(System.in);
    }

    @Override
    public void updateScreen() {
        currentScreen = new ScreenFactory().createCliScreen(model, controller);
        currentScreen.render();
    }

    @Override
    public void refresh() {
        currentScreen.render();
    }

    @Override
    public void start() {
        updateScreen();
    }

    private void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if(model.isCheatOn() && !CheatCodes.cheatEmpty()){
                    try {
                        input = CheatCodes.cheat();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    input = scanner.nextLine();
                }

                // letting the user correct format errors
                while (!currentScreen.isInputLegal(input)) {
                    //todo: this is called also when it s not your turn where u don t have to check invalid input format
                    //todo: ask the screen what to print, screens then prints either not your turn or a specific message
                    //screen.invalidInput();
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                };

                currentScreen.parseAndInvoke(input);
            }
        }).start();
    }
}
