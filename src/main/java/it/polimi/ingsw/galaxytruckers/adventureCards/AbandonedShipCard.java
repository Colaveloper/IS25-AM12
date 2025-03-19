package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class AbandonedShipCard extends AdventureCardDeprecated {
    private final int credits;
    private final int numResidents;
    private final int flightDaysLost;

    public AbandonedShipCard(FlightBoard flightBoard, int credits, int numResidents, int flightDaysLost){
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.ASK_IF_PASS);
        cardStates.add(PlayerAction.LOSE_RESIDENTS);
        cardStates.add(PlayerAction.END_CARD);

        this.name = "[ABANDONED SHIP]";
        this.credits = credits;
        this.numResidents = numResidents;
        this.flightDaysLost = flightDaysLost;
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep() {
        if(step == 1){
            currentShipBoard.gainCredits(credits);
            flightBoard.displaceShip(currentShipBoard, flightDaysLost);
        }
        step++;
        return cardStates.get(step);
    }
    @Override
    public int getCreditPrize() {
        return credits;
    }
    @Override
    public int getSacrifice() {
        return numResidents;
    }
    @Override
    public int getFlightDaysLoss() {
        return flightDaysLost;
    }

    //UNUSED METHODS ----------------------------------------
    @Override
    public int getFirePowerThreshold() {
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
    public List<GoodsType> getGoods(){
        return null;
    }
    @Override
    public List<ProjectileDeprecated> getProjectilesType() {
        return null;
    }
    @Override
    public void landOnPlanet(int i) {}
}