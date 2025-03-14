package adventureCards;

import adventureCards.utils.Choice;
import adventureCards.utils.Goods;
import adventureCards.utils.Projectile;

import java.util.Arrays;
import java.util.List;

public class PiratesCard extends AdventureCard{

    private final int firePower;
    private final List<Integer> projectileDirections;
    private final List<Projectile> projectileTypes;
    private final int credits;


    public PiratesCard(int firePower, List<Integer> projectileDirections, List<Projectile> projectileTypes, int credits){
        super(Arrays.asList(
                Choice.ACTIVATE_CANNON,
                Choice.SUBMIT_POWER,
                Choice.ACTIVATE_SHIELD,
                Choice.GRAB_CREDITS,
                Choice.ASK_NEXT_PLAYER,
                Choice.END_CARD));
        this.firePower = firePower;
        this.projectileDirections = projectileDirections;
        this.projectileTypes = projectileTypes;
        this.credits = credits;
    }


    @Override
    public void landOnPlanet(int i) {       //do nothing

    }

    @Override
    public void resetSteps() {              //do nothing

    }


    @Override
    public List<Boolean> getPlanets() {     //do nothing
        return null;
    }

    //getters for the controller to see
    public int getFirePower() {
        return firePower;
    }

    public List<Integer> getProjectileDirections() {
        return projectileDirections;
    }

    @Override
    public List<Projectile> getProjectilesType() {
        return projectileTypes;
    }

    @Override
    public int getSacrifice() { //do nothing
        return 0;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public List<Goods> getGoods() {     //do nothing
        return null;
    }
}
