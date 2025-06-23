package it.polimi.ingsw.galaxytruckers.view.cliScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;

import java.util.ArrayList;
import java.util.List;

public abstract class CliAdventureScreen extends CliScreen{

    protected final boolean isMyTurn;
    protected final boolean imOut;
    protected final ShipBoard currentShip;
    protected final CliAdventureCard currentAdventureCard;

    public CliAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        imOut = gameState.getImOut();   //if player surrendered
        currentShip = gameState.getShipBoard(); // ship of the current player playing
        isMyTurn = currentShip == myShipBoard;
        AdventureCard adventureCard = model.getGame().getCurrentCard();
        if(adventureCard != null) this.currentAdventureCard = new CliAdventureCard(adventureCard);
        else this.currentAdventureCard = null;
    }

    protected List<String> printCurrentCard() {
        return DescriptionUtils.borderAndTitle(currentAdventureCard.getDescription(), "Current card");
    }
}
