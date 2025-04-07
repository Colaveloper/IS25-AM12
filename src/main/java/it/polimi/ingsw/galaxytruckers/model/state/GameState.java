package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Deck;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;

public abstract class GameState {
    protected Game game;
    protected ShipBoard shipBoard;

    public void setGame(Game game) {
        this.game = game;
    }

    public void activateComponent(Point position) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void spendBatteries(Point point, int amount) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void grabReward() {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void chooseShipPiece(int pieceIndex) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void drawCard(Deck deck) {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void loseCrew(Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void loseGood(Point position)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void addGood(Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void removeGood(Point position, GoodsType good)  {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void goNext() {
        throw new IllegalStateException("This action is unsupported in this state");
    }

    public void selectOption(int option) {
        throw new IllegalStateException("This action is unsupported in this state");
    }
}