import adventureCards.utils.Choice;

import adventureCards.AdventureCard;
import java.util.List;

public class GameController {
    private GameModel model;
    private GameView view;

    //example interaction between controller with view and model
//    public void startGame(){
//        model.startGame();
//        view.showWelcomeMessage(); //show the initial screen
//        play();
//    }
//
//    public void play(){
//        while(!model.isGameOver){
//            view.displayBoard(model.getBoard()); //shipboard and flightboard
//            handlePlayerAction();
//            model.endTurn();
//        }
//        view.displayWinner(model.getWinner());
//    }

    // Cards management
    public void drawCard() {
        AdventureCard card = model.drawCard();
        view.displayCard(card);

        List<Choice> choices = card.getChoicesList();
        if (choices != null) {
            for (Choice choice : choices) {
                switch(choice){
                    case GRAB_GOODS -> model.grabGoods(view.getInput(model.getCurrentPlayer()));
                    case GRAB_CREDITS -> {
                        if (view.getInput(model.getCurrentPlayer())){
                            model.grabCredits();
                        }
                    }
                    case CHOOSE_PLANET -> {
                       //TODO: update model and view
                    }
                    //TODO: add remaining cases
                    default -> view.showError("Invalid choice");

                }
            }
        }
    }
}
