package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class OpenSpaceCard extends AdventureCardDeprecated {
    //attributes
    private int numPlayers;

    public OpenSpaceCard(FlightBoard flightBoard, int numPlayers){
        //attributes init
        super(flightBoard);
        this.numPlayers = numPlayers;
        this.name = "[OPEN SPACE]";

        //cardState init
        cardStates = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            cardStates.add(PlayerAction.ACTIVATE_ENGINES);
        }
        cardStates.add(PlayerAction.END_CARD);

    }

    //USED METHODS
    @Override
    public PlayerAction nextStep() {
        step++;
        return cardStates.get(step);
    }

    //UNUSED METHODS
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
