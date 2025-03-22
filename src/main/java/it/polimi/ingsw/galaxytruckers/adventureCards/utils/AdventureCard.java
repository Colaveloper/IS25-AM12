package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.Physical;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

public abstract class AdventureCard implements Physical {
    protected FlightBoard flightBoard;
    protected int currentPlayerIndex;
    protected final Level cardLevel;
    private final Image image;

    protected AdventureCard(Image image, Level cardLevel) {
        this.image = image;
        this.cardLevel = cardLevel;
    }

    public abstract GameState nextStep();

    // Does nothing - does not throw exceptions because I control correct
    // method invocation through GameState
    public void choose(boolean choice) {}

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public String getDescription() {
        return "Level "+ cardLevel +" card:"; // TODO: add description
    }
}
