package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;

import java.io.IOException;

public class CliNewCardScreen extends CliScreen {
    AdventureCard adventureCard;

    public CliNewCardScreen(ClientModel model, ControllerToServer controller, GameState gameState){
        super(model, controller, gameState);
    }

    @Override
    public void render() {
        System.out.println("A new card has been drawn:\n");
        System.out.println(CliAdventureCard.getNewDescription(adventureCard));
    }

    @Override
    public void parseAndInvoke(String input) {
        controller.goNext();
    }
}