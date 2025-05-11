package it.polimi.ingsw.galaxytruckers.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CurrentProjectile;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.GoodsBuffer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.Planets;
import it.polimi.ingsw.galaxytruckers.view.shipBuildingClient.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientModel {
    private String currentPlayerNickname;
    private String myNickname;
    private final LinkedHashMap<String, Shipboard> playerToShip;        // need order to be always the same
    private final BiMap<String, Colors> playerToColor;

    private final FlightBoard flightBoard;
    private List<Point> selectablePoints;
    private Planets planets;
    private CurrentProjectile currentProjectile;
    private GoodsBuffer goodsBuffer;
    private final ComponentBank componentBank;
    private final AllShips allShips; //physical to print all ships in a row
    private boolean existsUnwelded; // update this value

    private AdventureCard currentCard;

    public ClientModel() {
        this.flightBoard = new FlightBoard();
        this.playerToColor = HashBiMap.create();
        this.playerToShip = new LinkedHashMap<>();
        this.selectablePoints = new ArrayList<>();
        this.componentBank = new ComponentBank();
        this.allShips = new AllShips(playerToShip);
    }

        // SETUP PHASE

    public LinkedHashMap<String, Shipboard> getShipboards() {
        return playerToShip;
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

    public void setPlayerColor(String nickname, Colors color) {
        playerToColor.putIfAbsent(nickname, color);
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

        // SHIP BUILDING PHASE

            // COMPONENT BANK

    public void setRevealedComponent(List<Integer> components) throws IOException {
        componentBank.setRevealedComponents(components);
    }

    public void setStashedComponents(List<Integer> stashedComponentIds) throws IOException {
        componentBank.setStashedComponents(stashedComponentIds);
    }

    public void setCurrentComponent(int componentId) throws IOException {
        componentBank.setCurrentComponent(componentId);
    }

    public void clearCurrentComponent() {
        componentBank.clearCurrentComponent();
    }

    public void setCoveredComponents(int coveredComponentsN) {
        componentBank.setCoveredComponentN(coveredComponentsN);
    }

            // SHIPBOARD
    public void removeComponent (Point position, String nickname) throws IOException {
        playerToShip.get(nickname).removeComponent(position);
    }

    public void setComponent(String nickname, int componentId, int direction, Point position) throws IOException {
        playerToShip.get(nickname).setComponent(position, direction, componentId);
    }

    public void setCabinStats(String nickname, Point position, CrewType crewType, int crew) throws IOException {
        playerToShip.get(nickname).getComponent(position).setCrewRace(crewType);
        playerToShip.get(nickname).getComponent(position).setStat(crew);
    }

    public Physical getComponentBank() {
        return componentBank;
    }

        // ADVENTURE PHASE
    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public boolean isMyTurn() {
        return currentPlayerNickname != null && currentPlayerNickname.equals(myNickname);
    }

    public boolean isMyNickname(String nickname) {
        return myNickname.equals(nickname);
    }

    public void setBatteries(String nickname, Point position, int totalBatteries) throws IOException {
        playerToShip.get(nickname).getComponent(position).setStat(totalBatteries);
    }

    public void setCredits(String nickname, int creditsToAdd) {
        playerToShip.get(nickname).setCredits(creditsToAdd);
    }

    public int getCredits(String nickname) {
        return playerToShip.get(nickname).getCredits();
    }

    public void setLostComponent(String nickname, int losses) {
        playerToShip.get(nickname).setLostComponent(losses);
    }

    public int getLostComponents(String nickname) {
        return playerToShip.get(nickname).getLostComponents();
    }

    // place goods on ship
    public void setGoods(String nickname, Point position, List<GoodsType> goods) throws IOException {
        playerToShip.get(nickname).getComponent(position).setGoods(goods);
    }

    public Physical getPlanets(){ return planets; }

    public Physical getGoodsBuffer(){ return goodsBuffer; }

    // remove good from the buffer
    public void updateGoodsBuffer(int index) throws IOException {
        goodsBuffer.takeGood(index);
    }

    // in case of planets the goodBuffer must be set after the choice, this happens once, other update use updateGoodsBuffer
    public void setPlanetGoodBuffer(int planetId) {
        planets = new Planets(currentCard);
        goodsBuffer = new GoodsBuffer(planets, planetId);
    }

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
        currentCard = new AdventureCard(cardId);
        goodsBuffer = new GoodsBuffer(currentCard);
    }

    public void rotateCurrentComponentLeft() {
        componentBank.rotateCurrentComponentLeft();
    }

    public String getCardName() {
        return currentCard.getCardName();
    }

        // OTHER

    public Physical getAllShips() {
        return allShips;
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
        this.selectablePoints.clear();
        this.selectablePoints.addAll(selectablePoints);
    }

    public String getCurrentLeader() {
        return playerToColor.inverse().get(flightBoard.getCurrentLeaderColor());
    }

    public List<Point> getSelectablePoints() {
        return selectablePoints;
    }

    public void addPlayer(String nickname) {
        playerToShip.putIfAbsent(nickname, new Shipboard());
        allShips.addPlayer(playerToShip.get(nickname)); // to add the listener
    }

    public List<String> getNicknames() {
        return new ArrayList<>(playerToShip.keySet());
    }

    public boolean existsUnwelded() {
        return existsUnwelded;
    }
}
