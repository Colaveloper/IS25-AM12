package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class GuiAdventureScreen extends GuiGameScreen {

    protected final AdventureState state;
    
    public GuiAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        this.state = gameState;
    }

    protected boolean isMyTurn() {
        return state.getShipBoard() == myShipBoard;
    }

    protected Optional<GuiAdventureCard> guiAdventureCard() {
        return state.getCurrentCard() == null ? Optional.empty() : Optional.of(new GuiAdventureCard(state.getCurrentCard().getId()));
    }
}
