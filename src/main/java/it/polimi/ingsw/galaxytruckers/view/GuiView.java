package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.guiScreens.GuiScreen;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GuiView extends View<GuiScreen> {

    private GuiScreen currentScreen;
    private StackPane contentPane;

    public GuiView(ClientController controller, ClientModel model) {
        super(model, controller, new GuiScreenFactory());
        this.currentScreen = screenFactory.createScreen(MetaState.REGISTER, model, controller);
    }

    // called by JFXApp thread, no need of runLater()
    public void setStage(Stage stage) {
        this.contentPane = new StackPane();
        this.contentPane.getChildren().setAll(currentScreen.getNode());

        StackPane rootPane = createRootWithBackground();
        rootPane.getChildren().add(contentPane);

        stage.setTitle("Galaxy Truckers");
        stage.setScene(new Scene(rootPane, 1280, 720));
        stage.show();
    }

    //region State-Notify methods
    @Override
    public void notifyMetaState(MetaState metaState) {
        Platform.runLater(() -> switchToScreen(screenFactory.createScreen(metaState, model, controller)));
    }

    @Override
    public void notifyCurrentState(GameState gameState) {
        Platform.runLater(() -> switchToScreen(screenFactory.createScreen(gameState, model, controller)));
    }
    //endregion

    //region Event-Notify methods
    @Override
    public void notifyNewLobby(Lobby lobby) {

    }

    @Override
    public void notifyRemoveLobby(UUID uuid) {

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
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation) {
        currentScreen.notifyPlaceComponent(shipBoard,newPoint,orientation);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition) {
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
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        currentScreen.notifyFlightBoardPosition(shipBoard, position);
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

    private void switchToScreen(GuiScreen newScreen) {
        this.currentScreen = newScreen;
        contentPane.getChildren().setAll(currentScreen.getNode());
    }

    private StackPane createRootWithBackground() {
        StackPane pane = new StackPane();

        try {
            URL bgUrl = getClass().getResource("/textures/background.png");
            if (bgUrl == null) {
                throw new FileNotFoundException("Resource not found: /textures/background.png");
            }
            Image bgImage = new Image(bgUrl.toExternalForm(), true);
            ImageView bgView = new ImageView(bgImage);
            bgView.setPreserveRatio(false);
            bgView.setSmooth(true);
            bgView.fitWidthProperty().bind(pane.widthProperty());
            bgView.fitHeightProperty().bind(pane.heightProperty());
            pane.getChildren().addAll(bgView);
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            pane.setStyle("-fx-background-color: black;");
        }

        return pane;
    }
}
