package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class CombatZoneCard extends AdventureCard{
    //attributes
    private int flightDaysLost;
    private int numResidentsLost;
    private List<Integer> projectileDirections;
    private List<Projectile> projectileType;

    public CombatZoneCard(FlightBoard flightBoard, int flightDaysLost, int numResidentsLost, List<Integer> projectileDirections, List<Projectile> projectileType){
        //cardState init
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.START_CARD);
        cardStates.add(PlayerAction.ACTIVATE_ENGINE);
        cardStates.add(PlayerAction.ACTIVATE_CANNON);
        cardStates.add(PlayerAction.END_CARD);

        //attributes init
        this.name = "[COMBAT ZONE]";
        this.flightDaysLost = flightDaysLost;
        this.numResidentsLost = numResidentsLost;
        this.projectileDirections = new ArrayList<>(projectileDirections);
        this.projectileType = new ArrayList<>(projectileType);
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep(GameModel model) {
        if(step == 0){
            model.loseFlightDaysLeastResidents(flightDaysLost);
        }
        step++;
        return cardStates.get(step);
    }
    @Override
    public List<Integer> getProjectileDirections() {
        //0 down, 1 left, 2 up, 3 right
        return projectileDirections;
    }
    @Override
    public List<Projectile> getProjectilesType() {
        return projectileType;
    }
    @Override
    public int getFlightDaysLost() {
        return flightDaysLost;
    }

    //UNUSED METHODS -------------------------------
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
    public int getSacrifice() {
        return 0;
    }
    @Override
    public void landOnPlanet(int i) {

    }
}
