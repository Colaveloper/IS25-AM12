package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public non-sealed class ShipCorrectionState extends GameState implements GameStateInterface {
    private final Set<ShipBoard> validShipBoards;
    private final Map<ShipBoard, List<Set<Point>>> shipPiecesMap;
    private final boolean shouldDiscard;

    public ShipCorrectionState(boolean shouldDiscard) {
        this.shouldDiscard = shouldDiscard;
        this.validShipBoards = new HashSet<>();
        this.shipPiecesMap = new HashMap<>();
    }

    protected void removeAt(ShipBoard shipBoard, Point point) {
        if (shouldDiscard) shipBoard.discardComponent(point);
        else shipBoard.removeComponent(point);
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
        for (ShipBoard shipBoard : game.getShipBoards()) {
            if (checkShipValidity(shipBoard)) {
                checkShipConnection(shipBoard);
            }
        }
        game.getEventListener().notifyGameStateUpdateEvent(this);
        tryStateTransition();
    }

    protected void tryStateTransition() {
        if (getValidShipBoards().size() == game.getShipBoards().size() && getShipPiecesMap().isEmpty()) {
            game.submitStateTransition(() -> game.setCurrentState(new ShipInitializationState()));
        }
    }

    private boolean checkShipValidity(ShipBoard shipBoard) {
        if (shipBoard.checkValidity()) {
            synchronized (validShipBoards) {
                validShipBoards.add(shipBoard);
            }
            return true;
        }
        return false;
    }
    
    private boolean checkShipConnection(ShipBoard shipBoard) {
        List<Set<Point>> currentShipPieces = shipBoard.getConnectedSets();
        if (currentShipPieces.size() > 1) {
            synchronized (shipPiecesMap) {
                shipPiecesMap.put(shipBoard, currentShipPieces);
            }
            return false;
        }
        return true;
    }

    @Override
    public void removeComponent(ShipBoard shipBoard, Point point) {
        synchronized (validShipBoards) {
            if (validShipBoards.contains(shipBoard)) {
                throw new IllegalStateException("Ship Board is already valid");
            }
        }
        removeAt(shipBoard, point);
        game.getEventListener().notifyRemoveComponentEvent(shipBoard, point);
        if (checkShipValidity(shipBoard)) {
            if (!checkShipConnection(shipBoard)) {
                game.getEventListener().notifyShipNotConnectedEvent(shipBoard, shipPiecesMap.get(shipBoard));
            } else {
                game.getEventListener().notifyValidateShipEvent(shipBoard);
            }
        }
        tryStateTransition();
    }

    @Override
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        List<Set<Point>> shipPieces = getShipPieces(shipBoard);
        if (shipPieces == null) {
            throw new IllegalStateException("You have no ship pieces to choose");
        }
        if (pieceIndex < 0 || pieceIndex >= shipPieces.size()) {
            throw new IllegalArgumentException("Invalid piece index");
        }
        Set<Point> componentsToRemove = shipBoard.getComponentMap().keySet();
        componentsToRemove.removeAll(shipPieces.get(pieceIndex));
        for (Point point : componentsToRemove) {
            removeAt(shipBoard, point);
        }
        synchronized (shipPiecesMap) {
            shipPiecesMap.remove(shipBoard);
        }
        game.getEventListener().notifyShipPieceRemovalEvent(shipBoard,pieceIndex);
        tryStateTransition();
    }

    public Set<ShipBoard> getValidShipBoards() {
        synchronized (validShipBoards) {
            return new HashSet<>(validShipBoards);
        }
    }

    public Map<ShipBoard, List<Set<Point>>> getShipPiecesMap() {
        synchronized (shipPiecesMap) {
            Map<ShipBoard, List<Set<Point>>> mapCopy = new HashMap<>();
            for (ShipBoard ship : shipPiecesMap.keySet()) {
                mapCopy.put(ship, new ArrayList<>(shipPiecesMap.get(ship)));
            }
            return mapCopy;
        }
    }

    public List<Set<Point>> getShipPieces(ShipBoard shipBoard) {
        synchronized (shipPiecesMap) {
            return shipPiecesMap.get(shipBoard);
        }
    }

    public boolean getShouldDiscard() {
        return shouldDiscard;
    }
}
