package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStationCard extends AdventureCard{
    //attributes
    private List<GoodsType> goodsList;
    private int numRequiredResidents;
    private int flightDaysLost;

    public AbandonedStationCard(List<GoodsType> goodsList, int numRequiredResidents, int flightDaysLost){
        //states init
        super();
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.ASK_IF_PASS);
        cardStates.add(PlayerAction.MANANGE_GOODS);
        cardStates.add(PlayerAction.END_CARD);

        //attributes init
        this.name = "[ABANDONED STATION]";
        this.goodsList = new ArrayList<>(goodsList);
        this.numRequiredResidents = numRequiredResidents;
        this.flightDaysLost = flightDaysLost;
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep(GameModel model) {
        if(step == 1){
            model.loseFlightDays(flightDaysLost);
        }
        step++;
        return cardStates.get(step);
    }
    @Override
    public List<GoodsType> getGoods() {
        return goodsList;
    }
    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    //UNUSED METHODS ------------------------------------
    @Override
    public List<Boolean> getPlanets() {
        return null;
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
    @Override
    public void landOnPlanet(int i) {

    }
}