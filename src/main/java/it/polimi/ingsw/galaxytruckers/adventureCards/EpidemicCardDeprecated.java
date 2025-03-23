package it.polimi.ingsw.galaxytruckers.adventureCards;



import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class EpidemicCardDeprecated extends AdventureCardDeprecated {
    public EpidemicCardDeprecated(FlightBoard flightBoard) {
        super(flightBoard);
        this.name = "[EPIDEMIC]";

        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.START_CARD);
        cardStates.add(PlayerAction.END_CARD);
    }

    @Override
    public PlayerAction nextStep() {
        if (step == 0) {
            //model.epidemic();
        }
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
