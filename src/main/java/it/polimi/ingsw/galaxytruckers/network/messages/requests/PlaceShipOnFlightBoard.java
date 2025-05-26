package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class PlaceShipOnFlightBoard extends RegisteredRequest {
    private final int startingPosition;

    public PlaceShipOnFlightBoard(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    @Override
    public void execute(VirtualServer server) {
        server.placeShipOnFlightBoard(startingPosition);
    }
}
