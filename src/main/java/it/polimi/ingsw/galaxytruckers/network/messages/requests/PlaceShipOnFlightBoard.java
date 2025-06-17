package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class PlaceShipOnFlightBoard extends RegisteredRequest {
    private final int startingPosition;

    public PlaceShipOnFlightBoard(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    public PlaceShipOnFlightBoard() {
        this.startingPosition = -1;
    }

    @Override
    public void execute(VirtualServer server) {
        if (startingPosition == -1) server.placeShipOnFlightBoard();
        else server.placeShipOnFlightBoard(startingPosition);
    }
}
