package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.awt.*;
import java.util.Map;

import static java.lang.Math.abs;

public class EpidemicCard extends AdventureCard {


    public EpidemicCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public AdventureState getNextState() {
        for(ShipBoard shipBoard : flightBoard.getOrderedShips()) {

            // check currentShipboard for cabins
            Map<Point, Cabin> cabins = shipBoard.getCabins();
            for (Map.Entry<Point, Cabin> cabinEntry : cabins.entrySet()) {
                for (int i = 0; i < 4; i++) {
                    Point infectionOrigin = new Point(
                            cabinEntry.getKey().x + (i % 2 * 2 - 1),
                            cabinEntry.getKey().y + (1 - abs(i % 2 * 2 - 1))
                    );
                    if (cabins.containsKey(infectionOrigin) // infectionOrigin is a cabin
                            && cabinEntry.getValue().getConnectors().get(i) != Connector.NONE // they are connected
                            && shipBoard.getCabins().get(infectionOrigin).getNumResidents() > 0 // somebody infecting
                            && shipBoard.getCabins().get(cabinEntry.getKey()).getNumResidents() > 0 // somebody to infect
                        ) {
                            shipBoard.loseCrew(cabinEntry.getKey(), 1);
                            break;
                        }
                    }
                }
        }
        return new DrawCardState();
    }
}
