package it.polimi.ingsw.galaxytruckers.adventureCards;


import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class SlaversCard extends AdventureCardDeprecated {

    private final int firePower;
    private final int credits;
    private final int sacrifices;
    private final int flightDaysLost;

    public SlaversCard(FlightBoard flightBoard, int firePower, int credits, int sacrifices, int flightDaysLost) {
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.ACTIVATE_CANNONS);
        //cardStates.add(PlayerAction.SUBMIT_POWER);
        cardStates.add(PlayerAction.LOSE_RESIDENTS);
        //cardStates.add(CardState.ASK_NEXT_PLAYER);
        cardStates.add(PlayerAction.END_CARD);

        this.firePower = firePower;
        this.credits = credits;
        this.sacrifices = sacrifices;
        this.flightDaysLost = flightDaysLost;
        this.name = "[SLAVERS]";
    }

    @Override
    public PlayerAction nextStep() {
        if (step == 0) {
            if (currentShipBoard.getFirePower() > firePower) {
                currentShipBoard.gainCredits(credits);
                flightBoard.displaceShip(currentShipBoard, flightDaysLost);
                step = step + 2;    //2 as the states to skip as a player wins
            } else if (currentShipBoard.getFirePower() == firePower) {
                passCardToNextPlayer();
            }
            else {
                //currentShipBoard.loseCrew();
                //passCardToNextPlayer();
            }
        }
        step ++;
        return cardStates.get(step);
    }

    @Override
    public int getFirePowerThreshold() {
        return firePower;
    }

    @Override
    public int getCreditPrize() {
        return credits;
    }

    @Override
    public int getSacrifice() {
        return sacrifices;
    }

    @Override
    public int getFlightDaysLoss() {
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
    public List<ProjectileDeprecated> getProjectilesType() {
        return null;
    }

}
