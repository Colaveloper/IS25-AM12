package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CliScreen {

    protected ClientModel model;
    private GameState state;
    protected ControllerToServer controller;
    protected List<StateActions> availableActions;
    protected CliFlightBoard cliFlightBoard;


    public CliScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
        this.state = gameState;
        availableActions = gameState.getAvailableActions();
        this.cliFlightBoard = new CliFlightBoard(model.getGame().getFlightBoard());
        this.cliFlightBoard.addObserver(this::render);
    }

    public CliScreen(ClientModel model, ControllerToServer controller) {
        this.model = model;
        this.controller = controller;
    }

    public abstract void render();

    public abstract void parseAndInvoke(String input);

    protected void printShips() {
//        flightBoard.getNewDescription().forEach(System.out::println);
//        allShips.getNewDescription().forEach(System.out::println);//todo sistemare altri tipi di allships
    }

    protected void printActions() {
        if(!model.getGame().getGivenUpShips().contains(model.getClientPlayer())) {
            List<String> actions = new ArrayList<>();
            for (StateActions action : availableActions) {
                switch (action) {
                    case ACTIVATE_COMPONENT -> {
                        actions.add("P [x] [y] \tPlace unwelded component");
                    }
                    case SPEND_BATTERIES -> {
                        actions.add("B [x] [y] \tSpend battery on component");
                    }
                    case GRAB_REWARD -> {
                        actions.add("P       \tTo pick reward");
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
                    case LOSE_GOOD -> {
                        actions.add("[x] [y] \tRemove valuable good from cargo hold");
                    }
                    case REMOVE_GOOD -> {
                        actions.add("R [x] [y] [good type] \tPick goods from cargo hold");
                    }
                    case ADD_GOOD -> {
                        actions.add("P [x] [y] [good type] \tPlace goods on cargo hold");
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
            for (int i = 0; i < availableActions.size(); i++) {
                System.out.print(actions.get(i));
                if ((i + 1) % 4 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
        }
        else{
            //TODO: print something in place of actions
            System.out.println("You're out of the flight! - watch other players compete.");
        }
    }

    protected Point getPoint(String input) {
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new Point(x, y);
    }

    public boolean isInputLegal(String input) {
        return isFormatLegal(input);
    }

    //todo controlli dell'input dal model
    protected boolean isFormatLegal(String input) {
        String[] parts = input.split(" ");
        return switch (parts[0]) {
            case "P" -> availableActions.contains(StateActions.PLACE_COMPONENT) && input.matches("(?i)P\\s+\\d+\\s+\\d+") ||
                    availableActions.contains(StateActions.GRAB_REWARD) && input.matches("(?i)P");
            case "H" -> availableActions.contains(StateActions.FLIP_HOURGLASS) && input.matches("(?i)H");
            case "S" -> availableActions.contains(StateActions.STASH_COMPONENT) && input.matches("(?i)S") ||
                    availableActions.contains(StateActions.GRAB_STASHED_COMPONENT) && input.matches("(?i)S\\s+\\d+");
            case "R" -> availableActions.contains(StateActions.REJECT_COMPONENT) && input.matches("(?i)R");
            case "C" -> availableActions.contains(StateActions.REQUEST_COMPONENT) && input.matches("(?i)C");
            case "F" -> availableActions.contains(StateActions.ACQUIRE_FORECAST) && input.matches("(?i)F\\s+\\d+");
            case "X" -> availableActions.contains(StateActions.FINISH_BUILDING) && input.matches("(?i)X");
            case "L" -> availableActions.contains(StateActions.LOSE_CREW) && input.matches("(?i)L\\s+\\d+\\s+\\d+");
            case "B" -> availableActions.contains(StateActions.SPEND_BATTERIES) && input.matches("(?i)B\\s+\\d+\\s+\\d+");

            default -> {
                System.out.println("invalid input");
                yield false;
            }
        };
    }
}


//        for(StateActions action : availableActions){
//            switch(action){
//                case ACTIVATE_COMPONENT -> {
//                    //check if input = number + space + number
//                    if (checkFormat(input, "\\d+ \\d+")) return false;
//
//                    // check if the point made from those numbers is selectable
//                    if (!gameState.getAvailablePositions().contains(getPoint(input))){
//                        return false;
//                    }
//                }
//                case SPEND_BATTERIES -> {
//                    //check if input = number + space + number
//                    if (!input.matches("\\d+ \\d+")) return false;
//
//                    // check if the point made from those numbers is selectable
//                    return model.getMyShip().getBatteries().containsKey(getPoint(input));
//                }
//                case GRAB_REWARD -> {
//                    if (!input.matches("(?i)[pr]")) return false;
//                }
//                case CHOOSE_SHIP_PIECE -> {
//                    if (!input.matches("\\d")) return false;
//                }
//            }
//        }
//        return false;
