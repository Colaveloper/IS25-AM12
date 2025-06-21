package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;

public non-sealed class ShipInitializationState extends GameState implements GameStateInterface {
    private final Map<ShipBoard, Map<CrewType, Set<Point>>> shipRelevantCabins = new HashMap<>();
    private final Set<ShipBoard> pendingShipBoards = new HashSet<>();

    private final Object lock = new Object();

    @Override
    public void skip(ShipBoard shipBoard) {
        synchronized (lock) {
            pendingShipBoards.add(shipBoard);
        }
    }

    @Override
    public void cancelSkip(ShipBoard shipBoard) {
        synchronized (lock) {
            pendingShipBoards.remove(shipBoard);
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
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
                initHumans(shipBoard,false);
            }
        }
        game.getEventListener().notifyGameStateUpdateEvent(this);
        tryStateTransition();
    }

    @Override
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        Map<CrewType, Set<Point>> crewTypeMap = getCrewTypeMap(shipBoard);
        if (crewTypeMap != null && crewTypeMap.containsKey(crewType) && crewTypeMap.get(crewType).contains(point)) {
            shipBoard.initializeCabin(point, crewType);
            crewTypeMap.remove(crewType);
            game.getEventListener().notifyCabinInitializationEvent(shipBoard,point,crewType);
            if (crewTypeMap.isEmpty()) {
                initHumans(shipBoard,true);
            }
        } else {
            throw new IllegalArgumentException("The specified cabin does not need to be initialized");
        }
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        initHumans(shipBoard, true);
    }

    private void initHumans(ShipBoard shipBoard, boolean stateTransition) {
        Map<Point, Cabin> cabins = shipBoard.getCabins();
        cabins.keySet().stream()
                .filter(p -> cabins.get(p).getNumResidents() == 0)
                .forEach(p -> {
                    shipBoard.initializeCabin(p, CrewType.HUMAN);
                    game.getEventListener().notifyCabinInitializationEvent(shipBoard,p,CrewType.HUMAN);
                });
        synchronized (lock) {
            shipRelevantCabins.remove(shipBoard);
            if (stateTransition) tryStateTransition();
        }
    }

    private void tryStateTransition() {
        synchronized (lock) {
            if (shipRelevantCabins.isEmpty() || shipRelevantCabins.keySet().equals(pendingShipBoards)) {
                pendingShipBoards.forEach(s -> initHumans(s,false));
                game.submitStateTransition(() -> game.setCurrentState(new DrawCardState()));
            }
        }
    }

    public Map<ShipBoard, Map<CrewType, Set<Point>>> getShipRelevantCabins() {
        synchronized (lock) {
            Map<ShipBoard, Map<CrewType, Set<Point>>> mapCopy = new HashMap<>();
            for (ShipBoard ship : shipRelevantCabins.keySet()) {
                mapCopy.put(ship, new HashMap<>(shipRelevantCabins.get(ship)));
            }
            return mapCopy;
        }
    }

    public Map<CrewType, Set<Point>> getCrewTypeMap(ShipBoard shipBoard) {
        synchronized (lock) {
            return shipRelevantCabins.get(shipBoard);
        }
    }
}
