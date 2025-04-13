package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

import java.sql.SQLOutput;

public class PlanetVisualization implements VisualizationStrategy{

    @Override
    public void showCLI(ClientGameModel model) {
        model.getPlanets().getDescription().forEach(System.out::println);
        System.out.println("Please write the number of the planet you wish to land on, then press enter.");
        System.out.println("For example, to land on the second planet type: 2");
    }
}
