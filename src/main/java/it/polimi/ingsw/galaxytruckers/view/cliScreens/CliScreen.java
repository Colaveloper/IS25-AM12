package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.view.Screen;
import it.polimi.ingsw.galaxytruckers.view.cliElements.*;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Base class for all command-line interface screens in the game.
 * Provides common functionality for managing game state, player ships,
 * and user interface elements across different game phases.
 * Each game screen extends this class to implement its unique behavior.
 */
public abstract class CliScreen extends Screen {

    protected final ClientModel model;
    protected final ControllerToServer controller;
    protected final GameState state;
    protected final List<StateActions> availableActions;
    protected CliFlightBoard cliFlightBoard;
    protected ShipBoard myShipBoard;
    protected CliAllShips cliAllShips;
    protected Map<ShipBoard, CliShipBoard> shipToCliShip;
    protected CliComponentLegend cliComponentLegend;
    private String surrenderMessage;


    /**
     * Creates a new CLI screen with the given game state and controller.
     * Initializes the screen's UI elements, player ships, and available actions.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     * @param gameState The current game state containing available actions and player information
     */
    public CliScreen(ClientModel model, ControllerToServer controller, GameState gameState) {
        this.model = model;
        this.controller = controller;
        this.state = gameState;
        this.availableActions = new ArrayList<>();
        this.cliComponentLegend = new CliComponentLegend();
        if (gameState != null) {
            this.availableActions.addAll(gameState.getAvailableActions());
            this.myShipBoard = model.getMyShip();
            this.shipToCliShip = new HashMap<>();
            for (Player player : model.getPlayers()) {
                shipToCliShip.put(player.getShipBoard(), new CliShipBoard(player.getShipBoard(), player.getNickname()));
            }
            List<CliShipBoard> list = new ArrayList<>(shipToCliShip.values().stream().toList());
            list.sort(Comparator.comparing(CliShipBoard::getNickname));
            list.remove(shipToCliShip.get(myShipBoard));
            list.addFirst(shipToCliShip.get(myShipBoard));
            this.cliAllShips = new CliAllShips(list);
            this.cliFlightBoard = new CliFlightBoard(model.getGame().getFlightBoard());
        }
    }

    /**
     * Alternative constructor for screens that don't require a game state.
     *
     * @param model The client model containing the current game state
     * @param controller The controller for sending commands to the server
     */
    public CliScreen(ClientModel model, ControllerToServer controller) {
        this(model, controller, null);
    }

    /**
     * Gets the current game state associated with this screen.
     *
     * @return The current GameState object
     */
    public GameState getState() {
        return state;
    }

    /**
     * Renders the current screen content to the command line interface.
     * Each screen implementation provides its specific rendering logic.
     */
    public abstract void render();

    /**
     * Handles notification when a player gives up the game.
     * Updates the screen with a surrender message.
     *
     * @param player The player who has given up
     */
    @Override
    public void notifyGiveUp(Player player) {
        surrenderMessage = player.getNickname() + " has surrendered!";
    }

    /**
     * Processes and executes user input commands.
     * Each screen implementation handles its specific command set.
     *
     * @param input The command string entered by the user
     */
    public abstract void parseAndInvoke(String input);

