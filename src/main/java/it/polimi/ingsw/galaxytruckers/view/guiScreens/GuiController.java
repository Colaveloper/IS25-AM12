package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import java.awt.*;

public abstract class GuiController {

    public void placeShipOnFlightboard(int position) {};

    public void requestRandComponent() {};

    public void rejectComponent() {};

    public void requestComponent(int id) {};

    public void flipHourglass() {};

    public void stashComponent() {};

    public void grabStashedComponent(int i) {};

    public void handlePointPress(Point currentPoint) {};

    public void rotateHandComponent() {};

    public void acquireForecast(int finalI) {}

    public void releaseForecast() {}

    public void drawCard() {}

    public void goNext() {}

    public void grabReward() {}

    public void chooseShipPiece(int i) {}

    public void removeCrew(Point point) {}

    public void removeGood(Point point) {}

    public void choosePlanet(int i) {}

//                    case ACTIVATE_COMPONENT ->      actions.add("P[x][y] Activate component        ");
//                    case SPEND_BATTERIES ->         actions.add("B[x][y] Spend battery on component");
//                    case GRAB_REWARD ->             actions.add("P       To pick reward            ");
//                    case CHOOSE_SHIP_PIECE ->       actions.add("K [i] Choose piece of ship to keep");
//                    case GO_NEXT, RELEASE_FORECAST->actions.add("press ENTER key to continue       ");
//                    case LOSE_CREW ->               actions.add("L[x][y] Remove crew from component");
//                    case LOSE_GOOD ->               actions.add("L[x][y] Remove good from cargo hold");
//                    case REMOVE_GOOD ->             actions.add("R[x][y][color] Pick goods from cargo hold");
//                    case ADD_GOOD ->                actions.add("P[x][y][color] Place goods on cargo hold");
//                    case CHOOSE_PLANET ->           actions.add("L[i]  Land on i-th planet         ");
//                    case REQUEST_RAND_COMPONENT ->  actions.add("C  Get New covered component      ");
//                    case REQUEST_COMPONENT ->       actions.add("U[i] Pick i-th uncovered component");
//                    case REJECT_COMPONENT ->        actions.add("R  Rejected component             ");
//                    case STASH_COMPONENT ->         actions.add("S  To stash current component     ");
//                    case GRAB_STASHED_COMPONENT ->  actions.add("S [i]  To grab i-th stashed       ");
//                    case PLACE_COMPONENT ->         actions.add("P[x][y]  Place component in [x][y]");
//                    case FLIP_HOURGLASS ->          actions.add("H  To Flip hourglass              ");
//                    case PLACE_SHIP_ON_FLIGHTBOARD->actions.add("E [i] End and place on flightboard");
//                    case FINISH_BUILDING ->         actions.add("X  To finish building             ");
//                    case ACQUIRE_FORECAST ->        actions.add("F [i]  Pick i-th forecast deck    ");
//                    case DRAW_CARD ->               actions.add("  Press ENTER to draw a card      ");
//                    case REMOVE_COMPONENT ->        actions.add("R[x][y]  Remove component in x, y ");
//                    case INITIALIZE_CABIN ->        actions.add("P[x][y]  Initialize cabin in x, y ");
//                    case GIVE_UP ->                 actions.add("Y  To give up and stop playing    ");
}
