package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#placeShipOnFlightBoard()} and
 * {@link VirtualServer#placeShipOnFlightBoard(int)}
 */
public class PlaceShipOnFlightBoard extends RegisteredRequest {
    private final int startingPosition;

    /**
     * Creates a new PlaceShipOnFlightBoard request with a specified starting position.
     * @param startingPosition the position on the flight board where the ship should be placed
     */
    public PlaceShipOnFlightBoard(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    /**
     * Creates a new PlaceShipOnFlightBoard request without a specified starting position.
     * This will default to placing the ship at the first available position on the flight board.
     */
    public PlaceShipOnFlightBoard() {
        this.startingPosition = -1;
    }

    @Override
    public void execute(VirtualServer server) {
        if (startingPosition == -1) server.placeShipOnFlightBoard();
        else server.placeShipOnFlightBoard(startingPosition);
    }
}
