package it.polimi.ingsw.galaxytruckers.view.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

public abstract class AdventureCard {
    private final Level cardLevel;
    private final int id;

    protected AdventureCard(Level cardLevel, int id) {
        this.cardLevel = cardLevel;
        this.id = id;
    }

    public Level getCardLevel() {
        return cardLevel;
    }

    public int getId() {
        return id;
    }
}
