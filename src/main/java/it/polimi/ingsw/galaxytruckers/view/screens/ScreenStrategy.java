package it.polimi.ingsw.galaxytruckers.view.screens;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public abstract class ScreenStrategy extends CliElement {

    public ScreenStrategy(ClientModel model) {
        super(model);
    }

    public abstract boolean isLegalInput(ClientModel model, String input);
    public abstract void parseAndInvoke(ClientModel model, String input, VirtualServer server) throws IOException;
    public abstract void showGUI(ClientModel model, Pane root, VirtualServer server) throws IOException;
}
