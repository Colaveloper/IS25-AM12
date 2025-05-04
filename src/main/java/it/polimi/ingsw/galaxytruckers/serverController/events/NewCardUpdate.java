package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.network.shared.EventHandler;

import java.io.IOException;

public class NewCardUpdate extends Event {
    final int cardId;

    public NewCardUpdate(int cardId) {
        this.cardId = cardId;
    }

    public int getCardId() {
        return cardId;
    }

    @Override
    public void accept(EventHandler eventHandler) throws IOException {
        eventHandler.handleEvent(this);
    }
}
