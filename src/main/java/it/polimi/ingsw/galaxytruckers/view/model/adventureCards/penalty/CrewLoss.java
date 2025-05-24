package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

public final class CrewLoss implements Penalty {
    int crew;

    public CrewLoss(int crew) {
        this.crew = crew;
    }

    public int getCrew() {
        return crew;
    }
}
