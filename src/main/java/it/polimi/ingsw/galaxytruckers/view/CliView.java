package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliNicknameChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;

import java.util.Scanner;

public class CliView implements View {
    ClientModel model;
    ClientController controller;
    ScreenFactory screenFactory;
    CliScreen currentScreen;
    MetaState metaState;

    public CliView(ClientController controller, ClientModel model) {
        startInputLoop();
        this.controller = controller;
        this.model = model;
        this.screenFactory = new ScreenFactory();
    }

    @Override
    public void updateScreen() {
        if (isToUpdate()) {
            currentScreen = screenFactory.createCliScreen(model, controller);
        }
        currentScreen.render();
    }

    private boolean isToUpdate() {
        if (model.getMetaState() != metaState) return true;
        return model.getGame().getCurrentState() != null &&
                model.getGame().getCurrentState().getClass() != currentScreen.getState().getClass();
    }

    private void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
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
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                }

                currentScreen.parseAndInvoke(input);
            }
        }).start();
    }
}
