package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;

/**
 * Event signaling that a new card has been drawn
 * @param cardId the id of the current card
 */
public record NewCardEvent(int cardId) implements Event {

    public static NewCardEvent from(AdventureCard card) {
        return new NewCardEvent(
                0
                //TODO: add ids to cards
                // card.getId()
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
