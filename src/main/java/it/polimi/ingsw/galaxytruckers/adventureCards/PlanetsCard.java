package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class PlanetsCard extends AdventureCardDeprecated {
    //attributes
    private List<Boolean> planets;
    private List<GoodsType> goods;
    private int flightDaysLost;
    private int numPlanets;

    public PlanetsCard(FlightBoard flightBoard, int numPlanets, List<GoodsType> goods, int flightDaysLost){
        //attributes init
        super(flightBoard);
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
        cardStates.add(PlayerAction.CHOOSE_PLANET);
        cardStates.add(PlayerAction.MANAGE_GOODS);
        cardStates.add(PlayerAction.END_CARD);
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep() {
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
    public int getFlightDaysLoss() {
        return flightDaysLost;
    }
    @Override
    public void landOnPlanet(int i) {
        planets.set(i, Boolean.TRUE);
    }
    
    //UNUSED METHODS
    @Override
    public int getFirePowerThreshold() {
        return 0;
    }
    @Override
    public int getCreditPrize() {
        return 0;
    }
    @Override
    public List<Integer> getProjectileDirections() {
        return null;
    }
    @Override
    public List<ProjectileDeprecated> getProjectilesType() {
        return null;
    }
    @Override
    public int getSacrifice() {
        return 0;
    }

}
