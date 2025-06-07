package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiComponentBank;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiForecast;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiHourglass;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;

public class GuiSecondShipBuildingScreen extends GuiGameScreen {

    private final GuiComponentBank guiComponentBank;
    private final GuiForecast guiForecast;
    private final GuiHourglass guiHourglass;

    private Direction lastComponentDirection = Direction.UP;

    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState state) {
        super(model, controller, state);
        guiComponentBank = new GuiComponentBank(state.getComponentBank(), controller);
        guiForecast = new GuiForecast(state.getBlockedForecasts(), controller);
        guiHourglass = new GuiHourglass(controller);
    }

    @Override
    public Parent getNode() {
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(guiComponentBank, guiForecast, guiHourglass, guiFlightBoard, guiAllShips);
        return layout;
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRequestRandComponent();
        guiAllShips.notifySetHand(shipBoard, component);
    }


    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRequestComponent(component);
        guiAllShips.notifySetHand(shipBoard, component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        guiAllShips.notifyStashComponent(shipBoard, component);
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiAllShips.notifyStashComponent(shipBoard, component, oldPosition);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRejectComponent(component);
        guiAllShips.notifyClearHand(shipBoard);
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiComponentBank.notifyRejectComponent(component);
        guiAllShips.notifyRemoveComponent(shipBoard, oldPosition);
    }
    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        guiAllShips.notifyGrabStashedComponent(shipBoard, index, component);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        int placedComponentId = shipBoard.getComponentMap().get(point).getId();
        guiAllShips.notifyPlaceComponent(shipBoard, placedComponentId, point, orientation);
    }
    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard)
    {
//        {// Update the hourglass status in the UI
        // cliFlightBoard.setDirty();
    }

    @Override
    public void notifyHourglassEnd()
    {
//        {// Mark the flight board as dirty to update the hourglass status
        // cliFlightBoard.setDirty();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        guiFlightBoard.notifyFlightBoardPosition(shipBoard, position);
    }
    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        guiForecast.notifyPeekForecast(shipBoard, deckIndex);
    }
    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards)
//    {
//        cliForecastCards.setCards(adventureCards);
//        hasForecastDeck = true;
//        cliForecastCards.setDirty();
//    }
    {}
    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        guiForecast.notifyReleaseForecast(shipBoard, deckIndex);
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiAllShips.notifyRemoveComponent(shipBoard, point);
    }

    @Override
    public void handlePointPress(Point point) {
        if (model.getMyShip().getComponentMap().containsKey(point)) {
            lastComponentDirection = lastComponentDirection.getLeft();
        } else if (model.getMyShip().getShipArea().contains(point)) {
            controller.placeComponent(point, lastComponentDirection);
        }
    }
}