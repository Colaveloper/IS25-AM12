package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.List;

public abstract class CliScreen {

    ClientModel model;
    GameState gameState;
    ControllerToServer controller;

    public CliScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
        this.gameState = gameState;
    }

    public abstract void render();
    public abstract void parseAndInvoke(String input) ;
    // public abstract boolean isLegalInput(String input);
    public boolean isLegalInput(String input) {
        List<StateActions> availableActions = gameState.getAvailableActions();
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
                    return false;//todo
                }
            }
        }
        return false;//todo
    }

}
