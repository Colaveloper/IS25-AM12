package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;

/**
 * ModelEvent signaling that a new card has been drawn
 * @param cardId the id of the current card
 */
public record NewCardEvent(int cardId) implements Event {

    public static NewCardEvent from(AdventureCard card) {
        return new NewCardEvent(
                card.getId()
        );
    }

}
