package it.polimi.ingsw.galaxytruckers.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientModel {
    private String currentPlayerNickname;
    private String myNickname;

    private final FlightBoard flightBoard;
    private final Map<String, Shipboard> playerToShip;
    private final BiMap<String, Colors> playerToColor;
    private final List<Point> selectablePoints;
    private Planets planets;
    private CurrentProjectile currentProjectile;
    private GoodsBuffer goods;
    private ComponentBank componentBank;

    private AdventureCard currentCard;

    public ClientModel() {
        this.flightBoard = new FlightBoard();
        this.playerToColor = HashBiMap.create();
        this.playerToShip = new HashMap<>();
        this.selectablePoints = new ArrayList<>();
        this.componentBank = new ComponentBank();
    }

        // SETUP PHASE

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

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
        addPlayer(myNickname);
    }

    public void setShipArea(Set<Point> shipArea) {
        for (Shipboard s : playerToShip.values()) {
            s.setShipArea(shipArea);
        }
    }

        // SHIP BUILDING PHASE

            // COMPONENT BANK
    public void addRevealedComponent(int componentId) throws IOException {
        componentBank.addRevealedComponent(componentId);
    }

    public void removeRevealedComponent(int componentId) throws IOException { //TODO: edit bank and shipboard in one go
        componentBank.removeStashedComponent(componentId);
    }

    public void addStashedComponent(int componentId) throws IOException {
        componentBank.stashComponent(componentId);
    }

    public void removeStashedComponent(int componentId) throws IOException {
        componentBank.removeStashedComponent(componentId);
    }

    public void setCurrentComponent(int componentId) throws IOException {
        componentBank.setCurrentComponent(componentId);
    }

    public void clearCurrentComponent() {
        componentBank.clearCurrentComponent();
    }

            // SHIPBOARD
    public void setComponent(String nickname, int componentId, int direction, Point position) throws IOException {
        playerToShip.get(nickname).setComponent(position, direction, componentId);
    }

    public void setCrew(String nickname, Point position, int crew, CrewType crewType) throws IOException {
        playerToShip.get(myNickname).getComponent(position).setCrewRace(crewType);
        playerToShip.get(myNickname).getComponent(position).setStat(crew);
    }

    public Physical getComponentBank() {
        return componentBank;
    }

        // ADVENTURE PHASE

    public void loseBatteries(String nickname, Point position, int batteriesLost) throws IOException {
        playerToShip.get(myNickname).getComponent(position).subtractStat(batteriesLost);//TODO: update other players too?
    }

    public void setCargo(String nickname, Point position, List<GoodsType> goods) throws IOException {
        playerToShip.get(nickname).getComponent(position).setGoods(goods);
    }

    public Physical getPlanets(){ return planets; }

    public Physical getGoodsBuffer(){ return goods; }

    public void setProjectile(ProjectileType projectileType, int direction, int roll) {
        currentProjectile = new CurrentProjectile (projectileType, direction, roll);
    }

    public CurrentProjectile getCurrentProjectile() {
        return currentProjectile;
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

        // OTHER

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

    public String getMyNickname() {
        return myNickname;
    }

    public void setSelectablePoints(List<Point> selectablePoints) {
        playerToShip.get(myNickname).setSelectablePoints(selectablePoints);
        this.selectablePoints.addAll(selectablePoints);
    }

    public String getCurrentLeader() {
        return playerToColor.inverse().get(flightBoard.getCurrentLeaderColor());
    }

    public List<Point> getSelectablePoints() {
        return selectablePoints;
    }

    public void addPlayer(String nickname) {
        playerToShip.put(nickname, new Shipboard());
    }
}
