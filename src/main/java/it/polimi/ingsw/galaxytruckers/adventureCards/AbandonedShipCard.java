package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Goods;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;

import java.util.ArrayList;
import java.util.List;

public class AbandonedShipCard extends AdventureCard{
    private final int credits;
    private final int numResidents;
    private final int flightDaysLost;

    public AbandonedShipCard(int credits, int numResidents, int flightDaysLost){
        cardStates = new ArrayList<>();
        cardStates.add(CardState.ASK_NEXT_PLAYER);
        cardStates.add(CardState.LOSE_RESIDENT);
        cardStates.add(CardState.GRAB_CREDITS);
        cardStates.add(CardState.END_CARD);

        this.name = "[ABANDONED SHIP]";
        this.credits = credits;
        this.numResidents = numResidents;
        this.flightDaysLost = flightDaysLost;
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
    public void landOnPlanet(int i) {}
}