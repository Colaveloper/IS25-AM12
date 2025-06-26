package it.polimi.ingsw.galaxytruckers.network.messages.requests;

import it.polimi.ingsw.galaxytruckers.network.client.VirtualServer;
import it.polimi.ingsw.galaxytruckers.network.messages.RegisteredRequest;

/**
 * Request class for {@link VirtualServer#chooseShipPiece(int)}
 */
public class ChooseShipPiece extends RegisteredRequest {
    private final int pieceIndex;

    /**
     * Constructor for ChooseShipPiece request.
     *
     * @param pieceIndex the index of the ship piece to choose
     */
    public ChooseShipPiece(int pieceIndex) {
        this.pieceIndex = pieceIndex;
    }

    @Override
    public void execute(VirtualServer server) {
        server.chooseShipPiece(pieceIndex);
    }
}
