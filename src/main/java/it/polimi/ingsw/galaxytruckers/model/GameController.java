//import adventureCards.utils.CardState;
//
//import adventureCards.AdventureCardDeprecated;
//import java.util.List;
//
//public class GameController {
//    private GameModel model;
//    private GameView view;
//    private int currentPlayer;
//    private CardState choice;
//    private Boolean submit;
//
//    //example interaction between controller with view and model
////    public void startGame(){
////        model.startGame();
////        view.showWelcomeMessage(); //show the initial screen
////        play();
////    }
////
////    public void play(){
////        while(!model.isGameOver(){
////            view.displayBoard(model.getBoard()); //shipboard and flightboard
////            handlePlayerAction();
////            model.endTurn();
////        }
////        view.displayWinner(model.getWinner());
////    }
//
//    // Cards management
//    public void drawCard() {
//        model.drawCard();
//        currentPlayer = model.getCurrenPlayer();
//        view.displayCard(card);
//
//
//        while (choice = model.nextStep() != CardState.END_CARD) {
//            switch(choice){
//                case GRAB_GOODS -> model.grabGoods(view.getInput(currentPlayer));
//                case GRAB_CREDITS -> {      //useless if getting credits is automatic
//                    if (view.getInput(currentPlayer)){
//                        model.grabCredits();
//                    }
//                }
//                case CHOOSE_PLANET -> {
//                   //TODO: update model and view
//                }
//                case LOSE_RESIDENT -> {
//                    model.loseResident();
//                }
//                case SUBMIT_POWER -> {
//                    model.submitPower();
//                }
//                case ACTIVATE_SHIELD -> {
//                    for (Integer i : model.getProjectileDirections()) {
//                        if (model.checkShieldDirection(i)) {
//                            model.removeComponent(i);
//                        }
//                        else if (view.getInput(currentPlayer)) { //activate shield?
//                            model.removeComponent(i);
//                        }
//                    }
//                }
//
//                case ACTIVATE_CANNON -> {
//                    while(!submit) {
//                        model.askShipboard();
//                        model.activateCannon(view.getInput(currentPlayer));//
//                    }
//                }
//
//                case ACTIVATE_ENGINE -> {
//                    while(!submit) {
//                        model.activateEngine(view.getInput(currentPlayer));
//                    }
//                }
//
//                case PLACE_GOODS -> {
//                    model.placeGoods();
//                }
//                case CONSUME_BATTERIES -> {    //not necessary if put in lose goods
//                    //model.loseBatteries(n);
//                }
//                case ASK_NEXT_PLAYER -> {
//                    model.resetStep();
//                    flightBoard.nextPlayer();
//                    currentPlayer = flightBoard.getCurrentPlayer();
//                }
//
//                default -> view.showError("Invalid choice");
//
//            }
//        }
//    }
//}
