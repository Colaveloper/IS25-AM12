package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

public class CliNewCardScreen extends CliScreen {

    private DrawCardState gameState;
    public CliNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState gameState){
        super(model, controller, gameState);
        this.gameState = gameState;
    }

    @Override
    public void render() {
        printShips();
        printActions();
        System.out.println("A new card has been drawn:\n");
        System.out.println(new CliAdventureCard(model).getNewDescription());
    }

    @Override
    public void parseAndInvoke(String input) {
        if(input.equalsIgnoreCase("X")){
            controller.giveUp();
        }
        else{
            // TODO: probably needs fixing
            controller.goNext();
        }
    }
}