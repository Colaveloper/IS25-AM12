package it.polimi.ingsw.galaxytruckers;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import com.google.common.collect.BiMap;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.List;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameModel {
    private BiMap<Integer, ShipBoard> idsToShip;
    private BuildingTime buildingTime;
    private Dice dice;
    private GameFactory gameFactory;
    private FlightBoard flightBoard;
    private Deck deck;
    private AdventureCard currentCard;
    private int maxCardPlays; //TEMPORARY, defines the maximum times a card can be played
    private int activeCardPlayCount;
    private List<Integer> playerShot;//TODO: list of shipboard, method maps coming int to ships here

    public GameModel(Level chosenLevel, Map<Integer, Colors> chosenColors) {
        flightBoard = gameFactory.createFlightBoard();
        deck = gameFactory.createDeck();
    }

    // UTILITY METHODS
    /**
     * @return translation from a player ID to his ShipBoard
     */
    private ShipBoard getShipFromPlayer(int playerId) {
        if (!idsToShip.containsKey(playerId)) {
            throw new IllegalArgumentException("No player with such id");
        }
        return idsToShip.get(playerId);
    }
    /**
     * @return translation from a player's ShipBoard to his ID
     */
    private Integer getPlayerFromShip(ShipBoard shipBoard) {
        if (!idsToShip.inverse().containsKey(shipBoard)) {
            throw new IllegalArgumentException("No such shipboard");
        }
        return idsToShip.inverse().get(shipBoard);
    }

    // FLIGHTBOARD-RELATED METHODS
    /** @return true iff the building phase is finished for everybody
     * @throws IllegalArgumentException if the starting position is not legal
     */
    public boolean placeShipOnFlightBoard(int playerId, int startingPosition) {
        return flightBoard.placeShipOnFlightBoard(getShipFromPlayer(playerId), startingPosition);
    }
    /**
     * @return  a list of player IDs (players ahead in the flight have lower index)
     */
    public List<Integer> getOrderedPlayers() {
        return flightBoard.getOrderedShips()
                .stream()
                .map(this::getPlayerFromShip)
                .collect(Collectors.toList());
    }
    /** Moves the player's ship on the FlightBoard
     */
    public void displaceShip(int playerId, int displacement) {
        flightBoard.displaceShip(getShipFromPlayer(playerId), displacement);
    } // TODO: hide from controller
    /**
     * @return  for each player ID, the score he obtained at the end of the game
     */
    public Map<Integer, Integer> getFinalScores() {
        return flightBoard.getFinalScores().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> getPlayerFromShip(entry.getKey()), Map.Entry::getValue
                ));
    }
    /** Makes the player with the specified ID give up
     * */
    public void chooseToGiveUp(int playerId) {
        flightBoard.giveUp(getShipFromPlayer(playerId));
    }
    /**
     * @return players lapped by the leader, or the empty set if in test flight
     */
    public Set<Integer> getLappedPlayers () {
        return flightBoard.getLappedShips()
                .stream()
                .map(this::getPlayerFromShip)
                .collect(Collectors.toSet());
    }

    // SHIP-BUILDING METHODS
    /**
     * Makes a covered component the last component
     */
    public void requestRanComponent(int playerId) {
        getShipFromPlayer(playerId).requestRandComponent();
    } // TODO: handle concurrency!
    /**
     * Makes the requested uncovered component the last component
     */
    public void requestComponent(int playerId, int componentId) {
        getShipFromPlayer(playerId).requestComponent(componentId);
    } // TODO: handle concurrency!
    /**
     * Makes the last component available to other players
     */
    public void rejectComponent(int playerId) {
        getShipFromPlayer(playerId).rejectComponent();
    }
    /**
     * Rotates anti-clockwise the last component
     * @throws IllegalStateException if no component can be turned
     */
    public void rotateComponent (int playerId) {
        getShipFromPlayer(playerId).rotateComponent();
    }
    /**
     * Stashes up to two last components for each player
     */
    public void stashComponent (int playerId) {
        getShipFromPlayer(playerId).stashComponent();
    } // TODO: make available for second flight only!
    /**
     * Un-stashes the indexed component, making it the last component
     * @throws IllegalArgumentException if there is not such component in the stash
     */
    public void takeStashedComponent (int playerId, int componentId) {
        getShipFromPlayer(playerId).grabStashedComponent(componentId);
    } // TODO: make available for second flight only!
    /**
     * Places the last component in the specified position of the ship of the specified player
     */
    public void placeComponent(int playerId, Point position) {
        getShipFromPlayer(playerId).placeComponent(position);
    } // TODO: handle illegal positions!
    /**
     *
     */
    void removeComponent(int playerId, Point position) {
        getShipFromPlayer(playerId).removeComponent(position);
    } // TODO: hide from controller, consider whether to remove

    boolean checkShipValidity(int playerId) {
        return getShipFromPlayer(playerId).checkValidity();
    }





    //TEMPORARY CODE, FOR TESTING ONLY ---------------------------------------------
    public GameModel(){
        this.deck = new TempDeck();
        maxCardPlays = 4;
        activeCardPlayCount = 0;
    }

    public void setPlayerShot(List<Integer> playerShot) {
        this.playerShot = playerShot;
    }

    public void shootPlayers() {
        for (Integer p : playerShot){
            System.out.println("player " + p + " gets shot");
        }
    }

    public int getCurrentPlayerIndex(){
        return activeCardPlayCount;
    }

    //CARD-RELATED METHODS
    public void drawCard(){
        currentCard = deck.drawCard();
        activeCardPlayCount = 0;
    }

    public void resetSteps(){
        currentCard.passCardToNextPlayer();
    }

    public List<PlayerAction> getCardStates(){
        return currentCard.getChoicesList();
    }

    public String getCardName() {
        return currentCard.getName();
    }

    public int getCardSacrifice() {
        return currentCard.getSacrifice();
    }

    public int getCardCredits() {
        return currentCard.getCredits();
    }

    public int getCardFlightDaysLost() {
        return currentCard.getFlightDaysLost();
    }

    public PlayerAction getNextPlayerAction() {
        return currentCard.nextStep();
    }

