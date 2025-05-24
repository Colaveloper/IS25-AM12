package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.List;

public class StarDustCard extends AdventureCard {

    List<ShipBoard> invertedShips;

    public StarDustCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public void initialize() {
        super.initialize();
        this.invertedShips = flightBoard.getOrderedShips().reversed();
    }

    public AdventureState getNextState() {
        for (ShipBoard shipBoard : invertedShips) { // ships go back on the board in inverted flight order
            flightBoard.displaceShip(shipBoard, -shipBoard.getExposedConnectorsNumber());
        }
        return new DrawCardState();
    }
}
