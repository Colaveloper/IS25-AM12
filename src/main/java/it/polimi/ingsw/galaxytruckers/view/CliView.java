package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.VisualizationStrategy;

public class CliView extends View{

    public CliView(ClientGameModel model) {
        super(model);
    }

    public void show(VisualizationStrategy strategy) {
        strategy.showCLI(model);
    }
}
