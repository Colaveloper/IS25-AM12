package adventureCards;

import adventureCards.utils.Choice;
import adventureCards.utils.Goods;
import adventureCards.utils.Projectile;

import java.util.Arrays;
import java.util.List;

public class PlanetsCard extends AdventureCard{
    //attributes
    private List<Goods> goods;
    private List<Boolean> planets = Arrays.asList(false, false, false);

    public PlanetsCard(List<Goods> goods, int planets) {
        super(Arrays.asList(
                Choice.CHOOSE_PLANET,
                Choice.GRAB_GOODS,
                Choice.ASK_NEXT_PLAYER,
                Choice.END_CARD
        ));
        this.goods = goods;
        for (int i = 0; i < planets; i++) {
            this.planets.add(i, false);
        }
    }

    //methods

    @Override
    public List<Boolean> getPlanets(){
        return planets;
    }



    @Override
    public int getFirePower() {
        return 0;
    }

    @Override
    public int getCredits() {
        return 0;
    }

    @Override
    public List<Goods> getGoods() {
        return goods;
    }

    @Override
    public List<Integer> getProjectileDirections() {
        return null;
    }

    @Override
    public List<Projectile> getProjectilesType() {  //do nothing
        return null;
    }

    @Override
    public int getSacrifice() {
        return 0;
    }


    @Override
    public void landOnPlanet(int i) {
        planets.set(i, true);
    }

}