//    public void passCardToNextPlayer(){
//        activeCardPlayCount++;
//        if(activeCardPlayCount < 4){
//            activeCard.resetSteps();    //reset only if there is another player
//            //TODO: pass the card to next player
//            System.out.println("Card has been passed to the next player.");
//        }
//        else {
//            System.out.println("Card can't be played anymore");
//        }
//
//    }

    public int getShipPower() {
        //TODO: get actual shipboard model to do this
        return 2;
    }

    public void epidemic() {
        //TODO: all players lose 1 crew member in paired cabins
        System.out.println("Epidemic strikes!");
    }

    public void loseResidents(int numResidents){
        //TODO: update current player shipboard to reflect lost residents
        System.out.println("current player has lost " + numResidents + " residents");
    }
    public void grabCredits(int credits){
        //TODO: update current player shipboard to reflect gain in credits
        System.out.println("current player has received " + credits + " credits");
    }
    public void loseFlightDays(int flightDaysLost){
        //TODO: make current player lose flight days
        //or potentially all players depending on card
        System.out.println("current player has lost " + flightDaysLost + " days");
    }

    public int getExposedConnectors() {
        //TODO: use shipboard method to get exposed connectors
        System.out.println("current player has 3 exposed connectors");
        return 3;
    }

    public void loseGoods(int goods){
        System.out.println("current player has lost " + goods + " goods");
    }

    public void loseFlightDaysLeastResidents(int flightDaysLost){
        //TODO: select the player with least number of residents to lose flight days
        System.out.println("Player with least number of residents has lost " + flightDaysLost + " days");
    }

    public void sabotage(){
        //TODO: select player with least amount of residents
        //roll two dice for column and two dice for row
        //lose that component
        System.out.println("Player with least amount of residents has been sabotaged!");
    }

    public void grabGoods(List<GoodsType> goodsList){
        //TODO: give goods to current player
        System.out.println("Current player has been given the goods");
    }

    public List<GoodsType> getCardGoods(){
        return currentCard.getGoods();
    }

    //TODO: possibly condense these three methods into one method ------------------------------------
    public void activateEngine(int x, int y){
        //TODO: increase the current players engine power by spending batteries
        System.out.println("Increasing engine power at (x=" + x +",y="+ y +") for the current player");
    }

    public void activateCannon(int x, int y){
        //TODO: increase the current players engine power by spending batteries
        System.out.println("Increasing cannon power at (x=" + x +",y="+ y +") for the current player");
    }

    public void activateShield(int x, int y){
        //TODO: increase the current players engine power by spending batteries
        System.out.println("Increasing shield power at (x=" + x +",y="+ y +") for the current player");
    }
    //TODO: ----------------------------------------------------------------------------------------------

    public void landOnPlanet(int i){
        //TODO: current player lands on planet
        currentCard.landOnPlanet(i);
    }

    public void fireCannonAtPlayer(){
        //TODO: roll dice and fire
        currentCard.fireCannonAtPlayer();
    }

    public int rollDice(){
        Dice dice = Dice.create();
        return dice.roll();
    }
}
