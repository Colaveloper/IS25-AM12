package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Physical;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;

public abstract class AdventureCard implements Physical {
    protected FlightBoard flightBoard;
    protected int currentPlayerIndex;
    protected ShipBoard currentShipBoard;
    protected final Level cardLevel;
    private final Image image;


    protected AdventureCard(Image image, Level cardLevel, FlightBoard flightBoard) {
        this.image = image;
        this.cardLevel = cardLevel;
        this.flightBoard = flightBoard;
        this.currentShipBoard = null;
        this.currentPlayerIndex = 0;
    }

    public Level getCardLevel() {
        return cardLevel;
    }
    public abstract GameState nextStep();

    // Does nothing - does not throw exceptions because I control correct
    // method invocation through GameState
    // TODO : consider removing this method (using GeneralChoiceState)
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
