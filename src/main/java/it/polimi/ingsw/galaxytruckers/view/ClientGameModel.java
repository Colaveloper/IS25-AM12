package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientGameModel {
    private String currentPlayerNickname;
    private String myNickname;

    private final FlightBoard flightBoard;
    private final Map<String, Shipboard> playerToShip;
    private final Map<String, Colors> playerToColor;

    private AdventureCard currentCard;

    public ClientGameModel() {
        this.flightBoard = new FlightBoard();
        this.playerToColor = new HashMap<>();
        this.playerToShip = new HashMap<>();
    }

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public void setPlayerToPlace(Map<String, Integer> playerToPlace) {
        flightBoard.setPlayerToPlace(playerToPlace.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> playerToColor.get(e.getKey()),
                        Map.Entry::getValue)
                ));
    }

    public void setFlightBoard(int loopLength, List<Integer> startingPositions) {
        flightBoard.setLoopLength(loopLength);
        flightBoard.setStartingPositionLeft(startingPositions);
    }

    public void setPlayerColor(String nickname, Colors color) {playerToColor.put(nickname, color);}

    public void setComponent(String nickname, int componentId, int direction, Point position) throws IOException {
        playerToShip.get(nickname).setComponent(position, direction, componentId);
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public Colors getColorFromNickname(String nickname) {return playerToColor.get(nickname);}

    public Physical getFlightBoard() {
        return flightBoard;
    }

    public Physical getMyShipBoard() {
        return playerToShip.get(myNickname);
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
        addPlayer(myNickname);
    }

    public void setShipArea(Set<Point> shipArea) {
        for (Shipboard s : playerToShip.values()) {
            s.setShipArea(shipArea);
        }
    }

    public void setSelectablePoints(List<Point> selectablePoints) {
        playerToShip.get(myNickname).setSelectablePoints(selectablePoints);
    }

    public Physical getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(int cardId) throws IOException {
        this.currentCard = new AdventureCard(cardId);
    }

    public String getCardName() {
        return currentCard.getCardName();
    }

    public void addPlayer(String nickname) {
        playerToShip.put(nickname, new Shipboard());
    }
}
