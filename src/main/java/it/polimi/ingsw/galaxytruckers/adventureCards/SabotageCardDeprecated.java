package it.polimi.ingsw.galaxytruckers.adventureCards;



import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;


public class SabotageCardDeprecated extends AdventureCardDeprecated {
    public SabotageCardDeprecated(FlightBoard flightBoard) {
        super(flightBoard);
        cardStates = new ArrayList<>();
        //cardStates.add(PlayerAction.SABOTAGE);
        cardStates.add(PlayerAction.END_CARD);

        this.name = "[SABOTAGE]";
    }

    @Override
    public PlayerAction nextStep() {
        step ++;
        return cardStates.get(step);
    }


    //UNUSED METHODS-------------------------------------------
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
    public List<GoodsType> getGoods() {
        return null;
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
    public int getFlightDaysLoss() {
        return 0;
    }

    @Override
    public void landOnPlanet(int i) {

    }
}
