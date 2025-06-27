package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

/**
 * CLI screen for displaying the end game state.
 */
public class CliEndGameScreen extends CliScreen {

    /**
     * Constructor for the end game screen.
     *
     * @param model      the client model containing the game state
     * @param controller the client controller to handle server communication
     */
    public CliEndGameScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    /**
     * {@inheritDoc}
     *
     * @param input The command string entered by the user
     */
    @Override
    public boolean isInputLegal(String input) {
        return input.isEmpty();
    }

    /**
     * {@inheritDoc}
     * This method is called to render the end game screen.
     * It displays the final scores of all players and prompts to start a new game.
     */
    @Override
    public void render() {
        System.out.println("GAME OVER\n\n");
        model.getFinalScores().forEach((key, value) -> {
            System.out.println(key.getNickname() + ": " + value);
        });
        System.out.println("\n\n\n\n press ENTER to start new game");
    }

    /**
     * {@inheritDoc}
     * This method is called to parse the input and update the model's state.
     * In this case, it sets the meta-state to JOINORCREATE if the input is empty.
     *
     * @param input The command string entered by the user
     */
    @Override
    public void parseAndInvoke(String input) {
        if (input.isEmpty()) controller.clearModel();
    }
}
