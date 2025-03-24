package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class StarDustCard extends AdventureCard {

    List<ShipBoard> invertedShips;

    public StarDustCard(Image image, Level level, FlightBoard flightBoard) {
        super(image, level, flightBoard);

        this.invertedShips = flightBoard.getOrderedShips().reversed();
    }

    public GameState nextStep() {
        for (ShipBoard shipBoard : invertedShips) { // ships go back on the board in inverted flight order
            flightBoard.displaceShip(shipBoard, -shipBoard.getExposedConnectorsNumber());
        }
        return new DrawCardState();
    }
}
