package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiNicknameChoiceScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GuiView extends Application implements View {
    private static ClientModel model;
    private static ClientController controller;
    private final ScreenFactory screenFactory = new ScreenFactory();

    private GuiScreen currentScreen;
    private Stage primaryStage;

    public static void setModel(ClientModel model) {
        GuiView.model = model;
    }

    public static void setController(ClientController controller) {
        GuiView.controller = controller;
    }

    @Override
    public void start(Stage stage) {
        model.addObserver(this);

        this.primaryStage = stage;
        currentScreen = new GuiNicknameChoiceScreen(model, controller);
        stage.setScene(new Scene(currentScreen.getNode()));
        stage.setTitle("Screen Switcher");
        stage.show();
    }

    //region Update methods
    @Override
    public void notifyMetaState(MetaState metaState) {
        currentScreen = screenFactory.createGuiScreen(model, controller);
        Platform.runLater(()->{
            primaryStage.setScene(new Scene(currentScreen.getNode()));
        });
    }

    @Override
    public void notifyCurrentState(GameState gameState) {
        currentScreen = screenFactory.createGuiScreen(model, controller);
        Platform.runLater(()->{
            primaryStage.setScene(new Scene(currentScreen.getNode()));
        });
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRequestRandComponent(shipBoard, component);
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRequestComponent(shipBoard, component);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRejectComponent(shipBoard, component);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        currentScreen.notifyRejectComponent(shipBoard,component,oldPosition);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyStashComponent(shipBoard, component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        currentScreen.notifyStashComponent(shipBoard,component,oldPosition);
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        currentScreen.notifyGrabStashedComponent(shipBoard,index,component);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation) {
        currentScreen.notifyPlaceComponent(shipBoard,newPoint,orientation);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation, Point oldPosition) {
        currentScreen.notifyPlaceComponent(shipBoard,newPoint,orientation,oldPosition);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        currentScreen.notifyFlipHourglass(shipBoard);
    }

    @Override
    public void notifyHourglassEnd() {
        currentScreen.notifyHourglassEnd();
    }

    @Override
    public void notifyFlightBoardPosition(FlightBoard flightBoard) {
        currentScreen.notifyFlightBoardPosition(flightBoard);
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        currentScreen.notifyPeekForecast(shipBoard,deckIndex);
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        currentScreen.setForecastDeck(adventureCards);
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {
        currentScreen.notifyReleaseForecast(shipBoard, index);
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        currentScreen.notifyRemoveComponent(shipBoard,point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        currentScreen.notifyChooseShipPiece(shipBoard,pieceIndex,removed);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        currentScreen.notifyShipNotConnected(shipBoard,shipPieces);
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        currentScreen.notifyShipValidated(shipBoard);
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {
        currentScreen.notifyInitializeCabin(shipBoard,point,crewType,numResidents);
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        currentScreen.notifyDrawCard(adventureCard);
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        currentScreen.notifyActivateComponent(shipBoard,point);
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        currentScreen.notifyLoseCrew(shipBoard,point);
    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        currentScreen.notifyGrabReward(shipBoard,rewardGrabbed);
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyPlaceGoods(shipBoard,point,goodsType);
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyRemoveGoods(shipBoard,point,goodsType);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        currentScreen.notifyUseBattery(shipBoard,point);
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        currentScreen.notifyChoosePlanet(shipBoard,choice);
    }

    @Override
    public void notifyGiveUp(ShipBoard shipBoard) {
        currentScreen.notifyGiveUp(shipBoard);
    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {
        currentScreen.setFinalScores(finalScores);
    }
    //endregion
}
