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
    }

    public CliScreen(ClientModel model, ControllerToServer controller) {
        this.model = model;
        this.controller = controller;
    }

    public GameState getState() {
        return state;
    }

    public abstract void render();

    public abstract void parseAndInvoke(String input);

    protected void printShips() {
//        flightBoard.getNewDescription().forEach(System.out::println);
//        allShips.getNewDescription().forEach(System.out::println);//todo sistemare altri tipi di allships
    }

    protected void printActions() {
        if(!model.getGame().getGivenUpShips().contains(model.getClientPlayer().getShipBoard())) {
            List<String> actions = new ArrayList<>();
            availableActions = state.getAvailableActions(); // refresh available actions
            for (StateActions action : availableActions) {
                switch (action) {
                    case ACTIVATE_COMPONENT ->      actions.add("P[x][y] Activate component        ");
                    case SPEND_BATTERIES ->         actions.add("B[x][y] Spend battery on component");
                    case GRAB_REWARD ->             actions.add("P       To pick reward            ");
                    case CHOOSE_SHIP_PIECE ->       actions.add("[i]   Choose piece of ship to keep");
                    case GO_NEXT, RELEASE_FORECAST->actions.add("C to continue                     ");
                    case LOSE_CREW ->               actions.add("L[x][y] Remove crew from component");
                    case LOSE_GOOD ->               actions.add("L[x][y] Remove good from cargo hold");
                    case REMOVE_GOOD ->             actions.add("R[x][y][color] Pick goods from cargo hold");
                    case ADD_GOOD ->                actions.add("P[x][y][color] Place goods on cargo hold");
                    case CHOOSE_PLANET ->           actions.add("L[i]  Land on i-th planet         ");
                    case REQUEST_RAND_COMPONENT ->  actions.add("C  Get New covered component      ");
                    case REQUEST_COMPONENT ->       actions.add("U[i] Pick i-th uncovered component");
                    case REJECT_COMPONENT ->        actions.add("R  Rejected component             ");
                    case STASH_COMPONENT ->         actions.add("S  To stash current component     ");
                    case GRAB_STASHED_COMPONENT ->  actions.add("S [i]  To grab i-th stashed       ");
                    case PLACE_COMPONENT ->         actions.add("P[x][y]  Place component in [x][y]");
                    case FLIP_HOURGLASS ->          actions.add("H  To Flip hourglass              ");
                    case PLACE_SHIP_ON_FLIGHTBOARD->actions.add("E [i] End and place on flightboard");
                    case FINISH_BUILDING ->         actions.add("X  To finish building             ");
                    case ACQUIRE_FORECAST ->        actions.add("F [i]  Pick i-th forecast deck    ");
                    case DRAW_CARD ->               actions.add("  Press any key to draw a card    ");
                    case REMOVE_COMPONENT ->        actions.add("P[x][y]  Remove component in x, y ");
                    case INITIALIZE_CABIN ->        actions.add("P[x][y]  Initialize cabin in x, y ");
                    case GIVE_UP ->                 actions.add("Y  To give up and stop playing    ");
                }
            }
            for (int i = 0; i < availableActions.size(); i++) {
                System.out.print(actions.get(i) + "\t\t");
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

    protected boolean isFormatLegal(String input) {
        String[] parts = input.split(" ");
        return switch (parts[0]) {
            case "P" -> availableActions.contains(StateActions.PLACE_COMPONENT) && input.trim().matches("(?i)P\\s+\\d+\\s+\\d+") ||
                        availableActions.contains(StateActions.GRAB_REWARD)     && input.trim().matches("(?i)P");
            case "H" -> availableActions.contains(StateActions.FLIP_HOURGLASS)  && input.trim().matches("(?i)H");
            case "S" -> availableActions.contains(StateActions.STASH_COMPONENT) && input.trim().matches("(?i)S") ||
                        availableActions.contains(StateActions.GRAB_STASHED_COMPONENT) && input.trim().matches("(?i)S\\s+\\d+");
            case "R" -> availableActions.contains(StateActions.REJECT_COMPONENT)&& input.trim().matches("(?i)R");
            case "C" -> availableActions.contains(StateActions.REQUEST_RAND_COMPONENT)&& input.trim().matches("(?i)C") ||
                        availableActions.contains(StateActions.GO_NEXT)         && input.trim().matches("(?i)C") ||
                        availableActions.contains(StateActions.RELEASE_FORECAST)&& input.trim().matches("(?i)C");
            case "U" -> availableActions.contains(StateActions.REQUEST_COMPONENT)&& input.trim().matches("(?i)U\\s+\\d+");
            case "F" -> availableActions.contains(StateActions.ACQUIRE_FORECAST)&& input.trim().matches("(?i)F\\s+\\d+");
            case "X" -> availableActions.contains(StateActions.FINISH_BUILDING) && input.trim().matches("(?i)X");
            case "L" -> availableActions.contains(StateActions.LOSE_CREW)       && input.trim().matches("(?i)L\\s+\\d+\\s+\\d+") ||
                        availableActions.contains(StateActions.LOSE_GOOD)       && input.trim().matches("(?i)L\\s+\\d+\\s+\\d+") ||
                        availableActions.contains(StateActions.CHOOSE_PLANET)   && input.trim().matches("(?i)L");
            case "B" -> availableActions.contains(StateActions.SPEND_BATTERIES) && input.trim().matches("(?i)B\\s+\\d+\\s+\\d+");
            case "K" -> availableActions.contains(StateActions.CHOOSE_SHIP_PIECE)&& input.trim().matches("(?i)K");
            case "Y" -> availableActions.contains(StateActions.GIVE_UP)         && input.trim().matches("(?i)Y");
            case "A" -> availableActions.contains(StateActions.ACTIVATE_COMPONENT)&& input.trim().matches("(?i)A\\s+\\d+\\s+\\d+");
            case "E" -> availableActions.contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD) && input.trim().matches("(?i)E\\s+\\d+");
            case "" -> (availableActions.contains(StateActions.GO_NEXT) || availableActions.contains(StateActions.DRAW_CARD)) && input.isEmpty();

            default -> {
                System.out.println("invalid input");
                yield false;
            }
        };
    }
}
