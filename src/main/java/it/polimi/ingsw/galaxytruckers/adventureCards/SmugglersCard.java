package it.polimi.ingsw.galaxytruckers.adventureCards;



import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmugglersCard extends AdventureCard{

    private int firePower;
    private int goodsStolen;
    private final List<GoodsType> loot;
    private final int flightDaysLost;


    public SmugglersCard(List<GoodsType> loot, int flightDaysLost, int firePower, int goodsStolen) {
        cardStates = new ArrayList<>();
        cardStates.add(CardState.ACTIVATE_CANNON);
        cardStates.add(CardState.SUBMIT_POWER);
        cardStates.add(CardState.LOSE_GOODS);
        cardStates.add(CardState.ASK_NEXT_PLAYER);
        cardStates.add(CardState.GRAB_GOODS);
        cardStates.add(CardState.END_CARD);

        this.flightDaysLost = flightDaysLost;
        this.loot = loot;
        this.goodsStolen = goodsStolen;
        this.firePower = firePower;
    }

    @Override
    public CardState nextStep(GameModel model) {
        if (step == 1) {
            if (model.getShipPower() > firePower) {
                step = step + 3;
                //return cardStates.get(step);
            } else if (model.getShipPower() == firePower) {
                model.passCardToNextPlayer();
                //return cardStates.get(step);
            }
            else {
                model.loseFlightDays(goodsStolen);
                model.passCardToNextPlayer();
                //return cardStates.get(step);
            }
        }
        step ++;
        return cardStates.get(step);
    }


    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    @Override
    public int getFirePower() {
        return firePower;
    }

    @Override
    public List<GoodsType> getGoods() {     //do nothing
        return null;
    }


        //none of these do anything

    @Override
    public void landOnPlanet(int i) {   //do nothing

    }



    @Override
    public List<Boolean> getPlanets() { //do nothing
        return null;
    }



    @Override
    public int getCredits() {                           //do nothing
        return 0;
    }



    @Override
    public List<Integer> getProjectileDirections() {    //do nothing
        return null;
    }//do nothing

    @Override
    public List<Projectile> getProjectilesType() {  //do nothing
        return null;
    }

    @Override
    public int getSacrifice() {                         //do nothing
        return 0;
    }       //do nothing


}
