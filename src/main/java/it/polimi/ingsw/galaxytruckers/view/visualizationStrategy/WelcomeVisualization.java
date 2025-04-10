package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

import java.util.Scanner;

public class WelcomeVisualization implements VisualizationStrategy {

    @Override
    public void showCLI(ClientGameModel model) {
        System.out.println("You've successfully bound to the server!");
        System.out.println("Please choose a unique nickname in order to proceed: ");
    }
}
