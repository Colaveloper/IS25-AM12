package it.polimi.ingsw.galaxytruckers.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.CurrentProjectile;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.GoodsBuffer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.Planets;
import it.polimi.ingsw.galaxytruckers.view.shipBuildingClient.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.beans.property.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ClientModel {

    // META
    private String currentPlayerNickname;
    private String myNickname;
    private final Set<Point> shipArea;
    private final LinkedHashMap<Colors, Map<Point, Integer>> colorToShip;        // need order to be always the same
    private final BiMap<String, Colors> playerToColor;
    private final List<Point> selectablePoints;
    private final Map<StatType, Integer> stats;

    // BUILDING
    private final List<Integer> revealedComponents;
    private final IntegerProperty coveredComponentN;
    private final Map<Colors, List<Integer>> stashedComponents;
    private final Map<Colors, Integer> hands; // (former current component)
    private CliComponent unweldedComponent;

    // FLIGHTBOARD
    private int loopLength;
    private List<Integer>  startingPositionLeft;
    private final MapProperty<Colors, Integer> colorToPlace;


    // FLIGHT
    private int currentCardId;
    private int remainingCards;

    // PLANETS
//    private final List<Optional<String>> landedPlayers;
//    private Optional<List<Map<GoodsType, Integer>>> planets;

    // PROJECTILES
    private int currentProjectileRoll;
    private int currentProjectileDirection;
    private ProjectileType currentProjectileType;

    // GOODS BUFFER
    private List<Optional<GoodsType>> goodsBuffer;

    public ClientModel() {
        shipArea = new HashSet<>();
        playerToColor = HashBiMap.create();
        colorToShip = new LinkedHashMap<>();
        selectablePoints = new ArrayList<>();
        coveredComponentN = new SimpleIntegerProperty();
        revealedComponents = new ArrayList<>();
        stashedComponents = new HashMap<>();
        hands = new HashMap<>();
        colorToPlace = new SimpleMapProperty<>();
        stats = new SimpleMapProperty<>();
    }

    // SETUP PHASE

    public LinkedHashMap<Colors, Map<Point, Integer>> getShipboards() {
        return colorToShip;
    }

    public void setPlayerToPlace(Map<String, Integer> playerToPlace) {
        // translating nicknames to colors
        playerToPlace.forEach((key, value) -> this.colorToPlace.putIfAbsent(
                playerToColor.get(key), value
        ));
    }

    public void setFlightBoard(int loopLength, List<Integer> startingPositions) {
        this.loopLength = loopLength;
        this.startingPositionLeft = startingPositions;
    }

    public void setPlayerColor(String nickname, Colors color) {
        playerToColor.putIfAbsent(nickname, color);
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
        addPlayer(myNickname); // TODO: ??
    }

    public void setShipArea(Set<Point> shipArea) {
        this.shipArea.addAll(shipArea);
    }

    // SHIP BUILDING PHASE

        // COMPONENT BANK

    public void addRevealedComponent(int revealedComponent) throws IOException {
        revealedComponents.addLast(revealedComponent);
    }

    public void setStashedComponents(String nickname, List<Integer> stashedComponents) throws IOException {
        this.stashedComponents.put(playerToColor.get(nickname), stashedComponents);
    }

    public void setComponentInHand(String nickname, int componentInHand) throws IOException {
        hands.put(playerToColor.get(nickname), componentInHand);
    }

    public void clearComponentInHand() {
        hands.remove(playerToColor.get(myNickname));
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
    }

            // SHIPBOARD

    public void removeComponent (Point position, String nickname) throws IOException {
        this.colorToShip.get(playerToColor.get(nickname)).remove(position);
    }

    public void setComponent(String nickname, int componentId, int direction, Point position) throws IOException {
        this.colorToShip.get(playerToColor.get(nickname)).put(position, CliComponent);
                .get(nickname).setComponent(position, direction, componentId);
    }

    public void setCabinStats(String nickname, Point position, CrewType crewType, int crew) throws IOException {
        playerToShip.get(nickname).getComponent(position).setCrewRace(crewType);
        playerToShip.get(nickname).getComponent(position).setStat(crew);
    }

    public CliElement getComponentBank() {
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

    public CliElement getPlanets(){ return planets; }

    public CliElement getGoodsBuffer(){ return goodsBuffer; }

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

    public CliElement getCurrentCard() {
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

    public Colors getColorFromNickname(String nickname) {return playerToColor.get(nickname);}

    public CliElement getFlightBoard() {
        return flightBoard;
    }

    public CliElement getMyShipBoard() {
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
    }

    public List<String> getNicknames() {
        return new ArrayList<>(playerToShip.keySet());
    }

    public boolean existsUnwelded() {
        return existsUnwelded;
    }
}
