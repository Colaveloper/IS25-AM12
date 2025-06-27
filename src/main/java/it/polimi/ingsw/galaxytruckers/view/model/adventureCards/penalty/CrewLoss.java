package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

/**
 * Represents a penalty that causes the loss of crew members in the game.
 * Implements the {@link Penalty} interface.
 */
public final class CrewLoss implements Penalty {
    int crew;

    /**
     * Constructs a CrewLoss penalty with the specified number of crew members lost.
     *
     * @param crew the number of crew members to lose
     */
    public CrewLoss(int crew) {
        this.crew = crew;
    }

    /**
     * Returns the number of crew members lost.
     *
     * @return the number of crew members lost
     */
    public int getCrew() {
        return crew;
    }
}
