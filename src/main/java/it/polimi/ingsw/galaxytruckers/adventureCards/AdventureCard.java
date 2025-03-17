package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.List;

public abstract class AdventureCard {
    //attributes
    protected int step = -1;
    protected List<PlayerAction> cardStates;
    protected final FlightBoard flightBoard;
    protected String name;
    protected ShipBoard currentShipBoard;
    protected int diceRoll;
    //protected final List<Integer> projectileDirections;

    protected AdventureCard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
        this.currentShipBoard = flightBoard.getOrderedShips().getFirst();
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
            passCardToNextPlayer();
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

    //abstract methods
    /**increments the step by one,
     * returns the next choice*/
    public abstract PlayerAction nextStep();
    public abstract List<Boolean> getPlanets();
    public abstract int getFirePower();
    public abstract int getCredits();
    public abstract List<GoodsType> getGoods();
    public abstract List<Integer> getProjectileDirections();
    public abstract List<Projectile> getProjectilesType();
    public abstract int getSacrifice();
    public abstract int getFlightDaysLost();
    public abstract void landOnPlanet(int i);

    public abstract void fireCannonAtPlayer(int projectileIndex);
}