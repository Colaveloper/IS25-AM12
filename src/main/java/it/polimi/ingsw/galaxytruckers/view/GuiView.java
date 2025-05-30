package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuiView extends Application implements View {
    GuiScreen screen;
    ClientModel model;
    ClientController controller;
    ScreenFactory screenFactory;

    public GuiView(ClientController controller, ClientModel model) {
        this.controller = controller;
        this.model = model;
        model.addObserver(this);
        this.screenFactory = new ScreenFactory();
    }

    public void updateScreen() {
        screen = screenFactory.createGuiScreen(model, controller);
//        this.refreshScreen();
    }

//    private void refreshScreen(VBox root) {
//        Platform.runLater(() -> {
//            root.getChildren().clear();
//            try {
//                screen.attachContentToRoot(root);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    //region Update methods
    @Override
    public void notifyMetaState(MetaState metaState) {
        screen = screenFactory.createGuiScreen(model, controller);
    }

    @Override
    public void notifyCurrentState(GameState gameState) {
        screen = screenFactory.createGuiScreen(model, controller);
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        screen.notifyRequestRandComponent(shipBoard, component);
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        screen.notifyRequestComponent(shipBoard, component);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        screen.notifyRejectComponent(shipBoard, component);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        screen.notifyRejectComponent(shipBoard,component,oldPosition);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        screen.notifyStashComponent(shipBoard, component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        screen.notifyStashComponent(shipBoard,component,oldPosition);
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        screen.notifyGrabStashedComponent(shipBoard,index,component);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation) {
        screen.notifyPlaceComponent(shipBoard,newPoint,orientation);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation, Point oldPosition) {
        screen.notifyPlaceComponent(shipBoard,newPoint,orientation,oldPosition);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        screen.notifyFlipHourglass(shipBoard);
    }

    @Override
    public void notifyHourglassEnd() {
        screen.notifyHourglassEnd();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        screen.notifyFlightBoardPosition(shipBoard,position);
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        screen.notifyPeekForecast(shipBoard,deckIndex);
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        screen.setForecastDeck(adventureCards);
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {
        screen.notifyReleaseForecast(shipBoard, index);
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        screen.notifyRemoveComponent(shipBoard,point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        screen.notifyChooseShipPiece(shipBoard,pieceIndex,removed);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        screen.notifyShipNotConnected(shipBoard,shipPieces);
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        screen.notifyShipValidated(shipBoard);
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {
        screen.notifyInitializeCabin(shipBoard,point,crewType,numResidents);
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        screen.notifyDrawCard(adventureCard);
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        screen.notifyActivateComponent(shipBoard,point);
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        screen.notifyLoseCrew(shipBoard,point);
    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        screen.notifyGrabReward(shipBoard,rewardGrabbed);
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        screen.notifyPlaceGoods(shipBoard,point,goodsType);
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        screen.notifyRemoveGoods(shipBoard,point,goodsType);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        screen.notifyUseBattery(shipBoard,point);
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        screen.notifyChoosePlanet(shipBoard,choice);
    }

    @Override
    public void notifyGiveUp(ShipBoard shipBoard) {
        screen.notifyGiveUp(shipBoard);
    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {
        screen.setFinalScores(finalScores);
    }
    //endregion
}