    /**
     * Updates the screen when a ship component changes.
     * Marks affected UI elements as needing to be redrawn.
     *
     * @param shipBoard The ship board containing the changed component
     * @param point The position of the changed component
     */
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        cliAllShips.setDirty();
        shipToCliShip.get(shipBoard).setDirty();
        shipToCliShip.get(shipBoard).getCliComponent(point).setDirty();
    }

    /**
     * Generates a formatted display of the current ship's statistics and flight board.
     * Includes information about firepower, engine power, batteries, crew size,
     * credits, and any losses. Also shows the flight board and component legend.
     *
     * @return A list of strings containing the formatted ship and flight statistics
     */
    protected List<String> printShipFlightStats() {
        List<String> description = new ArrayList<>();

        description.addAll(cliFlightBoard.getDescription());

        description.addAll(cliComponentLegend.getDescription());

        description.add(
                "firepower: "     + myShipBoard.getFirePower()/2 +
                "\tengine power: "+ myShipBoard.getEnginePower() +
                "\tbatteries: "   + myShipBoard.getNumBatteries() +
                "\tcrewsize: "    + myShipBoard.getCrewSize() +
                "\tcredits: "     + myShipBoard.getCredits()
        );
        if(myShipBoard.getLosses()!=0) description.add("losses: " + myShipBoard.getLosses());

        description.addAll(cliAllShips.getDescription());
        return description;
    }

    /**
     * Displays all currently available actions to the player based on the game state.
     * Shows different command options depending on the current phase and available actions.
     * Includes surrender messages and handles players who have given up.
     */
    protected void printActions() {
        if(!model.getGame().getGivenUpShips().contains(model.getClientPlayer().getShipBoard())) {
            List<String> actions = new ArrayList<>();
            availableActions.clear(); // refreshing available actions
            availableActions.addAll(state.getAvailableActions());
            for (StateActions action : availableActions) {
                switch (action) {
                    case GRAB_PLACED_COMPONENT ->   actions.add("G Grab last placed Component");
                    case ACTIVATE_COMPONENT ->      actions.add("A[x][y] Activate component        ");
                    case SPEND_BATTERIES ->         actions.add("B[x][y] Spend battery on component");
                    case GRAB_REWARD ->             actions.add("P       To pick reward            ");
                    case CHOOSE_SHIP_PIECE ->       actions.add("K [i] Choose piece of ship to keep");
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
                    case ROTATE_COMPONENT ->        actions.add("R [LEFT|RIGHT] Rotate component   ");
                    case FLIP_HOURGLASS ->          actions.add("H  To Flip hourglass              ");
                    case PLACE_SHIP_ON_FLIGHTBOARD->actions.add("E [i] End and place on flightboard");
                    case PLACE_SHIP_FOR_TEST ->     actions.add("E to place ship on first available space");
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
            System.out.println("You're out of the flight! - watch other players compete.");
        }
        if(surrenderMessage!=null) {
            System.out.println(surrenderMessage);
            surrenderMessage = null;
        }
    }

    /**
     * Extracts x,y coordinates from an input command string.
     * Parses space-separated input where the second and third tokens are coordinates.
     *
     * @param input The command string containing coordinates (format: "command x y")
     * @return A Point object containing the parsed x,y coordinates
     * @throws NumberFormatException if the coordinates are not valid integers
     * @throws ArrayIndexOutOfBoundsException if the input doesn't contain enough parts
     */
    protected Point getPoint(String input) {
        String[] parts = input.split(" ");
        int x = Integer.parseInt(parts[1]);
        int y = Integer.parseInt(parts[2]);
        return new Point(x, y);
    }

    /**
     * Checks if the user input command is legal based on the return value of {@link #isFormatLegal(String)},
     * as well as the screen's own information (in @Override methods).
     *
     * @param input The command string entered by the user
     * @return true if the input is legal, false otherwise
     */
    public boolean isInputLegal(String input) {
        return isFormatLegal(input);
    }

    /**
     * Validates the format of a user input command based on the available actions.
     * Checks if the input matches the expected patterns for each action type.
     *
     * @param input The command string entered by the user
     * @return true if the input format is legal for the current game state, false otherwise
     */
    protected final boolean isFormatLegal(String input) {
        input = input.toUpperCase().trim();
        if(input.isEmpty()) input = " "; //to check last case of empty input
        //String[] parts = input.split(" ");
        return switch (input.substring(0, 1)) {
            case "P" ->(availableActions.contains(StateActions.PLACE_COMPONENT) ||
                        availableActions.contains(StateActions.INITIALIZE_CABIN))&& input.matches("P\\s\\d+\\s\\d+") ||
                        availableActions.contains(StateActions.GRAB_REWARD)     && input.matches("P") ||
                        availableActions.contains(StateActions.ADD_GOOD)        && input.matches("P\\s\\d+\\s\\d+\\s[A-Z]+");
            case "H" -> availableActions.contains(StateActions.FLIP_HOURGLASS)  && input.matches("H");
            case "S" -> availableActions.contains(StateActions.STASH_COMPONENT) && input.matches("S") ||
                        availableActions.contains(StateActions.GRAB_STASHED_COMPONENT) && input.matches("S\\s\\d+");
            case "R" -> availableActions.contains(StateActions.REJECT_COMPONENT)&& input.matches("R") ||
                        availableActions.contains(StateActions.REMOVE_GOOD)     && input.matches("R\\s\\d+\\s\\d+\\s[A-Z]+") ||
                        availableActions.contains(StateActions.REMOVE_COMPONENT)&& input.matches("R\\s\\d+\\s\\d+") ||
                        availableActions.contains(StateActions.ROTATE_COMPONENT)&& input.matches("R\\s(LEFT|RIGHT)");
            case "C" -> availableActions.contains(StateActions.REQUEST_RAND_COMPONENT) && input.matches("C");
            case "U" -> availableActions.contains(StateActions.REQUEST_COMPONENT)&& input.matches("U\\s\\d+");
            case "F" -> availableActions.contains(StateActions.ACQUIRE_FORECAST)&& input.matches("F\\s\\d+");
            case "X" -> availableActions.contains(StateActions.FINISH_BUILDING) && input.matches("X");
            case "L" ->(availableActions.contains(StateActions.LOSE_CREW)   ||
                        availableActions.contains(StateActions.LOSE_GOOD))      && input.matches("L\\s\\d+\\s\\d+") ||
                        availableActions.contains(StateActions.CHOOSE_PLANET)   && input.matches("L\\s\\d+");
            case "K" -> availableActions.contains(StateActions.CHOOSE_SHIP_PIECE)&& input.matches("K\\s\\d+");
            case "Y" -> availableActions.contains(StateActions.GIVE_UP)         && input.matches("Y");
            case "B" -> availableActions.contains(StateActions.SPEND_BATTERIES) && input.matches("B\\s\\d+\\s\\d+");
            case "A" -> availableActions.contains(StateActions.ACTIVATE_COMPONENT)&& input.matches("A\\s\\d+\\s\\d+");
            case "E" -> availableActions.contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD) && input.matches("E\\s\\d+") ||
                        availableActions.contains(StateActions.PLACE_SHIP_FOR_TEST) && input.matches("E");
            case "G" -> availableActions.contains(StateActions.GRAB_PLACED_COMPONENT) && input.matches("G");
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
