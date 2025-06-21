package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiGameScreen extends GuiScreen {
    protected final Map<ShipBoard, GuiShipBoard> guiShipBoards;
    protected final GuiLog guiLog;
    protected final GuiFlightBoard guiFlightBoard;
    protected final PurpleHBox guiButtonBox;

    public GuiGameScreen(ClientModel model, ControllerToServer controller, GameState state) {
        super(model, controller, state);

        guiShipBoards = new HashMap<>();
        for (Player player : model.getPlayers()) {
            GuiController guiController = player.getShipBoard().equals(model.getMyShip())
                    ? getGuiController()
                    : new GuiController() {};
            guiShipBoards.put(player.getShipBoard(), new GuiShipBoard(player.getNickname(), player.getShipBoard(), guiController));
        }

        guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), getGuiController());
        guiLog = new GuiLog();
        guiButtonBox = new PurpleHBox(10);
        guiButtonBox.setAlignment(Pos.CENTER);
    }

    public Pane getNode() {
        VBox layout = new VBox(10);

        PurpleVBox guiFlightBoard = this.guiFlightBoard;

        HBox bottomBox = getBottomBox();
        VBox.setVgrow(bottomBox, Priority.ALWAYS);
        bottomBox.setPrefHeight(0); // parent sets the real height

        layout.getChildren().addAll(guiFlightBoard, bottomBox);
        return layout;
    }

    private HBox getBottomBox() {
        HBox bottomBox = new HBox(10);

        PurpleVBox othersShipsVBox = getOtherShipsVBox();
        ScrollPane scrollPane = new ScrollPane(othersShipsVBox);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox centralVBox = getCentralVBox();
        HBox.setHgrow(centralVBox, Priority.ALWAYS);
        centralVBox.setPrefWidth(0);

        VBox sidePanel = getSideVBox();
        HBox.setHgrow(sidePanel, Priority.ALWAYS);
        centralVBox.setPrefWidth(0);

        bottomBox.getChildren().addAll(scrollPane, centralVBox, sidePanel);
        return bottomBox;
    }

    private PurpleVBox getOtherShipsVBox() {
        PurpleVBox otherShipsVBox = new PurpleVBox(10);

        for (ShipBoard shipBoard : guiShipBoards.keySet()) {
            if (!shipBoard.equals(myShipBoard)) {
                otherShipsVBox.getChildren().add(getShipBoardVBox(shipBoard));
                VBox.setVgrow(otherShipsVBox, Priority.ALWAYS);
                otherShipsVBox.setPrefHeight(0); // parent sets the real height
            }
        }

        return otherShipsVBox;
    }

    private VBox getCentralVBox() {
        VBox centralVBox = new PurpleVBox(10);

        VBox mainShipVBox = getShipBoardVBox(myShipBoard);

        HBox guiButtonBox = this.guiButtonBox;
        VBox.setVgrow(guiButtonBox, Priority.ALWAYS);
        guiButtonBox.setPrefHeight(0); // parent sets the real height

        centralVBox.getChildren().addAll(mainShipVBox, guiButtonBox);
        return centralVBox;
    }

    private VBox getSideVBox() {
        VBox sideVBox = new PurpleVBox(10);
        sideVBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox freeUseVBox = getFreeUseVBox();

        GuiLog guiLog = this.guiLog;
        VBox.setVgrow(guiLog, Priority.ALWAYS);
        guiLog.setMaxHeight(Double.MAX_VALUE);
        sideVBox.getChildren().addAll(freeUseVBox, guiLog);
        return sideVBox;
    }

    /**
     * @return a VBox with any of the following: forecast, hourglass, rejected components, current card
     */
    protected abstract VBox getFreeUseVBox();

    /**
     *
     * @param shipBoard the ship board to be displayed
     * @return a VBox with the GuiShipBoard and any of the following: GuiHand, GuiStash
     */
    protected abstract VBox getShipBoardVBox(ShipBoard shipBoard);

    protected abstract GuiController getGuiController();

    @Override
    public void notifyComponentChange(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyComponentChange(point);
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        super.notifyRemoveComponent(shipBoard, point);
    }
}