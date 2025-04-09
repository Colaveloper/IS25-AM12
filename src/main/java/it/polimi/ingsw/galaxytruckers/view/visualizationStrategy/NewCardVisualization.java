package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

public class NewCardVisualization implements VisualizationStrategy {

    @Override
    public void showCLI(ClientGameModel model) {

        System.out.println("Current Leader: "+model.getCurrentPlayerNickname()+" just drew:\n");

        // TODO: implement model.getCurrentCardDescription()
//        model.getCurrentCardDescription().forEach(System.out::println);

        System.out.println("PLACEHOLDER OF THE CARD");
    }
}
