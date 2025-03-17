package it.polimi.ingsw.galaxytruckers.adventureCards;



import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class SmugglersCard extends AdventureCard{

    private int firePower;
    private int goodsStolen;
    private final List<GoodsType> loot;
    private final int flightDaysLost;


    public SmugglersCard(FlightBoard flightBoard, List<GoodsType> loot, int flightDaysLost, int firePower, int goodsStolen) {
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.ACTIVATE_CANNON);
        cardStates.add(PlayerAction.SUBMIT_POWER);
        cardStates.add(PlayerAction.LOSE_GOODS);
        //cardStates.add(CardState.ASK_NEXT_PLAYER);
        cardStates.add(PlayerAction.MANANGE_GOODS);
        cardStates.add(PlayerAction.END_CARD);

        this.flightDaysLost = flightDaysLost;
        this.loot = loot;
        this.goodsStolen = goodsStolen;
        this.firePower = firePower;
        this.name = "[SMUGGLERS]";
    }

    @Override
    public PlayerAction nextStep() {
        if (step == 1) {
            if (model.getShipPower() > firePower) {
                model.loseFlightDays(flightDaysLost);
                step = step + 2;    //2 as the states to skip as a player wins
            } else if (model.getShipPower() == firePower) {
                model.passCardToNextPlayer();
            }
            else {
                model.loseGoods(goodsStolen);
                model.passCardToNextPlayer();
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


    //UNUSED METHODS------------------------------------------
    @Override
    public void landOnPlanet(int i) {
    }
    @Override
    public List<Boolean> getPlanets() { //do nothing
        return null;
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
