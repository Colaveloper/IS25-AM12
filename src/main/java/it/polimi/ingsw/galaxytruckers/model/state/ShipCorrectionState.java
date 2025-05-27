package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public non-sealed abstract class ShipCorrectionState extends GameState {
    Set<ShipBoard> validShipBoards;
    Map<ShipBoard, List<Set<Point>>> shipPieces;

    public ShipCorrectionState() {
        this.validShipBoards = new HashSet<>();
        this.shipPieces = new HashMap<>();
    }

    protected abstract void removeAt(ShipBoard shipBoard, Point point);

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        for (ShipBoard shipBoard : game.getShipBoards()) {
            checkShipValidity(shipBoard);
        }
        tryStateTransition();
        Set<ShipBoard> invalidShips = getInvalidShips();
        if (!invalidShips.isEmpty()) {
            game.getEventListener().notifyInvalidShipsUpdateEvent(invalidShips);
        }
        for (ShipBoard shipBoard : shipPieces.keySet()) {
            game.getEventListener().notifyShipNotConnectedEvent(shipBoard, shipPieces.get(shipBoard));
        }
    }

    protected abstract void tryStateTransition();

    private boolean checkShipValidity(ShipBoard shipBoard) {
        if (shipBoard.checkValidity()) {
            validShipBoards.add(shipBoard);
            return true;
        }
        return false;
    }
    
    private boolean checkShipConnection(ShipBoard shipBoard) {
        List<Set<Point>> currentShipPieces = shipBoard.getConnectedSets();
        if (currentShipPieces.size() > 1) {
            shipPieces.put(shipBoard, currentShipPieces);
            return false;
        }
        return true;
    }

    @Override
    public void removeComponent(ShipBoard shipBoard, Point point) {
        if (validShipBoards.contains(shipBoard)) {
            throw new IllegalStateException("Ship Board is already valid");
        }
        removeAt(shipBoard, point);
        boolean canTransition = true;
        if (checkShipValidity(shipBoard)) {
            if (!getInvalidShips().isEmpty()) {
                game.getEventListener().notifyInvalidShipsUpdateEvent(getInvalidShips());
                canTransition = false;
            }
            if (!checkShipConnection(shipBoard)) {
                canTransition = false;
                game.getEventListener().notifyShipNotConnectedEvent(shipBoard, shipPieces.get(shipBoard));
            }
        }
        if (canTransition) tryStateTransition();
    }

    @Override
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        if (!shipPieces.containsKey(shipBoard)) {
            throw new IllegalStateException("You have no ship pieces to choose");
        }
        if (pieceIndex < 0 || pieceIndex >= shipPieces.get(shipBoard).size()) {
            throw new IllegalArgumentException("Invalid piece index");
        }
        Set<Point> componentsToRemove = shipBoard.getComponentMap().keySet();
        componentsToRemove.removeAll(shipPieces.get(shipBoard).get(pieceIndex));
        for (Point point : componentsToRemove) {
            removeAt(shipBoard, point);
        }
        shipPieces.remove(shipBoard);
        game.getEventListener().notifyShipPieceRemoveEvent(shipBoard,componentsToRemove.stream().toList());
        tryStateTransition();
    }

    public Set<ShipBoard> getValidShipBoards() {
        return new HashSet<>(validShipBoards);
    }

    public Map<ShipBoard, List<Set<Point>>> getShipPieces() {
        return new HashMap<>(shipPieces);
    }
    
    public Set<ShipBoard> getInvalidShips() {
        Set<ShipBoard> invalidShips = new HashSet<>(game.getShipBoards());
        invalidShips.removeAll(validShipBoards);
        return invalidShips;
    }
}
