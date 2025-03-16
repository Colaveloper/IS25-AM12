package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Goods;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import java.util.List;

public abstract class AdventureCard {
    //attributes
    protected int step = -1;
    protected List<CardState> cardStates;
    protected String name;

    //public methods
    public String getName(){
        return name;
    }
    public int getCurrentStep(){
        return step;
    }
    /**sets the step to -1, after calling this method,
    * nextStep() should also be called*/
    public void resetSteps() {
        step = -1;
    }
    public List<CardState> getChoicesList(){
        return cardStates;
    }

    //abstract methods
    /**increments the step by one,
     * returns the next choice*/
    public abstract CardState nextStep(GameModel model);
    public abstract List<Boolean> getPlanets();
    public abstract int getFirePower();
    public abstract int getCredits();
    public abstract List<Goods> getGoods();
    public abstract List<Integer> getProjectileDirections();
    public abstract List<Projectile> getProjectilesType();
    public abstract int getSacrifice();
    public abstract int getFlightDaysLost();
    public abstract void landOnPlanet(int i);
}