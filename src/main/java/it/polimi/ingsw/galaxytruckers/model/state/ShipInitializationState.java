package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;

public final class ShipInitializationState extends GameState {
    private final Map<ShipBoard, Map<CrewType, Set<Point>>> shipRelevantCabins = new HashMap<>();

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        for (ShipBoard shipBoard : game.getShipBoards()) {
            shipRelevantCabins.put(shipBoard, new HashMap<>());
            Map<CrewType, Set<Point>> crewTypeCabin = shipRelevantCabins.get(shipBoard);
            for (Point p : shipBoard.getCabins().keySet()) {
                Set<CrewType> availableCrewTypes = shipBoard.getCrewTypeOptions(p);
                for (CrewType crewType : availableCrewTypes) {
                    if (crewType != CrewType.HUMAN) {
                        if (!crewTypeCabin.containsKey(crewType)) {
                            crewTypeCabin.put(crewType, new HashSet<>());
                        }
                        crewTypeCabin.get(crewType).add(p);
                    }
                }
            }
            if (crewTypeCabin.isEmpty()) {
                goNext(shipBoard);
            }
        }
        //TODO: add logic to notify the client that they should initialize cabins
        tryStateTransition();
    }

    @Override
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        if (shipRelevantCabins.containsKey(shipBoard) &&
                shipRelevantCabins.get(shipBoard).containsKey(crewType) &&
                shipRelevantCabins.get(shipBoard).get(crewType).contains(point)) {

            shipBoard.initializeCabin(point, crewType);
            shipRelevantCabins.get(shipBoard).remove(crewType);
            if (shipRelevantCabins.get(shipBoard).isEmpty()) {
                goNext(shipBoard);
            }
        } else {
            throw new IllegalArgumentException("The specified cabin does not need to be initialized");
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        Map<Point, Cabin> cabins = shipBoard.getCabins();
        cabins.keySet().stream()
                .filter(p -> cabins.get(p).getNumResidents() == 0)
                .forEach(p -> shipBoard.initializeCabin(p, CrewType.HUMAN));
        shipRelevantCabins.remove(shipBoard);
    }

    private void tryStateTransition() {
        if (shipRelevantCabins.isEmpty()) {
            game.setCurrentState(new DrawCardState());
        }
    }

    public Map<ShipBoard, Map<CrewType, Set<Point>>> getShipRelevantCabins() {
        return shipRelevantCabins;
    }
}
