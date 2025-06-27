package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.GuiComponentBank;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.GuiHand;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.client.model.state.ShipBuildingState;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract GUI screen for the ship building phase of the game.
 * <p>
 * This class manages the UI and logic for building ships, including handling the component bank,
 * player hands, and interactions with the ship board. It provides methods for updating the UI
 * in response to game events such as requesting components, placing components, and updating the
 * player's hand. Subclasses should implement specific behaviors for different ship building phases.
 * </p>
 *
 * @author (your name or team)
 */
public abstract class GuiShipBuildingScreen extends GuiGameScreen {

    protected final GuiComponentBank guiComponentBank;
    protected final Map<ShipBoard, GuiHand> guiHands;
    protected final ShipBuildingState state;

    /**
     * Constructs a new GuiShipBuildingScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param state the current ship building state
     */
    public GuiShipBuildingScreen(ClientModel model, ClientControllerInterface controller, ShipBuildingState state) {
        super(model, controller, state);
        this.state = state;
        guiComponentBank = new GuiComponentBank(state.getComponentBank(), getGuiController());
        guiHands = new HashMap<>();
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            guiHands.put(shipBoard, new GuiHand(
                    shipBoard.getLastPosition()==null ? shipBoard.getLastComponent() : null,
                    getGuiController()
            ));
        }
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRequestRandComponent();
        guiHands.get(shipBoard).notifySetHand(component);
        guiStatBox.notifyChange();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRequestComponent(component);
        guiHands.get(shipBoard).notifySetHand(component);
        guiStatBox.notifyChange();
    }


    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        guiComponentBank.notifyRejectComponent(component);
        guiHands.get(shipBoard).notifyClearHand();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiComponentBank.notifyRejectComponent(component);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        Component component = shipBoard.getComponentMap().get(point);
        guiShipBoards.get(shipBoard).notifyPlaceComponent(component, point, orientation);
        guiHands.get(shipBoard).notifyClearHand();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation, Point oldPosition) {
        Component component = shipBoard.getComponentMap().get(point);
        guiShipBoards.get(shipBoard).notifyPlaceComponent(component, point, orientation);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        guiFlightBoard.notifyFlightBoardPosition(shipBoard, position);
        guiStatBox.notifyChange();
    }
}
