package adventureCards;

import adventureCards.utils.Choice;
import adventureCards.utils.Goods;
import adventureCards.utils.Projectile;

import java.util.ArrayList;
import java.util.List;

public class AbandonedShipCard extends AdventureCard{
    private final int credits;
    private final int numResidents;
    private final int flightDaysLost;

    public AbandonedShipCard(int credits, int numResidents, int flightDaysLost){
        choices = new ArrayList<>();
        choices.add(Choice.ASK_NEXT_PLAYER);
        choices.add(Choice.LOSE_RESIDENT);
        choices.add(Choice.GRAB_CREDITS);
        choices.add(Choice.END_CARD);

        this.name = "[ABANDONED SHIP]";
        this.credits = credits;
        this.numResidents = numResidents;
        this.flightDaysLost = flightDaysLost;
    }

    public List<Choice> getChoicesList(){
        return choices;
    }

    //getters
    @Override
    public int getCredits() {
        return credits;
    }
    @Override
    public int getSacrifice() {
        return numResidents;
    }

    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    //UNUSED METHODS ----------------------------------------
    @Override
    public int getFirePower() {
        return 0;
    }

    @Override
    public List<Integer> getProjectileDirections() {
        return null;
    }
    @Override
    public List<Boolean> getPlanets(){
        return null;
    }
    @Override
    public List<Goods> getGoods(){
        return null;
    }

    @Override
    public List<Projectile> getProjectilesType() {
        return null;
    }

    @Override
    public void landOnPlanet(int i) {

    }
}