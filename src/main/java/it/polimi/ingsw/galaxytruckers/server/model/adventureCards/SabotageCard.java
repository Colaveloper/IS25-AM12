package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.Dice;
import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;

import java.awt.*;

public class SabotageCard extends AdventureCard {

    private static final Dice dice = new Dice() {};

    /**
     * Constructs a SabotageCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param id the unique card identifier
     */
    protected SabotageCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public AdventureState getNextState() {
        currentShipBoard = flightBoard.getOrderedShips().getFirst();
        for (ShipBoard shipBoard : flightBoard.getOrderedShips()) {
            if (shipBoard.getCrewSize() < currentShipBoard.getCrewSize()) {
                currentShipBoard = shipBoard;
            }
        }

        currentShipBoard.discardComponent(new Point(dice.getAsInt(),dice.getAsInt()));

        return new DrawCardState();
    }
}
