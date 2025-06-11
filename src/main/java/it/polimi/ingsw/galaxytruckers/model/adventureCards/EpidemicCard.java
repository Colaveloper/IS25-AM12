package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class EpidemicCard extends AdventureCard {


    public EpidemicCard(Game game, Level level, int id) {
        super(game, level, id);
    }

    @Override
    public AdventureState getNextState() {
        for(ShipBoard shipBoard : flightBoard.getOrderedShips()) {

            // check currentShipboard for cabins
            Map<Point, Cabin> cabins = shipBoard.getCabins().entrySet().stream()
                    .filter(e -> {
                        Cabin c = shipBoard.getCabins().get(e.getKey());
                        List<Direction> connectedDirections = Arrays.stream(Direction.values())
                                .filter(dir -> c.getConnectors().get(dir) != Connector.NONE)
                                .toList();
                        for (Direction direction : connectedDirections) {
                            Point neighbour = Direction.getNeighbour(e.getKey(),direction);
                            if (shipBoard.getCabins().containsKey(neighbour) &&
                                    shipBoard.getCabins().get(neighbour).getNumResidents() > 0)
                                return true;
                        }
                        return false;
                    })
                    .filter(e -> shipBoard.getCabins().get(e.getKey()).getNumResidents() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            cabins.keySet().forEach(p -> shipBoard.loseCrew(p));
        }
        return new DrawCardState();
    }
}
