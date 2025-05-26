package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

public abstract class CliScreen {

    ClientModel model;
    GameState gameState;
    ControllerToServer controller;
    List<StateActions> availableActions;

    CliFlightBoard flightBoard;
    CliAllShips allShips;

    public CliScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
        this.gameState = gameState;
        availableActions = gameState.getAvailableActions();
        this.flightBoard = new CliFlightBoard(model);
        this.allShips = new CliAllShips(model.getShipToPlayer());
    }

    public abstract void render();

    public abstract void parseAndInvoke(String input) ;

    protected void printShips() {
        System.out.println(flightBoard.getDescription());
        System.out.println(allShips.getDescription());
    }

    protected void printActions() {
        List<String> actions = new ArrayList<>();
        for(StateActions action : availableActions){
            switch(action){
                case ACTIVATE_COMPONENT -> {
                    actions.add("P [x] [y] \tPlace unwelded component");
                }
                case SPEND_BATTERIES -> {
                    actions.add("P [x] [y] \tSpend battery on component");
                }
                case GRAB_REWARD -> {
                    actions.add("P       \tTo pick reward");
                    actions.add("R       \tTo reject reward");
                }
                case CHOOSE_SHIP_PIECE -> {
                    actions.add("[i]     \tChoose piece of ship to keep");
                }
                case GO_NEXT, RELEASE_FORECAST -> {
                    actions.add("        \tPress any key to continue");
                }
                case LOSE_CREW -> {
                    actions.add("L [x] [y] \tremove crew from component");
                }
                case LOSE_GOOD  -> {
                    actions.add("R [x] [y] \tRemove valuable good from cargo hold");
                }
                case REMOVE_GOOD -> {
                    actions.add("R [x] [y] \tPick goods from cargo hold");
                }
                case ADD_GOOD -> {
                    actions.add("P [x] [y] \tPlace goods on cargo hold");
                }
                case CHOOSE_PLANET -> {
                    actions.add("[i]     \tLand on i-th planet");
                }
                case REQUEST_RAND_COMPONENT -> {
                    actions.add("C       \tGet New covered component");
                }
                case REQUEST_COMPONENT -> {
                    actions.add("U [i]   \tPick i-th uncovered component");
                }
                case REJECT_COMPONENT -> {
                    actions.add("R       \tRejected component");
                }
                case STASH_COMPONENT -> {
                    actions.add("S       \tTo stash current component");
                }
                case GRAB_STASHED_COMPONENT -> {
                    actions.add("S [i]   \tTo grab i-th stashed component");
                }
                case PLACE_COMPONENT -> {
                    actions.add("P [x] [y] \tPlace unwelded component in x, y");
                }
                case FLIP_HOURGLASS -> {
                    actions.add("H       \tTo Flip hourglass");
                }
                case PLACE_SHIP_ON_FLIGHTBOARD -> {
                    actions.add("E [i]   \tend and place on flightboard");
                }
                case FINISH_BUILDING -> {
                    actions.add("X       \tTo finish building");
                }
                case ACQUIRE_FORECAST -> {
                    actions.add("F [i]   \tTo get i-th forecast");
                }
                case DRAW_CARD -> {
                    actions.add("        \tPress any key to draw a card");
                }
                case REMOVE_COMPONENT -> {
                    actions.add("P [x] [y] \tRemove component in x, y");
                }
                case INITIALIZE_CABIN -> {
                    actions.add("P [x] [y] \tInitialize cabin in x, y");
                }
                case GIVE_UP -> {
                    actions.add("X       \tTo give up");
                }
            }
        }
        for(int i = 0; i < availableActions.size(); i++){
            System.out.print(actions.get(i));
            if ((i + 1) % 4 == 0) {
                System.out.println();
            }
        }
    }

    public boolean isLegalInput(String input) {
        for(StateActions action : availableActions){
            switch(action){
                case ACTIVATE_COMPONENT -> {
                    //todo
                }
                case SPEND_BATTERIES -> {
                }
                case GRAB_REWARD -> {
                }
                case CHOOSE_SHIP_PIECE -> {
                }
                case DRAW_CARD -> {
                }
                case LOSE_CREW -> {
                }
                case LOSE_GOOD -> {
                }
                case ADD_GOOD -> {
                }
                case REMOVE_GOOD -> {
                }
                case GO_NEXT -> {
                }
                case CHOOSE_PLANET -> {
                }
                case REQUEST_RAND_COMPONENT -> {
                }
                case REQUEST_COMPONENT -> {
                }
                case REJECT_COMPONENT -> {
                }
                case STASH_COMPONENT -> {
                }
                case GRAB_STASHED_COMPONENT -> {
                }
                case PLACE_COMPONENT -> {
                }
                case FLIP_HOURGLASS -> {
                }
                case PLACE_SHIP_ON_FLIGHTBOARD -> {
                }
                case FINISH_BUILDING -> {
                }
                case ACQUIRE_FORECAST -> {
                }
                case RELEASE_FORECAST -> {
                }
                case REMOVE_COMPONENT -> {
                }
                case INITIALIZE_CABIN -> {
                }
                case GIVE_UP -> {

                }
                default -> {
                    return false;
                }
            }
        }
        return false;
    }

}
