package it.polimi.ingsw.galaxytruckers.state;

import it.polimi.ingsw.galaxytruckers.Deck;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;

public abstract class GameState {
    protected AdventureCard adventureCard;

    public void activateComponent(ShipBoard shipBoard, Point position) {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public void makeBooleanChoice(boolean choice) {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public void makeChoice(int choice) {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public void chooseShipPiece(int pieceIndex) {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public boolean drawCard(Deck deck) {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public void chooseCrewToLose(Point position)  {
        throw new UnsupportedOperationException("This action is unsupported in this state");
    }

    public abstract GameState getNextState();


}
