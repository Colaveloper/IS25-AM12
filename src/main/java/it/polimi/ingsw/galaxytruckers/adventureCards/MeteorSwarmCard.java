package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class MeteorSwarmCard extends AdventureCard{
    //attributes
//    private List<Integer> projectileDirections;
//    private List<Projectile> projectileTypes;

    public MeteorSwarmCard(FlightBoard flightBoard, List<Integer> projectileDirections, List<Projectile> projectileType){
        //attributes init
        super(flightBoard);
        this.projectileDirections = new ArrayList<>(projectileDirections);
        this.projectileTypes = new ArrayList<>(projectileType);
        this.name = "[METEOR SWARM]";

        //cardState init
        cardStates = new ArrayList<>();
        for (int i = 0; i < projectileType.size(); i++) {
            if(projectileType.get(i) == Projectile.SMALL_METEOR){
                cardStates.add(PlayerAction.ROLL_DICE);
                cardStates.add(PlayerAction.ACTIVATE_SHIELD);
            } else if (projectileType.get(i) == Projectile.LARGE_METEOR) {
                cardStates.add(PlayerAction.ACTIVATE_CANNON);
            } else {
                //TODO: make this launch some kind of exception or proper error message
                System.out.println("ERROR: PROJECTILE OF INCORRECT TYPE IN CONSTRUCTOR");
            }
        }
        cardStates.add(PlayerAction.END_CARD);
    }

    //USED METHODS
    @Override
    public PlayerAction nextStep() {
        step++;
        return cardStates.get(step);
    }
    @Override
    public List<Integer> getProjectileDirections() {
        return projectileDirections;
    }
    @Override
    public List<Projectile> getProjectilesType() {
        return projectileTypes;
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