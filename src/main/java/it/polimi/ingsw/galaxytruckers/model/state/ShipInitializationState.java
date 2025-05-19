package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.events.SelectionPointsEvent;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class ShipInitializationState extends GameState {
    private final Map<ShipBoard, Set<Point>> shipRelevantCabins = new HashMap<>();

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        for (ShipBoard shipBoard : game.getShipBoards()) {
            Set<Point> relevantCabins = shipBoard.getCabins().keySet().stream()
                    .filter(p -> shipBoard.getCrewTypeOptions(p).size() > 1)
                    .collect(Collectors.toSet());
            if (!relevantCabins.isEmpty()) {
                shipRelevantCabins.put(shipBoard, relevantCabins);
            } else {
                goNext(shipBoard);
            }
        }
        //TODO: add logic to notify the client that they should initialize cabins
        for (ShipBoard shipBoard : shipRelevantCabins.keySet()) {
            game.getEventListener().notifySelectionPointEvent(shipBoard,shipRelevantCabins.get(shipBoard).stream().toList());
        }
        tryStateTransition();
    }

    @Override
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        if (shipRelevantCabins.containsKey(shipBoard) && shipRelevantCabins.get(shipBoard).contains(point)) {
            shipBoard.initializeCabin(point, crewType);
            shipRelevantCabins.get(shipBoard).remove(point);
            Set<Point> updatedCabins = shipRelevantCabins.get(shipBoard).stream()
                            .filter(p -> shipBoard.getCrewTypeOptions(p).size() > 1)
                            .collect(Collectors.toSet());
            if (updatedCabins.isEmpty()) {
                goNext(shipBoard);
                tryStateTransition();
            } else {
                shipRelevantCabins.put(shipBoard, updatedCabins);
                game.getEventListener().notifySelectionPointEvent(shipBoard,updatedCabins.stream().toList());
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
                .forEach(p -> shipBoard.initializeCabin(p,CrewType.HUMAN));
        shipRelevantCabins.remove(shipBoard);
        game.getEventListener().notifySelectionPointEvent(shipBoard,new ArrayList<>());
    }

    public void tryStateTransition() {
        if (shipRelevantCabins.isEmpty()) {
            game.setCurrentState(new DrawCardState());
        }
    }

}
