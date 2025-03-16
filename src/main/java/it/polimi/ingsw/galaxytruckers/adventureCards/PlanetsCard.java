package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class PlanetsCard extends AdventureCard{
    //attributes
    private List<Boolean> planets;
    private List<GoodsType> goods;
    private int flightDaysLost;
    private int numPlanets;

    public PlanetsCard(int numPlanets, List<GoodsType> goods, int flightDaysLost){
        //attributes init
        planets = new ArrayList<>();
        for (int i = 0; i < numPlanets; i++) {
            //TRUE indicates an occupied planet
            planets.add(Boolean.FALSE);
        }
        this.goods = new ArrayList<>(goods);
        this.flightDaysLost = flightDaysLost;
        this.name = "[PLANETS]";

        //cardState init
        cardStates = new ArrayList<>();
        cardStates.add(CardState.CHOOSE_PLANET);
        cardStates.add(CardState.END_CARD);
    }

    //USED METHODS
    @Override
    public CardState nextStep(GameModel model) {
        step++;
        return cardStates.get(step);
    }
    @Override
    public List<Boolean> getPlanets() {
        return planets;
    }
    @Override
    public List<GoodsType> getGoods() {
        return goods;
    }
    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }
    @Override
    public void landOnPlanet(int i) {
        planets.set(i, Boolean.TRUE);
    }
    
    //UNUSED METHODS
    @Override
    public int getFirePower() {
        return 0;
    }
    @Override
    public int getCredits() {
        return 0;
    }
    @Override
    public List<Integer> getProjectileDirections() {
        return null;
    }
    @Override
    public List<Projectile> getProjectilesType() {
        return null;
    }
    @Override
    public int getSacrifice() {
        return 0;
    }

}
