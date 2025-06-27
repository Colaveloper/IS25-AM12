package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;

import java.util.List;

/**
 * CLI screen for displaying a newly drawn adventure card in the Galaxy Truckers game.
 */
public class CliNewCardScreen extends CliAdventureScreen {
    private CliAdventureCard adventureCard;
    private boolean hasDrawn;

    /**
     * Creates a new screen for displaying a newly drawn adventure card.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current state of the game, including drawn cards
     */
    public CliNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState gameState){
        super(model, controller, gameState);
        hasDrawn = gameState.hasDrawn();
        if (hasDrawn) {
            adventureCard = new CliAdventureCard(gameState.getCurrentCard());
        }
    }

    @Override
    public void render() {
        printShipFlightStats().forEach(System.out::println);
        if(imOut) {
            System.out.println("you surrendered");
            return;
        }
        if(!hasDrawn && !isMyTurn){
            System.out.println("Wait for leader to draw");
        }
        if (hasDrawn) {
            System.out.println("A new card has been drawn:\n");
            DescriptionUtils.borderAndTitle(adventureCard.getDescription(), "Current card").forEach(System.out::println);
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
                if(isMyTurn && hasDrawn){
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
        hasDrawn = adventureCard != null;
//        if(adventureCard == null) this.adventureCard = null; else
        this.adventureCard = new CliAdventureCard(adventureCard);
    }
}