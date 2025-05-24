package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliNewCardScreen extends CliScreen {

    CliFlightBoard flightBoard;
    CliAllShips allShips;
    CliAdventureCard adventureCard;

    public CliNewCardScreen(ClientModel model, ControllerToServer controller) throws IOException {
        super(model, controller);

        adventureCard = new CliAdventureCard(model, model.getCurrentCard());

        flightBoard = new CliFlightBoard(model);
        flightBoard.addListener(this);

        allShips = new CliAllShips(model);
        allShips.addListener(this);
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<String> output = new ArrayList<>();

        System.out.println("Current Leader: "+model.getCurrentLeader()+" just drew: " + adventureCard.getCardName());
        output.addAll(flightBoard.getDescription());
        output.addAll(allShips.getDescription());
        adventureCard.getDescription().forEach(System.out::println);
        if (model.isMyTurn()) {
            output.add("it's your turn, press any key to continue");
        }
        else {
            output.add(model.getCurrentPlayerNickname() + " is choosing");
        }
        return output;
    }

    @Override
    public boolean isLegalInput(String input) {
        return true;
    }

    @Override
    public void parseAndInvoke(String input) {
        if(model.isMyTurn()) {
            controller.goNext();
        }
    }
}