package it.polimi.ingsw.galaxytruckers.adventureCards;



import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EpidemicCard extends AdventureCard {

    public EpidemicCard() {
        cardStates = new ArrayList<>();
        cardStates.add(CardState.START_CARD);
        cardStates.add(CardState.END_CARD);

        this.name = "[EPIDEMIC]";
    }

    @Override
    public CardState nextStep(GameModel model) {
        if (step == 0) {
            model.epidemic();
            model.passCardToNextPlayer();
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
    public int getFirePower() {
        return 0;
    }

    @Override
    public int getCredits() {
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
    public List<Projectile> getProjectilesType() {
        return null;
    }

    @Override
    public int getSacrifice() {
        return 0;
    }

    @Override
    public int getFlightDaysLost() {
        return 0;
    }

    @Override
    public void landOnPlanet(int i) {

    }
}
