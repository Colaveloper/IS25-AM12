package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;

public class CliEndGameScreen extends CliScreen {

    public CliEndGameScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public void render() {
        System.out.println("GAME OVER\n\n\n");
        model.getFinalScores().forEach((key, value) -> {
            System.out.println(key + ": " + value);
        });
        System.out.println("\n\n\n\n press ENTER to start new game");
    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.isEmpty()) model.setMetaState(MetaState.JOINORCREATE);
    }
}
