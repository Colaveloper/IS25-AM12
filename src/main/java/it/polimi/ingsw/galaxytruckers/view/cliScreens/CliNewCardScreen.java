package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.state.RemoveGoodsState;

public class CliNewCardScreen extends CliScreen {

    private CliAdventureCard adventureCard;
    private final boolean imLeader;
    private boolean hasDrown;
    public CliNewCardScreen(ClientModel model, ControllerToServer controller, DrawCardState gameState){
        super(model, controller, gameState);
        imLeader = gameState.getShipBoard() == model.getMyShip();
        hasDrown = false;
        //adventureCard = new CliAdventureCard(gameState.getGame().getCurrentAdventureCard());
    }

    @Override
    public void render() {
        cliFlightBoard.getDescription().forEach(System.out::println);
        cliAllShips.getDescription().forEach(System.out::println);
        if(!hasDrown && !imLeader){
            System.out.println("Wait for leader to draw");
        }
        if (hasDrown) {
            System.out.println("A new card has been drawn:\n");
            adventureCard.getDescription().forEach(System.out::println);
        }
        printActions();
    }

    @Override
    public boolean isInputLegal(String input) {
        if(!imLeader){
            System.out.println("wait for leader");
            return false;
        }
        return isFormatLegal(input);
    }

    @Override
    public void parseAndInvoke(String input) {
        String command = input.split(" ")[0];
        switch (command.toUpperCase()) {
            case "Y":
                controller.giveUp();
                break;
            case "":
                if(imLeader && hasDrown){
                    controller.goNext();
                }
                else if(imLeader){
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