package it.polimi.ingsw.galaxytruckers;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import com.google.common.collect.BiMap;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.Deck;
import it.polimi.ingsw.galaxytruckers.TempDeck;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;

import java.util.List;
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
    private AdventureCard activeCard;

    public GameModel(Level chosenLevel, Map<Integer, Colors> chosenColors) {
        flightBoard = gameFactory.createFlightBoard();
        deck = gameFactory.createDeck();
    }

    //FLIGHTBOARD-RELATED METHODS

    private ShipBoard getShipFromPlayer(int playerId) {
        return idsToShip.get(playerId);
    }

    private Integer getPlayerFromShip(ShipBoard shipBoard) {
        return idsToShip.inverse().get(shipBoard);
    }

    // return value to be interpreted as "building phase is finished for everybody"
    public boolean placeShipOnFlightBoard(int playerId, int startingPosition) {
        return flightBoard.placeShipOnFlightBoard(getShipFromPlayer(playerId), startingPosition);
    }

    public List<Integer> getOrderedPlayers() {
        return flightBoard.getOrderedShips()
                .stream()
                .map(this::getPlayerFromShip)
                .collect(Collectors.toList());
    }

    public void displaceShip(int playerId, int displacement) {
        flightBoard.displaceShip(getShipFromPlayer(playerId), displacement);
    }

    // returns playerId mapped to finalScore
    public Map<Integer, Integer> getFinalScores() {
        return flightBoard.getFinalScores().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> getPlayerFromShip(entry.getKey()), Map.Entry::getValue
                ));
    }

    public void playerGivesUp(int playerId) {
        flightBoard.giveUp(getShipFromPlayer(playerId));
    }

    // (returns empty set in test flight)
    public Set<Integer> getLappedPlayers () {
        return flightBoard.getLappedShips()
                .stream()
                .map(this::getPlayerFromShip)
                .collect(Collectors.toSet());
    }

    //TEMPORARY CODE, FOR TESTING ONLY ---------------------------------------------
    public GameModel(){
        this.deck = new TempDeck();
    }

    //CARD-RELATED METHODS
    public AdventureCard drawCard(){
        activeCard = deck.drawCard();
        return activeCard;
    }

    public void passCardToNextPlayer(){
        //TODO: pass the card to next player
        System.out.println("Card has been passed to the next player.");
    }

    public void loseResidents(int numResidents){
        //TODO: update current player shipboard to reflect lost residents
        System.out.println("current player has lost " + numResidents + " residents");
    }
    public void grabCredits(int credits){
        //TODO: update current player shipbpard to reflect gain in credits
        System.out.println("current player has received " + credits + " credits");
    }
    public void loseFlightDays(int flightDaysLost){
        //TODO: make currecnt player lose flight days
        //or potentially all players depending on card
        System.out.println("current player has lost " + flightDaysLost + " days");
    }

//    public int throwDice(Boolean activatable) {
//        i = rand;
//        if(activatable) {
//            shipboard.setDice(i);
//        }
//    }
}
