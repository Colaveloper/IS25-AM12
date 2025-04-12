package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;

import java.util.List;

import static java.lang.Math.abs;

public class StarDustCard extends AdventureCard {

    List<ShipBoard> invertedShips;

    public StarDustCard(Level level) {
        super(level);
    }

    @Override
    public void initialize(FlightBoard flightBoard) {
        super.initialize(flightBoard);
        this.invertedShips = flightBoard.getOrderedShips().reversed();
    }

    public GameState nextStep() {
        for (ShipBoard shipBoard : invertedShips) { // ships go back on the board in inverted flight order
            flightBoard.displaceShip(shipBoard, -shipBoard.getExposedConnectorsNumber());
        }
        return new DrawCardState();
    }
}
