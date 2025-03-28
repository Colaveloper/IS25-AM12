package it.polimi.ingsw.galaxytruckers;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.io.IOException;
import java.util.List;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameModel {
    private BiMap<Integer, ShipBoard> idsToShip;
    private GameFactory gameFactory;
    private FlightBoard flightBoard;
    private Deck deck;
//    private int maxCardPlays; //TEMPORARY, defines the maximum times a card can be played
//    private int activeCardPlayCount;
    private List<Integer> playerShot;//TODO: list of shipboard, method maps coming int to ships here

    public GameModel(Level chosenLevel, Map<Integer, Colors> chosenColors) throws IOException {
        switch (chosenLevel) {
            case Level.TEST -> gameFactory = new TestFactory();
            case Level.SECOND -> gameFactory = new SecondFactory();
        }
        idsToShip = HashBiMap.create(
                chosenColors.entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            choice -> gameFactory.createShipBoard(new ComponentBank(), choice.getValue())
                    ))
        );
        flightBoard = gameFactory.createFlightBoard(idsToShip.values());
        deck = gameFactory.createDeck();
//        deck = new TempDeck(flightBoard); // for mocking purposes only
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
        return flightBoard.getAndRemoveLappedShips() // TODO: restore pure getter!!!
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
     * @return whether a ship adheres to corporate standards
     */
    public boolean checkShipValidity(int playerId) {
        return getShipFromPlayer(playerId).checkValidity();
    }

    // DECK-INTERACTION METHODS
    /**
     * Makes the forecast deck unavailable to other players
     * @return the forecast deck in string format
     */
    public String acquireForecast(int playerId, int deckIndex) {
        return deck.getForecastDeck(deckIndex)
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    } // TODO: make return image if asked to
    /**
     * Makes the forecast deck available to other players again
     */
    public void releaseForecast(int playerId) {
        // TODO: implement when concurrency logic is clearer
    }



    // FLIGHT METHODS
    /**
     * removes all components in connected sets not selected by the player, after ship broke
     */
    void letPartOfTheShipGo(int playerId, int setToSave) {
        ShipBoard damagedShip = getShipFromPlayer(playerId);
        List<Set<Point>> connectedSets = damagedShip.getConnectedSets();
        try {
            IntStream.range(0, connectedSets.size())
                    .filter(index -> index != setToSave)
                    .mapToObj(connectedSets::get)
                    .flatMap(Set::stream)
                    .forEach(damagedShip::removeComponent);
        } catch (IndexOutOfBoundsException e) {
            // TODO: let the user know he messed up and let him chose again
        }
    }



    //TEMPORARY CODE, FOR TESTING ONLY ---------------------------------------------
//    public GameModel(){
//        this.deck = new TempDeck(flightBoard);
//        maxCardPlays = 4;
//        activeCardPlayCount = 0;
//    }
//
//    public void setPlayerShot(List<Integer> playerShot) {
//        this.playerShot = playerShot;
//    }
//
//    public void shootPlayers() {
//        for (Integer p : playerShot){
//            System.out.println("player " + p + " gets shot");
//        }
//    }
//
//    public int getCurrentPlayerIndex(){
//        return activeCardPlayCount;
//    }

    //CARD-RELATED METHODS
//    public void drawCard(){
//        currentCard = deck.drawCard();
////        activeCardPlayCount = 0;
//    }
//
//    public void resetSteps(){
//        currentCard.passCardToNextPlayer();
//    }
//
//    public List<PlayerAction> getCardStates(){
//        return currentCard.getChoicesList();
//    }
//
//    public String getCardName() {
//        return currentCard.getName();
//    }
//
//    public int getCardSacrifice() {
//        return currentCard.getSacrifice();
//    }
//
//    public int getCardCredits() {
//        return currentCard.getCreditPrize();
//    }
//
//    public int getCardFlightDaysLost() {
//        return currentCard.getFlightDaysLoss();
//    }


    /**
     * Used by the controller to query a client
     * @return a player ID and his expected action
     */
//    public Query<Integer, PlayerAction> getNextQueryToPlayer() {
//        return new Query<>(getPlayerFromShip(currentCard.getCurrentShipBoard()), currentCard.nextStep());
//    }
//
//    public  void  passCardToNextPlayer() {
//        currentCard.passCardToNextPlayer();
//    }
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

//    public List<GoodsType> getCardGoods(){
//        return currentCard.getGoods();
//    }

    /**
     * Activates a double cannon / double engine / shield, at the specified location, spending one battery
     * @throws IllegalStateException if there is no activatable at position
     */
    public void activateComponent(int playerID, Point position){
        getShipFromPlayer(playerID).activateComponent(position);
        // System.out.println("Component at ("+position.toString()+" now active));
    }
//
//    public void landOnPlanet(int i){
//        //TODO: current player lands on planet
//        currentCard.landOnPlanet(i);
//    }
//
//    public List<ShipBoard> getInvolvedShips() {
//        return currentCard.getInvolvedShips();
//    }
}
