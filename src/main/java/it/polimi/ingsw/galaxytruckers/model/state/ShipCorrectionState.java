package it.polimi.ingsw.galaxytruckers.model.state;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public non-sealed class ShipCorrectionState extends GameState implements GameStateInterface {
    private final Set<ShipBoard> validShipBoards;
    private final Map<ShipBoard, List<Set<Point>>> shipPiecesMap;
    private final boolean shouldDiscard;
    private final Set<ShipBoard> pendingShipBoards = new HashSet<>();
    private final Object lock = new Object();

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

    public ShipCorrectionState(boolean shouldDiscard) {
        this.shouldDiscard = shouldDiscard;
        this.validShipBoards = new HashSet<>();
        this.shipPiecesMap = new HashMap<>();
    }

    private void removeAt(ShipBoard shipBoard, Point point) {
        if (shouldDiscard) shipBoard.discardComponent(point);
        else shipBoard.removeComponent(point);
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        for (ShipBoard shipBoard : game.getShipBoards()) {
            if (checkShipValidity(shipBoard)) {
                checkShipConnection(shipBoard);
            }
        }
        tryStateTransition();
    }

    private void tryStateTransition() {
        synchronized (lock) {
            if (!expired) {
                Set<ShipBoard> remainingShipBoards = game.getShipBoards().stream()
                        .filter(s -> !getValidShipBoards().contains(s) || getShipPieces(s) != null)
                        .filter(s -> !pendingShipBoards.contains(s))
                        .collect(Collectors.toSet());
                if (remainingShipBoards.isEmpty()) {
                    expired = true;
                    pendingShipBoards.forEach(this::defaultAction);
                    game.submitStateTransition(() -> game.setCurrentState(new ShipInitializationState()));
                }
            }
        }
    }

    private void defaultAction(ShipBoard shipBoard) {
        synchronized (lock) {
            if (!validShipBoards.contains(shipBoard)) {
                shipBoard.removeAll(shouldDiscard);
            } else if (getShipPieces(shipBoard) != null) {
                shipBoard.keepShipPiece(getShipPieces(shipBoard),0,shouldDiscard);
            }
        }
    }

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
            synchronized (lock) {
                shipPiecesMap.put(shipBoard, currentShipPieces);
            }
            return false;
        }
        return true;
    }

    @Override
    public void removeComponent(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            if (validShipBoards.contains(shipBoard)) {
                throw new IllegalStateException("Ship Board is already valid");
            }
        }
        removeAt(shipBoard, point);
        synchronized (lock) {
            if (checkShipValidity(shipBoard)) {
                if (!checkShipConnection(shipBoard)) {
                    game.getEventListener().notifyShipNotConnectedEvent(shipBoard, shipPiecesMap.get(shipBoard));
                } else {
                    game.getEventListener().notifyValidateShipEvent(shipBoard);
                }
            }
            tryStateTransition();
        }
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
        shipBoard.keepShipPiece(shipPieces, pieceIndex, shouldDiscard);
        synchronized (lock) {
            shipPiecesMap.remove(shipBoard);
            tryStateTransition();
        }
    }

    public Set<ShipBoard> getValidShipBoards() {
        Set<ShipBoard> res;
        synchronized (lock) {
            res = new HashSet<>(validShipBoards);
        }
        return res;
    }

    public Map<ShipBoard, List<Set<Point>>> getShipPiecesMap() {
        Map<ShipBoard, List<Set<Point>>> mapCopy = new HashMap<>();
        synchronized (lock) {
            for (ShipBoard ship : shipPiecesMap.keySet()) {
                mapCopy.put(ship, new ArrayList<>(shipPiecesMap.get(ship)));
            }
        }
        return mapCopy;
    }

    public List<Set<Point>> getShipPieces(ShipBoard shipBoard) {
        List<Set<Point>> res;
        synchronized (lock) {
            res = shipPiecesMap.get(shipBoard);
        }
        return res;
    }

    public boolean getShouldDiscard() {
        return shouldDiscard;
    }

    @VisibleForTesting
    public Set<ShipBoard> getPendingShipBoards() {
        Set<ShipBoard> res;
        synchronized (lock) {
            res = pendingShipBoards;
        }
        return res;
    }
}
