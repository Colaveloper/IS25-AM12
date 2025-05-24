package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public class GuiView extends View {
    public GuiView(ControllerToServer controller, ClientModel model) {
        super(controller, model);
    }

    @Override
    public void setScreen(GameState gameState) {

    }
}
