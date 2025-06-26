package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Cabin;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;

/**
 * Represents the state of the game where players initialize their ship cabins.
 */
public non-sealed class ShipInitializationState extends GameState implements GameStateInterface {
    private final Map<ShipBoard, Map<CrewType, Set<Point>>> shipRelevantCabins = new HashMap<>();
    private final Set<ShipBoard> pendingShipBoards = new HashSet<>();

    private final Object lock = new Object();

    /**
     * Signals that the player associated with the ship board is inactive
     * and that a state transition should occur if all other players
     * have finished initializing their ships.
     *
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public void skip(ShipBoard shipBoard) {
        synchronized (lock) {
            pendingShipBoards.add(shipBoard);
            tryStateTransition();
        }
    }

    @Override
    public void cancelSkip(ShipBoard shipBoard) {
        synchronized (lock) {
            pendingShipBoards.remove(shipBoard);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Changes state to {@link DrawCardState} if no ship has
     * options to choose from when initializing their cabins</p>
     *
     * @param game the game to associate with this state
     */
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
                initHumans(shipBoard, false);
            }
        }
        tryStateTransition();
    }

    /**
     * Initializes a cabin on the ship board with the specified crew type.
     * Calls {@link ShipBoard#initializeCabin(Point, CrewType)}. Changes state
     * to {@link DrawCardState} if all ship boards have no more cabins
     * to initialize
     *
     * @param shipBoard the ship board initializing the cabin
     * @param point     the position of the cabin
     * @param crewType  the type of crew to assign to the cabin
     * @throws IllegalArgumentException if the specified cabin does not need to be initialized
     */
    @Override
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        Map<CrewType, Set<Point>> crewTypeMap = getCrewTypeMap(shipBoard);
        if (crewTypeMap != null && crewTypeMap.containsKey(crewType) && crewTypeMap.get(crewType).contains(point)) {
            shipBoard.initializeCabin(point, crewType);
            crewTypeMap.remove(crewType);
            if (crewTypeMap.isEmpty()) {
                initHumans(shipBoard, true);
            }
        } else {
            throw new IllegalArgumentException("The specified cabin does not need to be initialized");
        }
    }

    /**
     * Initializes all empty cabins on the ship board with humans.
     * Calls {@link ShipBoard#initializeCabin(Point, CrewType)}. Changes state
     * to {@link DrawCardState} if all ship boards have no more cabins
     * to initialize
     *
     * @param shipBoard the ship board of the player that requested the action
     */
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
                });
        synchronized (lock) {
            shipRelevantCabins.remove(shipBoard);
            if (stateTransition) tryStateTransition();
        }
    }

    private void tryStateTransition() {
        synchronized (lock) {
            if (!expired && (shipRelevantCabins.isEmpty() || pendingShipBoards.containsAll(shipRelevantCabins.keySet()))) {
                expired = true;
                game.submitStateTransition(() -> {
                    pendingShipBoards.forEach(s -> initHumans(s, false));
                    game.getEventListener().notifyStartAdventure();
                    game.setCurrentState(new DrawCardState());
                });
            }
        }
    }

    /**
     * Returns a copy of the map containing all ship boards and the available
     * cabin positions for each crew type.
     *
     * @return a map where the keys are ship boards and the values are maps
     * containing crew types and their corresponding cabin positions.
     */
    public Map<ShipBoard, Map<CrewType, Set<Point>>> getShipRelevantCabins() {
        Map<ShipBoard, Map<CrewType, Set<Point>>> mapCopy = new HashMap<>();
        synchronized (lock) {
            for (ShipBoard ship : shipRelevantCabins.keySet()) {
                mapCopy.put(ship, new HashMap<>(shipRelevantCabins.get(ship)));
            }
        }
        return mapCopy;
    }

    /**
     * Returns a map of crew types and their corresponding cabin positions.
     * It returns a live reference rather that a copy
     *
     * @param shipBoard the ship board for which to get the crew type map
     * @return a map where the keys are crew types and the values are sets of points
     * representing the cabin positions for that crew type.
     */
    public Map<CrewType, Set<Point>> getCrewTypeMap(ShipBoard shipBoard) {
        Map<CrewType, Set<Point>> res;
        synchronized (lock) {
            res = shipRelevantCabins.get(shipBoard);
        }
        return res;
    }

    /**
     * Should be used for testing purposes only.
     * @return a set of ship boards that have skipped
     */
    @VisibleForTesting
    public Set<ShipBoard> getPendingShipBoards() {
        Set<ShipBoard> res;
        synchronized (lock) {
            res = new HashSet<>(pendingShipBoards);
        }
        return res;
    }
}
