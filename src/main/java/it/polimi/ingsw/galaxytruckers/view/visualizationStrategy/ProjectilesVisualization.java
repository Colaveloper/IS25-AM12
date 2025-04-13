package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

public class ProjectilesVisualization implements VisualizationStrategy {
    @Override
    public void showCLI(ClientGameModel model) {
        model.getMyShipBoard().getDescription().forEach(System.out::println);
        System.out.println(model.getCurrentProjectile().getDescription());
    }
}
