package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

/**
 * JavaFX GUI implementation of the View for the Galaxy Truckers game.
 * This class manages the graphical user interface, handling screen transitions
 * and user interaction through a JavaFX application.
 */
public class GuiView extends View<GuiScreen> {

    /** The current screen being displayed to the user */
    private GuiScreen currentScreen;

    /** The main content pane where screen nodes are displayed */
    private StackPane contentPane;

    /**
     * Constructs a new GUI view with the specified controller and model.
     * Initializes the view with the REGISTER meta state.
     *
     * @param controller The client controller for handling user commands
     * @param model The client model containing game data
     */
    public GuiView(ClientController controller, ClientModel model) {
        super(model, controller, new GuiScreenFactory());
        this.currentScreen = screenFactory.createScreen(MetaState.REGISTER, model, controller);
    }

    /**
     * Sets up the main stage for the JavaFX application.
     * This method creates the scene hierarchy, adds the background,
     * configures the main content pane, and displays the initial screen.
     *
     * @param stage The JavaFX stage to set up
     */
    public void setStage(Stage stage) {
        this.contentPane = new StackPane();
        contentPane.setPadding(new Insets(10));
        contentPane.getChildren().setAll(currentScreen.getNode());
        contentPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        StackPane.setAlignment(contentPane, Pos.CENTER);

        StackPane rootPane = createRootWithBackground();
        rootPane.getChildren().add(contentPane);

        rootPane.setPrefSize(1280, 720);
        rootPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Scene scene = new Scene(rootPane);
        stage.setTitle("Galaxy Truckers");
        stage.setScene(scene);
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
        currentScreen.notifyNewLobby(lobby);
    }

    @Override
    public void notifyRemoveLobby(UUID uuid) {
        currentScreen.notifyRemoveLobby(uuid);
    }

    @Override
    public void notifyPlayerJoin(Player player) {
        currentScreen.notifyPlayerJoin(player);
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
    public void notifyGrabPlacedComponent(ShipBoard shipBoard, Point prevPosition) {
        currentScreen.notifyGrabPlacedComponent(shipBoard, prevPosition);
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
        currentScreen.notifyRemoveComponent(shipBoard, point);
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        currentScreen.notifyChooseShipPiece(shipBoard, pieceIndex, removed);
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        currentScreen.notifyShipNotConnected(shipBoard, shipPieces);
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        currentScreen.notifyShipValidated(shipBoard);
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {
        currentScreen.notifyComponentChange(shipBoard, point);
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
        currentScreen.notifyComponentChange(shipBoard,point);
    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        currentScreen.notifyGrabReward(shipBoard, rewardGrabbed);
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyComponentChange(shipBoard, point);
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyComponentChange(shipBoard, point);
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        currentScreen.notifyUseBattery(shipBoard, point);
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        currentScreen.notifyChoosePlanet(shipBoard, choice, nextShipBoard);
    }

    @Override
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        currentScreen.notifyCurrentPlayerUpdate(shipBoard);
    }

    @Override
    public void notifyGiveUp(Player player) {
        currentScreen.notifyGiveUp(player);
    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {
        currentScreen.setFinalScores(finalScores);
    }
    //endregion


    @Override
    public void reportError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Switches the current screen to a new screen.
     * Updates the currentScreen reference and replaces the node
     * in the content pane with the new screen's node.
     *
     * @param newScreen The new screen to display
     */
    private void switchToScreen(GuiScreen newScreen) {
        this.currentScreen = newScreen;
        contentPane.getChildren().setAll(currentScreen.getNode());
    }

    /**
     * Creates a root pane with a background image.
     * The background image is loaded from resources and sized to fill the pane.
     * If the image loading fails, a black background is used as a fallback.
     *
     * @return A StackPane with the background image set
     */
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
