package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.VisualizationStrategy;

public abstract class View {
    final ClientGameModel model;
    final ClientController controller;

    public View (ClientGameModel model, ClientController controller) {
        this.model = model;
        this.controller = controller;
    }

    public abstract void show(VisualizationStrategy strategy);
}
