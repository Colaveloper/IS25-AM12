package it.polimi.ingsw.galaxytruckers.adventureCards;


import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlaversCard extends AdventureCard {

    private final int firePower;
    private final int credits;
    private final int sacrifices;
    private final int flightDaysLost;

    public SlaversCard(int firePower, int credits, int sacrifices, int flightDaysLost) {
        cardStates = new ArrayList<>();
        cardStates.add(CardState.ACTIVATE_CANNON);
        cardStates.add(CardState.SUBMIT_POWER);
        cardStates.add(CardState.LOSE_RESIDENT);
        //cardStates.add(CardState.ASK_NEXT_PLAYER);
        cardStates.add(CardState.END_CARD);

        this.firePower = firePower;
        this.credits = credits;
        this.sacrifices = sacrifices;
        this.flightDaysLost = flightDaysLost;
        this.name = "[SLAVERS]";
    }

    @Override
    public CardState nextStep(GameModel model) {
        if (step == 1) {
            if (model.getShipPower() > firePower) {
                model.grabCredits(credits);
                model.loseFlightDays(flightDaysLost);
                step = step + 2;    //2 as the states to skip as a player wins
            } else if (model.getShipPower() == firePower) {
                model.passCardToNextPlayer();
            }
            else {
                model.loseResidents(sacrifices);
                model.passCardToNextPlayer();
            }
        }
        step ++;
        return cardStates.get(step);
    }

    @Override
    public int getFirePower() {
        return firePower;
    }

    @Override
    public int getCredits() {
        return credits;
    }

    @Override
    public int getSacrifice() {
        return sacrifices;
    }

    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    //UNUSED METHODS--------------------------
    @Override
    public void landOnPlanet(int i) {
    }

    @Override
    public List<Boolean> getPlanets() {
        return null;
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

}
