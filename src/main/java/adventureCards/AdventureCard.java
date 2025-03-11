package adventureCards;

import java.util.List;


public abstract class AdventureCard {
    //attributes
    protected FlightBoard flightBoard;
    protected ShipBoard shipBoard;
    protected List<Goods> goods;
    protected int flightDays;
    protected Dice dice;
    protected int credits;

    //methods
    public void grabGoods(){}
    public void placeGoods(){}
    public void spendBatteries(){}
    public void submitPower(){}
    public int rollDice(){return dice.roll();}
    public int grabCredits(){return credits;}
    public void loseResident(){}
    public void loseBatteries(){}
    public abstract void activate();
    public int loseFlightDays(){return flightDays;}
}