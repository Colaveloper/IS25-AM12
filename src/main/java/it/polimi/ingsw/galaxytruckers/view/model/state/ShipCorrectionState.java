package it.polimi.ingsw.galaxytruckers.view.model.state;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ShipCorrectionState extends GameState {
    Set<ShipBoard> validShipBoards;
    Map<ShipBoard, List<Set<Point>>> shipPieces;

    public ShipCorrectionState() {
        this.validShipBoards = new HashSet<>();
        this.shipPieces = new HashMap<>();
    }

    @Override
    public List<StateActions> getAvailableActions() {
        //TODO: logic for retrieving the client's shipboard and checking if
        // they should remove or choose ship piece
        return List.of(
                StateActions.REMOVE_COMPONENT,
                StateActions.CHOOSE_SHIP_PIECE
        );
    }
}
