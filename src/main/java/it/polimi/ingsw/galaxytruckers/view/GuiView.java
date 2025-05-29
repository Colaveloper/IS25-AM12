package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
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
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
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

    @Override
    public void notifyMetaState(MetaState metaState) {

    }

    @Override
    public void notifyCurrentState(GameState gameState) {

    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {

    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {

    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {

    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation) {

    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation, Point oldPosition) {

    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {

    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {

    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {

    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {

    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {

    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {

    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {

    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {

    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {

    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {

    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {

    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {

    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {

    }

    @Override
    public void notifyGiveUp(ShipBoard shipBoard) {

    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {

    }
}
