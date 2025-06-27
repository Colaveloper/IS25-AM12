package it.polimi.ingsw.galaxytruckers.client.view.cli.cliScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;

/**
 * ClIScreen for choosing a nickname in the CLI version of Galaxy Truckers.
 */
public class CliNicknameChoiceScreen extends CliScreen {

    /**
     * Creates a new nickname choice screen with the given model and controller.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliNicknameChoiceScreen(ClientModel model, ClientControllerInterface controller) {
        super(model, controller);
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.registerNickname(input);
    }

    @Override
    public boolean isInputLegal(String input) {
        return !input.trim().isEmpty();
    }

    @Override
    public void render() {
        System.out.println("Successfully bound to the server ✅");
        System.out.println("Please choose a unique nickname in order to proceed: ");
    }
}
