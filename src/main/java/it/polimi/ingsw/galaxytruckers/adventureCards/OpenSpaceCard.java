package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class OpenSpaceCard extends AdventureCard{
    //attributes
    private int numPlayers;

    public OpenSpaceCard(int numPlayers){
        //attributes init
        this.numPlayers = numPlayers;

        //cardState init
        cardStates = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            cardStates.add(CardState.ACTIVATE_ENGINE);
        }
        cardStates.add(CardState.END_CARD);

    }

    //USED METHODS
    @Override
    public CardState nextStep(GameModel model) {
        step++;
        return cardStates.get(step);
    }

    //UNUSED METHODS
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
