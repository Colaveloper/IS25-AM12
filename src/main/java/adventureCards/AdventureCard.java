package adventureCards;

import adventureCards.utils.Choice;
import adventureCards.utils.Goods;
import adventureCards.utils.Projectile;

import java.util.List;

public abstract class AdventureCard {
    protected int step = 0;
    private List<Choice> choices;
    public abstract List<Boolean> getPlanets();
    public abstract int getFirePower();
    public abstract int getCredits();
    public abstract List<Goods> getGoods();
    public abstract List<Integer> getProjectileDirections();
    public abstract List<Projectile> getProjectilesType();
    public abstract int getSacrifice();

    public  Choice nextStep() {
        step++;
        return choices.get(step);
    };
    public abstract void landOnPlanet(int i);

    public void resetSteps() {
        step = 0;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }


    public AdventureCard (List<Choice> choices) {
        this.choices = choices;
    }

}