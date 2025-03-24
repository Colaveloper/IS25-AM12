package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class AbandonedStationCardDeprecated extends AdventureCardDeprecated {
    //attributes
    private List<GoodsType> goodsList;
    private final int numRequiredResidents;
    private final int flightDaysLost;

    public AbandonedStationCardDeprecated(FlightBoard flightBoard, List<GoodsType> goodsList, int numRequiredResidents, int flightDaysLost){
        //states init
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.ASK_IF_PASS);
        cardStates.add(PlayerAction.MANAGE_GOODS);
        cardStates.add(PlayerAction.END_CARD);

        //attributes init
        this.name = "[ABANDONED STATION]";
        this.goodsList = new ArrayList<>(goodsList);
        this.numRequiredResidents = numRequiredResidents;
        this.flightDaysLost = flightDaysLost;
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep() {
//        if(step == 1){
//
//            flightBoard.
//            flightBoard.displaceShip(currentShipBoard, flightDaysLost);
//        }
//        step++;
        return cardStates.get(step);
    }
    @Override
    public List<GoodsType> getGoods() {
        return goodsList;
    }
    @Override
    public int getFlightDaysLoss() {
        return flightDaysLost;
    }

    //UNUSED METHODS ------------------------------------
    @Override
    public List<Boolean> getPlanets() {
        return null;
    }
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
    @Override
    public void landOnPlanet(int i) {

    }
}