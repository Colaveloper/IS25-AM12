package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.Dice;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.awt.*;

public class SabotageCard extends AdventureCard {

    private static final Dice dice = new Dice() {};

    protected SabotageCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public GameState nextStep() {
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
