package adventureCards;

import adventureCards.utils.Choice;
import adventureCards.utils.Goods;
import adventureCards.utils.Projectile;
import java.util.List;

public abstract class AdventureCard {
    //attributes
    protected int step = 0;
    protected List<Choice> choices;
    protected String name;

    //public methods
    public AdventureCard(){
        //default constructor
    }

    //returns the name of the card
    public String getName(){
        return name;
    }
    public int getCurrentStep(){
        return step;
    }

    /*increments the step by one,
    * returns the next choice*/
    public  Choice nextStep() {
        step++;
        return choices.get(step);
    }

    /*sets the step to -1, after calling this method,
    * nextStep() should also be called*/
    public void resetSteps() {
        step = -1;
    }

    public List<Choice> getChoices(){
        return choices;
    }
//    public void setChoices(List<Choice> choices) {
//        this.choices = choices;
//    }

    //abstract methods
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