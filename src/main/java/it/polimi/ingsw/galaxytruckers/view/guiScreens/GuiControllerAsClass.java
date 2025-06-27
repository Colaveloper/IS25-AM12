//package it.polimi.ingsw.galaxytruckers.view.guiScreens;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
//import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
//
//import java.awt.*;
//import java.util.Set;
//
//public class GuiController {
//    private final ControllerToServer controller;
//    private final Set<StateActions> availableActions;
//
//    public GuiController(ControllerToServer controller, Set<StateActions> availableActions) {
//        this.controller = controller;
//        this.availableActions = availableActions;
//    }
//
//    public void placeShipOnFlightBoard(int position) {
//        if (availableActions.contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD)) {
//            controller.placeShipOnFlightBoard(position);
//        }
//    };
//
//    public void requestRandComponent() {
//        if (availableActions.contains(StateActions.REQUEST_RAND_COMPONENT)) {
//            controller.requestRandComponent();
//        }
//    };
//
//    public void rejectComponent() {
//        if (availableActions.contains(StateActions.REJECT_COMPONENT)) {
//            controller.rejectComponent();
//        }
//    };
//
//    public void requestComponent(int id) {
//        if (availableActions.contains(StateActions.REQUEST_COMPONENT)) {
//            controller.requestComponent(id);
//        }
//    };
//
//    public void flipHourglass() {
//        if (availableActions.contains(StateActions.FLIP_HOURGLASS)) {
//            controller.flipHourglass();
//        }
//    };
//
//    public void stashComponent() {
//        if (availableActions.contains(StateActions.STASH_COMPONENT)) {
//            controller.stashComponent();
//        }
//    };
//
//    public void grabStashedComponent(int i) {
//        if (availableActions.contains(StateActions.GRAB_STASHED_COMPONENT)) {
//            controller.grabStashedComponent(i);
//        }
//    };
//
//    public void handlePointPress(Point currentPoint) {
//
//    };
//
//    public void acquireForecast(int finalI) {
//        if (availableActions.contains(StateActions.ACQUIRE_FORECAST)) {
//            controller.acquireForecast(finalI);
//        }
//    }
//
//    public void releaseForecast() {
//        if (availableActions.contains(StateActions.RELEASE_FORECAST)) {
//            controller.releaseForecast();
//        }
//    }
//
//    public void giveUp() {
//        if (availableActions.contains(StateActions.GIVE_UP)) {
//            controller.giveUp();
//        }
//    }
//
//    public void grabReward() {
//        if (availableActions.contains(StateActions.GRAB_REWARD)) {
//            controller.grabReward();
//        }
//    }
//
//    public void goNext() {
//        if (availableActions.contains(StateActions.GO_NEXT)) {
//            controller.goNext();
//        }
//    }
//
//    public void activateComponent(Point point) {
//        if (availableActions.contains(StateActions.ACTIVATE_COMPONENT)) {
//            controller.activateComponent(point);
//        }
//    }
//
//    public void spendBatteries(Point point) {
//        if (availableActions.contains(StateActions.SPEND_BATTERIES)) {
//            controller.useBattery(point);
//        }
//    }
//
//    public void chooseShipPiece(int i) {
//        if (availableActions.contains(StateActions.CHOOSE_SHIP_PIECE)) {
//            controller.chooseShipPiece(i);
//        }
//    }
//
//    public void loseCrew(Point point) {
//        if (availableActions.contains(StateActions.LOSE_CREW)) {
//            controller.loseCrew(point);
//        }
//    }
//
//    public void loseGood(Point point) {
//        if (availableActions.contains(StateActions.LOSE_GOOD)) {
//            controller.loseGoods(point);
//        }
//    }
//
//    public void removeGood(Point point, GoodsType color) {
//        if (availableActions.contains(StateActions.REMOVE_GOOD)) {
//            controller.removeGoods(point, color);
//        }
//    }
//
//    public void addGood(Point point, GoodsType color) {
//        if (availableActions.contains(StateActions.ADD_GOOD)) {
//            controller.placeGoods(point, color);
//        }
//    }
//
//    public void choosePlanet(int i) {
//        if (availableActions.contains(StateActions.CHOOSE_PLANET)) {
//            controller.choosePlanet(i);
//        }
//    }
//
//    public void finishBuilding() {
//        if (availableActions.contains(StateActions.FINISH_BUILDING)) {
//            controller.
//        }
//    }
//
//                    case PLACE_COMPONENT ->         actions.add("P[x][y]  Place component in [x][y]");
//                    case FINISH_BUILDING ->         actions.add("X  To finish building             ");
//                    case DRAW_CARD ->               actions.add("  Press ENTER to draw a card      ");
//                    case REMOVE_COMPONENT ->        actions.add("R[x][y]  Remove component in x, y ");
//                    case INITIALIZE_CABIN ->        actions.add("P[x][y]  Initialize cabin in x, y ");
//}
