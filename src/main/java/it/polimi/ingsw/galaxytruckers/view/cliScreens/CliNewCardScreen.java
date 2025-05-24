package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliNewCardScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;
    CliAdventureCard adventureCard;

    public CliNewCardScreen(ClientModel model, ControllerToServer controller, GameState gameState){
        super(model, controller, gameState);
        try{
            adventureCard = new CliAdventureCard(model, model.getGame().getCurrentAdventureCard().getId());
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        flightBoard = new CliFlightBoard(model);
        allShips = new CliAllShips(model);
    }

    @Override
    public void render() {
        System.out.println("Current Leader: "+model.getCurrentLeader()+" just drew: " + adventureCard.getCardName());
        System.out.println(flightBoard.getNewDescription());
        System.out.println(allShips.getNewDescription());
        adventureCard.getNewDescription().forEach(System.out::println);
        if (model.isMyTurn()) {
            System.out.println("it's your turn, press any key to continue");
        }
        else {
            System.out.println(model.getCurrentPlayerNickname() + " is choosing");
        }
    }

    @Override
    public void parseAndInvoke(String input) {
        if(model.isMyTurn()) {
            controller.goNext();
        }
    }
}