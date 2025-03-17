package it.polimi.ingsw.galaxytruckers.adventureCards;


import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.CardState;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PiratesCard extends AdventureCard{

    private final int firePower;
    private final List<Integer> projectileDirections;
    private final List<Projectile> projectileTypes;
    private List<Integer> playerShot;
    private final int credits;
    private final int flightDaysLost;

    public PiratesCard(int firePower, List<Integer> projectileDirections, List<Projectile> projectileTypes, int flightDaysLost, int credits){
        this.projectileDirections = projectileDirections;
        this.projectileTypes = projectileTypes;

        cardStates = new ArrayList<>();
        cardStates.add(CardState.ACTIVATE_CANNON);
        cardStates.add(CardState.SUBMIT_POWER);
        for (int i = 0; i < projectileTypes.size(); i++){       //adds commands for each cannon shot
            if(projectileTypes.get(i) == Projectile.SMALL_CANNON){
                cardStates.add(CardState.ACTIVATE_SHIELD);
            } else if (projectileTypes.get(i) == Projectile.BIG_CANNON) {
                cardStates.add(CardState.GET_BLASTED);
            } else {
                //TODO: make this launch some kind of exception or proper error message
                System.out.println("ERROR: PROJECTILE OF INCORRECT TYPE IN CONSTRUCTOR");
            }
        }
        cardStates.add(CardState.ACTIVATE_SHIELD);
        cardStates.add(CardState.END_CARD);

        this.firePower = firePower;
        this.flightDaysLost = flightDaysLost;
        this.credits = credits;
        this.playerShot = new ArrayList<>();
        this.name = "[PIRATES]";


    }

    @Override
    public CardState nextStep(GameModel model) {
        if (step == 1) {
            if (model.getShipPower() > firePower) {
                model.loseFlightDays(flightDaysLost);
                model.grabCredits(credits);
                //step = step + projectileTypes.size() + 2;       //skip meteors if player has firepower
            } else if (model.getShipPower() == firePower) {
                model.passCardToNextPlayer();
            }
            else{
                playerShot.add(model.getCurrentPlayerIndex());
                model.passCardToNextPlayer();
            }
        }
        else if(step == 2){

        }
        step ++;
        return cardStates.get(step);
    }

    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    public int getCredits() {
        return credits;
    }

    public int getFirePower() {
        return firePower;
    }

    public List<Integer> getProjectileDirections() {
        return projectileDirections;
    }

    @Override
    public List<Projectile> getProjectilesType() {
        return projectileTypes;
    }

    //UNUSED METHODS-------------------------
    @Override
    public void landOnPlanet(int i) {}

    @Override
    public List<Boolean> getPlanets() {
        return null;
    }

    @Override
    public int getSacrifice() {
        return 0;
    }

    @Override
    public List<GoodsType> getGoods() {
        return null;
    }
}
