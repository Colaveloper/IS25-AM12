package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAllShips;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CliScreen extends Screen {

    protected final ClientModel model;
    protected final ControllerToServer controller;
    protected final GameState state;
    protected final List<StateActions> availableActions;
    protected CliFlightBoard cliFlightBoard;
    protected ShipBoard myShipBoard;
    protected CliAllShips cliAllShips;
    protected Map<ShipBoard, CliShipBoard> shipToCliShip;


    public CliScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
        this.state = gameState;
        this.availableActions = new ArrayList<>();
        if (gameState != null) {
            this.availableActions.addAll(gameState.getAvailableActions());
            this.myShipBoard = model.getMyShip();
            this.shipToCliShip = new HashMap<>();
            for (Player player : model.getPlayers()) {
                shipToCliShip.put(player.getShipBoard(), new CliShipBoard(player.getShipBoard(), player.getNickname()));
            }
            this.cliAllShips = new CliAllShips(shipToCliShip.values().stream().toList());
            this.cliFlightBoard = new CliFlightBoard(model.getGame().getFlightBoard());
        }
    }

    public CliScreen(ClientModel model, ControllerToServer controller) {
        this(model, controller, null);
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
            availableActions.clear(); // refreshing available actions
            availableActions.addAll(state.getAvailableActions());
            for (StateActions action : availableActions) {
                switch (action) {
                    case ACTIVATE_COMPONENT ->      actions.add("P[x][y] Activate component        ");
                    case SPEND_BATTERIES ->         actions.add("B[x][y] Spend battery on component");
                    case GRAB_REWARD ->             actions.add("P       To pick reward            ");
                    case CHOOSE_SHIP_PIECE ->       actions.add("[i]   Choose piece of ship to keep");
                    case GO_NEXT, RELEASE_FORECAST->actions.add("press ENTER key to continue       ");
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
                    case DRAW_CARD ->               actions.add("  Press ENTER to draw a card      ");
                    case REMOVE_COMPONENT ->        actions.add("R[x][y]  Remove component in x, y ");
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
        input = input.toUpperCase().trim();
        if(input.isEmpty()) input = " "; //to check last case of empty input
        //String[] parts = input.split(" ");
        return switch (input.substring(0, 1)) {
            case "P" ->(availableActions.contains(StateActions.PLACE_COMPONENT) ||
                        availableActions.contains(StateActions.INITIALIZE_CABIN))&& input.matches("P\\s+\\d+\\s+\\d+") ||
                        availableActions.contains(StateActions.GRAB_REWARD)     && input.matches("P") ||
                        availableActions.contains(StateActions.ADD_GOOD)        && input.matches("P/\\s+\\d+\\s+\\d+\\s+[A-Z]+");
            case "H" -> availableActions.contains(StateActions.FLIP_HOURGLASS)  && input.matches("H");
            case "S" -> availableActions.contains(StateActions.STASH_COMPONENT) && input.matches("S") ||
                        availableActions.contains(StateActions.GRAB_STASHED_COMPONENT) && input.matches("S\\s+\\d+");
            case "R" -> availableActions.contains(StateActions.REJECT_COMPONENT)&& input.matches("R") ||
                        availableActions.contains(StateActions.REMOVE_GOOD)     && input.matches("R\\s+\\d+\\s+\\d+\\s+[A-Z]+") ||
                        availableActions.contains(StateActions.REMOVE_COMPONENT)&& input.matches("R\\s+\\d+\\s+\\d+");
            case "C" -> availableActions.contains(StateActions.REQUEST_RAND_COMPONENT) && input.matches("C");
            case "U" -> availableActions.contains(StateActions.REQUEST_COMPONENT)&& input.matches("U\\s+\\d+");
            case "F" -> availableActions.contains(StateActions.ACQUIRE_FORECAST)&& input.matches("F\\s+\\d+");
            case "X" -> availableActions.contains(StateActions.FINISH_BUILDING) && input.matches("X");
            case "L" ->(availableActions.contains(StateActions.LOSE_CREW)   ||
                        availableActions.contains(StateActions.LOSE_GOOD))      && input.matches("L\\s+\\d+\\s+\\d+") ||
                        availableActions.contains(StateActions.CHOOSE_PLANET)   && input.matches("L\\s+\\d+");
            case "B" -> availableActions.contains(StateActions.SPEND_BATTERIES) && input.matches("B\\s+\\d+\\s+\\d+");
            case "K" -> availableActions.contains(StateActions.CHOOSE_SHIP_PIECE)&& input.matches("K");
            case "Y" -> availableActions.contains(StateActions.GIVE_UP)         && input.matches("Y");
            case "A" -> availableActions.contains(StateActions.ACTIVATE_COMPONENT)&& input.matches("A\\s+\\d+\\s+\\d+");
            case "E" -> availableActions.contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD) && input.matches("E\\s*\\d+");
            case " " ->(availableActions.contains(StateActions.GO_NEXT)    ||
                        availableActions.contains(StateActions.DRAW_CARD)  ||
                        availableActions.contains(StateActions.RELEASE_FORECAST));

            default -> {
                //System.out.println("invalid input");
                yield false;
            }
        };
    }

}
