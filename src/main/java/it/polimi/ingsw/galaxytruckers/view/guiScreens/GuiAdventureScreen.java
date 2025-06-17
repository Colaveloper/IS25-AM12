package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

public abstract class GuiAdventureScreen extends GuiGameScreen {

    protected final boolean isMyTurn;
    protected final boolean imOut;
    protected final ShipBoard currentShip;
    protected final GuiAdventureCard currentCard;
    
    public GuiAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        imOut = gameState.getImOut();   //if player surrendered
        currentShip = gameState.getShipBoard(); // ship of the current player playing
        isMyTurn = currentShip == myShipBoard;
        this.currentCard = new GuiAdventureCard(gameState.getCurrentCard().getId());
    }
}
