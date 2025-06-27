package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;

import java.util.ArrayList;
import java.util.List;

public class StarDustCard extends AdventureCard {

    private final List<ShipBoard> invertedShips = new ArrayList<>();

    /**
     * Constructs a StarDustCard.
     *
     * @param game the game instance
     * @param level the adventure card level
     * @param id the unique card identifier
     */
    public StarDustCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public void initialize() {
        super.initialize();
        this.invertedShips.clear();
        this.invertedShips.addAll(flightBoard.getOrderedShips().reversed());
    }

    public AdventureState getNextState() {
        for (ShipBoard shipBoard : invertedShips) { // ships go back on the board in inverted flight order
            flightBoard.displaceShip(shipBoard, -shipBoard.getExposedConnectorsNumber());
        }
        return new DrawCardState();
    }
}
