package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;
import it.polimi.ingsw.galaxytruckers.view.Shipboard;

import java.util.List;
import java.util.Map;

public class PointSelectionVisualization implements VisualizationStrategy {

    @Override
    public void showCLI(ClientGameModel model) {

        model.getFlightBoardDescription().forEach(System.out::println);

        System.out.println("Please write the coordinates of one of the following points, then press enter.");
        System.out.println("For example: 4 2");

        model.getMyShipBoardDescription().forEach(System.out::println);
    }
}
