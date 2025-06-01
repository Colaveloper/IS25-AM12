package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipHandAndStash;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAllShips;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiComponentBank;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiFlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.List;

public class GuiSecondShipBuildingScreen extends GuiGameScreen {

    private final GuiComponentBank guiComponentBank;
//    GuiForecast guiForecast;

    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState state) {
        super(model, controller, state);

        guiComponentBank = new GuiComponentBank(state.getComponentBank(), controller);
        guiFlightBoard = new GuiFlightBoard(model.getGame().getFlightBoard(), controller);
    }

    @Override
    public Parent getNode() {
        VBox root = new VBox();
        root.getChildren().addAll(guiComponentBank, guiFlightBoard, guiAllShips);
//        root.getChildren().add(new GuiAllShips(model, controller).getNode());
        return root;
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRequestRandComponent();
    }


    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component)
//    {
//        guiComponentBank.notifyRequestComponent(shipBoard, component);
//    }
    {}

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component)
//    {
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.clearHand();
//        ship.onStash(component);
//        cliAllShips.setDirty();
//    }
    {}
    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition)
//    {
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.onRemoveComponent(oldPosition);
//        ship.onStash(component);
//        cliAllShips.setDirty();
//    }
    {}
    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component)
    {
        //todo: can t reject component picked from stashed
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.clearHand();
        guiComponentBank.addUncovered(component);
    }
    {}
    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition)
//    {
//        //can t reject component picked from stashed
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.onRemoveComponent(oldPosition);
//        cliComponentBank.addUncovered(component);
//        cliAllShips.setDirty();
//    }
    {}
    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component)
//    {
//        hasStashed = true;
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.setHand(component);
//        ship.onGrabStashed(index);
//        cliAllShips.setDirty();
//    }
    {}
    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation)
//    {
//        Component placedComponent = shipBoard.getComponentMap().get(point);
//        if (placedComponent != null) {
//            CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//            ship.onPutComponent(point, placedComponent);
//            ship.clearHand();
//            cliAllShips.setDirty();
//        }
//    }
    {}
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
    public void notifyFlightBoardPosition(FlightBoard flightBoard)
//    {
//        // Update the flight board position and mark it as dirty
//        cliFlightBoard = new CliFlightBoard(flightBoard);
//        cliFlightBoard.setDirty();
//    }
    {}
    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex)
//    {
//        // Mark the forecast display as dirty to update it
//        cliForecast.setBlockedForecasts(deckIndex, shipBoard.getColor());
//        cliForecast.setDirty();
//    }
    {}
    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards)
//    {
//        cliForecastCards.setCards(adventureCards);
//        hasForecastDeck = true;
//        cliForecastCards.setDirty();
//    }
    {}
    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index)
//    {
//        // Update the forecast display when a forecast is released
//        if(myShipBoard == shipBoard) {
//            hasForecastDeck = false;
//        }
//        cliForecast.setDirty();
//    }
    {}
    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point)
//    {
//        CliShipHandAndStash ship = shipToCliShip.get(shipBoard);
//        ship.onRemoveComponent(point);
//        cliAllShips.setDirty();
//    }
    {}
//    private boolean componentInHand()
////    {
////        return myShipBoard.getLastComponent() != null && myShipBoard.getLastPosition() == null;
//    {}//    }
}