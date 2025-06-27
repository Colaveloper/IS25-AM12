package it.polimi.ingsw.galaxytruckers.client.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Represents the state where players correct issues with their ships in the Galaxy Truckers game.
 * This state is triggered when a ship has validation issues, such as disconnected components
 * or rule violations. Players must fix these issues by either removing problematic components
 * or choosing which ship piece to keep when disconnected.
 */
public final class ShipCorrectionState extends GameState implements GameStateInterface {
    /** Set of ship boards that have passed validation */
    private final Set<ShipBoard> validShipBoards;

    /** Map of ship boards to their disconnected pieces (for ships that are not connected) */
    private final Map<ShipBoard, List<Set<Point>>> shipPieces;

    /** Flag indicating whether removed components should be counted as losses */
    private final boolean shouldDiscard;

    /** Flag indicating whether the local player's ship is connected */
    private boolean isConnected;

    /** Flag indicating whether the local player's ship is valid */
    private boolean isValid;

    /**
     * Creates a new ShipCorrectionState with the specified parameters.
     * Initializes the state with the player's ship, valid ship boards, disconnected ship pieces,
     * and discard flag. Determines the initial validity and connectivity status of the player's ship.
     *
     * @param myShip The ship board of the local player
     * @param validShipBoards Set of ship boards that have passed validation
     * @param shipPieces Map of ship boards to their disconnected pieces
     * @param shouldDiscard Flag indicating whether removed components should be counted as losses
     */
    public ShipCorrectionState(ShipBoard myShip, Set<ShipBoard> validShipBoards, Map<ShipBoard, List<Set<Point>>> shipPieces, boolean shouldDiscard) {
        this.myShip = myShip;
        isValid = validShipBoards.contains(myShip);
        isConnected = !shipPieces.containsKey(myShip);
        this.validShipBoards = validShipBoards;
        this.shipPieces = shipPieces;
        this.shouldDiscard = shouldDiscard;
    }

    @Override
    public List<StateActions> getAvailableActions() {
        if(!isValid) return List.of(StateActions.REMOVE_COMPONENT);
        if(!isConnected) return List.of(StateActions.CHOOSE_SHIP_PIECE);
        return List.of();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        shipBoard.removeComponent(point);
        if (shouldDiscard) shipBoard.incrementLosses(1);
        game.getObservers().forEach(observer -> observer.notifyRemoveComponent(shipBoard, point));
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if(myShip == shipBoard) isConnected = true;
        List<Point> removedPoints = shipBoard.removeShipPiece(shipPieces.get(shipBoard), pieceIndex);
        if (shouldDiscard) shipBoard.incrementLosses(removedPoints.size());
        game.getObservers().forEach(observer -> observer.notifyChooseShipPiece(shipBoard, pieceIndex, removedPoints));
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        if(myShip == shipBoard) {
            isConnected = false;
            isValid = true;
        }
        validShipBoards.add(shipBoard);
        this.shipPieces.put(shipBoard, shipPieces);
        game.getObservers().forEach(observer -> observer.notifyShipNotConnected(shipBoard, shipPieces));
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        if(myShip == shipBoard) {
            isConnected = true;
            isValid = true;
        }
        shipPieces.remove(shipBoard);
        game.getObservers().forEach(observer -> observer.notifyShipValidated(shipBoard));
    }

    /**
     * Gets the set of ship boards that have passed validation.
     * This represents ships that meet the basic validation requirements,
     * though they may still have connectivity issues.
     *
     * @return Set of ship boards that have passed validation
     */
    public Set<ShipBoard> getValidShipBoards() {
        return validShipBoards;
    }

    /**
     * Gets the mapping of ship boards to their disconnected pieces.
     * For each ship board with connectivity issues, this returns a list of sets of points,
     * where each set represents a disconnected piece of the ship.
     *
     * @return Map of ship boards to their disconnected pieces
     */
    public Map<ShipBoard, List<Set<Point>>> getShipPieces() {
        return shipPieces;
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        int numResidents = shipBoard.initializeCabin(point, crewType);
        game.getObservers().forEach(observer -> observer.notifyInitializeCabin(shipBoard, point, crewType, numResidents));
    }

}
