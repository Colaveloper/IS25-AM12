package it.polimi.ingsw.galaxytruckers.view.visualizationStrategy;

import it.polimi.ingsw.galaxytruckers.view.ClientGameModel;

public class NewCardVisualization implements VisualizationStrategy {

    @Override
    public void showCLI(ClientGameModel model) {
        System.out.println("Current Leader: "+model.getCurrentPlayerNickname()+" just drew:" + model.getCardName());
        model.getMyShipBoard().getDescription().forEach(System.out::println);
        model.getCurrentCard().getDescription().forEach(System.out::println);
    }
}
//
//for (String line : model.getMyShipBoardDescription()) {
//        System.out.println(line);
//        }
//                //System.out.println(model.getMyShipBoardDescription());
//                System.out.println(model.getCardDescription());
//