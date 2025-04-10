package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.visualizationStrategy.VisualizationStrategy;

import java.util.Scanner;

public class CliView extends View{
    Scanner scanner;

    public CliView(ClientGameModel model, ClientController controller) {
        super(model, controller);
    }

    public void show(VisualizationStrategy strategy) {
        strategy.showCLI(model);
    }
}
