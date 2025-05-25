package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

public class ChooseShipPiece extends RegisteredRequest {
    private final int pieceIndex;

    public ChooseShipPiece(int pieceIndex) {
        this.pieceIndex = pieceIndex;
    }

    @Override
    public void execute(VirtualServer server) {
        server.chooseShipPiece(pieceIndex);
    }
}
