package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.shipBuilding.Connector;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AdventureCard {
    //attributes
    protected int step = -1;
    protected List<PlayerAction> cardStates;
    protected final FlightBoard flightBoard;
    protected String name;
    protected ShipBoard currentShipBoard;
    protected int diceRoll;
    protected List<Integer> projectileDirections;
    protected List<Projectile> projectileTypes;
    protected int projectileIndex;

    protected AdventureCard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
        this.currentShipBoard = flightBoard.getOrderedShips().getFirst();
        cardStates = new ArrayList<>();
    }

    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }

    // called after player failed to prevent projectile
    public void projectileAtPlayer() {
        Point projectileTarget = currentShipBoard.getFirstComponentAt(diceRoll, projectileDirections.get(projectileIndex));
        if (projectileTypes.get(projectileIndex) == Projectile.SMALL_METEOR &&
                currentShipBoard
                        .getComponentMap()
                        .get(projectileTarget)
                        .getConnectors()
                        .get(projectileDirections.get(projectileIndex))
                        == Connector.NONE
            ) { return;}
        currentShipBoard.removeComponent(projectileTarget); //in case of big/small fire, bigMeteor and smallMeteor on openConnector
    }

    //public methods
    public String getName(){
        return name;
    }
    public int getCurrentStep(){
        return step;
    }
    /**sets the step to -1, after calling this method,
    * nextStep() should also be called*/
    public void passCardToNextPlayer() {
        int currentPlayerIndex = flightBoard.getOrderedShips().indexOf(currentShipBoard);

        if (currentPlayerIndex < flightBoard.getOrderedShips().size()) {
            // still another player
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex+1);
            step = -1; // bringing card to initial step
        } else {
            // no players left
            // no action! going on with the steps
        }
    }
    public List<PlayerAction> getChoicesList(){
        return cardStates;
    }

    public void setDiceRoll(int diceRoll){
        this.diceRoll = diceRoll;
    }

    //abstract methods // TODO: condense in render() and toString()
    /**increments the step by one,
     * returns the next choice*/
    public abstract PlayerAction nextStep();
    public abstract List<Boolean> getPlanets();
    public abstract int getFirePowerThreshold();
    public abstract int getCreditPrize();
    public abstract List<GoodsType> getGoods();
    public abstract List<Integer> getProjectileDirections();
    public abstract List<Projectile> getProjectilesType();
    public abstract int getSacrifice();
    public abstract int getFlightDaysLoss();
    public abstract void landOnPlanet(int i);
    public  List<ShipBoard> getInvolvedShips() {
        return flightBoard.getOrderedShips();
    }

}