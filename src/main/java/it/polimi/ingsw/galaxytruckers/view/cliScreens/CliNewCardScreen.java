package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;

import java.util.List;

public class CliNewCardScreen extends CliAdventureScreen {

    private CliAdventureCard adventureCard;
    private boolean hasDrown;
    public CliNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState gameState){
        super(model, controller, gameState);
        hasDrown = false;
        //adventureCard = new CliAdventureCard(gameState.getGame().getCurrentAdventureCard());
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if(!hasDrown && !isMyTurn){
            System.out.println("Wait for leader to draw");
        }
        if (hasDrown) {
            System.out.println("A new card has been drawn:\n");
            List<String> descriptions = adventureCard.getDescription();
            descriptions.forEach(System.out::println);
        }
        printActions();
    }

    @Override
    public void parseAndInvoke(String input) {
        String command = input.split(" ")[0];
        switch (command.toUpperCase()) {
            case "Y":
                controller.giveUp();
                break;
            case "":
                if(isMyTurn && hasDrown){
                    controller.goNext();
                }
                else if(isMyTurn){
                    controller.drawCard();
                }
                else{
                    System.out.println("wait for the leader to continue");
                }
        }
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        hasDrown = adventureCard != null;
        if(adventureCard == null) this.adventureCard = null;
        else this.adventureCard = new CliAdventureCard(adventureCard);
    }
}