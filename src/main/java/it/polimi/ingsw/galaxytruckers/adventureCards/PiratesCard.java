package it.polimi.ingsw.galaxytruckers.adventureCards;


import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class PiratesCard extends AdventureCard{

    private final int firePowerThreshold;
    private final List<Integer> projectileDirections;
    private final List<Projectile> projectileTypes;
    private List<ShipBoard> defeatedPlayers;
    private final int creditPrize;
    private final int flightDaysLoss;
//    private int projectileHittingIndex;

    public PiratesCard(
            FlightBoard flightBoard,
            int firePowerThreshold,
            int flightDaysLoss,
            int creditPrize,
            List<Integer> projectileDirections,
            List<Projectile> projectileTypes
    ) {
        super(flightBoard);
        // ESTABLISHING WHO WINS, DRAWS, OR GETS DEFEATED BY PIRATES
        cardStates.add(PlayerAction.ACTIVATE_CANNONS);
        for (int i = 0; i < projectileTypes.size(); i++){           //adds commands for each cannon shot
            if(projectileTypes.get(i) == Projectile.LIGHT_FIRE){    //TODO: AND there is a shield in that direction
                cardStates.add(PlayerAction.ROLL_DICE);
                cardStates.add(PlayerAction.ACTIVATE_SHIELDS);
            } else if (projectileTypes.get(i) == Projectile.HEAVY_FIRE) {
                cardStates.add(PlayerAction.ROLL_DICE);
            } else {
                //TODO: make this launch some kind of exception or proper error message
                System.out.println("ERROR: PROJECTILE OF INCORRECT TYPE IN CONSTRUCTOR");
            }
        }
        //cardStates.add(CardState.ACTIVATE_SHIELD);
        cardStates.add(PlayerAction.END_CARD);


        this.projectileDirections = projectileDirections;
        this.projectileTypes = projectileTypes;
        this.firePowerThreshold = firePowerThreshold;
        this.flightDaysLoss = flightDaysLoss;
        this.creditPrize = creditPrize;
        this.defeatedPlayers = new ArrayList<>();
        this.name = "[PIRATES]";

    }

    @Override
    public PlayerAction nextStep() {
        // flightBoard.getOrderedShips()

        if (step == 0) {
            if (currentShipBoard.getFirePower() > firePowerThreshold) {
                // current player defeats pirates
                flightBoard.displaceShip(currentShipBoard, flightDaysLoss);
                currentShipBoard.gainCredits(creditPrize);
                //step = step + projectileTypes.size() + 2;       //skip meteors if player has firepower
            } else if (currentShipBoard.getFirePower() == firePowerThreshold) {
                // current player draws with pirates
                passCardToNextPlayer();
            } else {
                // pirates defeat current player
                defeatedPlayers.add(currentShipBoard);
                passCardToNextPlayer();
            }
        } else if (step == 1) {

        }
//        if (cardStates.get(step) == PlayerAction.ACTIVATE_SHIELD) {
//
//        }


        step ++;
        return cardStates.get(step);
    }

    @Override
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public int getCreditPrize() {
        return creditPrize;
    }

    public int getFirePowerThreshold() {
        return firePowerThreshold;
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
