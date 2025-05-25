package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.allShips = new CliAllShips(model);
    }

    public abstract void render();

    public abstract void parseAndInvoke(String input) ;

    protected List<String> printShips() {
        List<String> ships = new ArrayList<>();
        ships.addAll(flightBoard.getDescription());
        ships.addAll(allShips.getDescription());
        return ships;
    }

    protected List<String> printActions() {
        List<String> actions = new ArrayList<>();
        for(StateActions action : availableActions){
            switch(action){
                case ACTIVATE_COMPONENT -> {
                    actions.add("A\t\tactivate component");
                }
                case SPEND_BATTERIES -> {
                    actions.add("A\t\tactivate component");
                }
                case GRAB_REWARD -> {
                    actions.add("A\t\tactivate component");
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
            }
        }
        return actions;
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
