package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;
import java.util.List;

public class CliGoodsScreen extends CliScreen {

    public CliGoodsScreen(ClientModel model, ControllerToServer controller) {
        super(model, controller);
    }

    @Override
    public boolean isLegalInput(String input) {
        return false;
    }

    @Override
    public void parseAndInvoke(String input) {

    }

    @Override
    protected List<String> getNewDescription() throws IOException {
        return List.of();
    }
}
