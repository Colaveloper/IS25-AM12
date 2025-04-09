package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.VisualizationStrategy;

public abstract class View {
    final ClientGameModel model;

    public View (ClientGameModel model) {
        this.model = model;
    }

    public abstract void show(VisualizationStrategy strategy);
}
