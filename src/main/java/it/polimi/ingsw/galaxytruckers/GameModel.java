package it.polimi.ingsw.galaxytruckers;
// DESCRIPTION:
// the main logical component that provides all the methods to the controller
// to access and modify the state of the game

import com.google.common.collect.BiMap;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GameModel {
    BiMap<Integer, ShipBoard> idsToShip;
    BuildingTime buildingTime;
    Dice dice;
    GameFactory gameFactory;
    FlightBoard flightBoard;
    Deck deck;

    public GameModel(Level chosenLevel, Map<Integer, Colors> chosenColors) {
        flightBoard = gameFactory.createFlightBoard();
        deck = gameFactory.createDeck();
    }

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
}
