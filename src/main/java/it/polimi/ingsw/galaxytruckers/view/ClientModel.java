package it.polimi.ingsw.galaxytruckers.view;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.CurrentProjectile;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.GoodsBuffer;
import it.polimi.ingsw.galaxytruckers.view.adventureClient.Planets;
import it.polimi.ingsw.galaxytruckers.view.shipBuildingClient.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.shipBuildingClient.Component;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.beans.property.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientModel {

    // META
    private String currentPlayerNickname;
    private String myNickname;
    private final Set<Point> shipArea;
    private final LinkedHashMap<Colors, Map<Point, Component>> ships;
    private final BiMap<String, Colors> playerToColor;
    private final List<Point> selectablePoints;
    private final Map<Colors, Map<StatType, Integer>> stats;

    // BUILDING
    private final List<Component> revealedComponents;
    private final IntegerProperty coveredComponentN;
    private final Map<Colors, List<Component>> stashedComponents;
    private final Map<Colors, Component> hands; // (former current component)
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
    private final List<List<GoodsType>> planets;
    private int chosenPlanetIndex;
    private final List<GoodsType> goods;

    // PROJECTILES
    private CurrentProjectile currentProjectile;

    // GOODS BUFFER
//    private List<Optional<GoodsType>> goodsBuffer;
    private List<GoodsType> goodsBuffer;

    public ClientModel() {
        shipArea = new HashSet<>();
        playerToColor = HashBiMap.create();
        ships = new LinkedHashMap<>();
        selectablePoints = new ArrayList<>();
        coveredComponentN = new SimpleIntegerProperty();
        revealedComponents = new ArrayList<>();
        stashedComponents = new HashMap<>();
        hands = new HashMap<>();
        colorToPlace = new SimpleMapProperty<>();
        stats = new SimpleMapProperty<>();
        planets = new ArrayList<>();
        goods = new ArrayList<>();
    }

    // SETUP PHASE

    public LinkedHashMap<Colors, Map<Point, Component>> getShipboards() {
        return ships;
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

    public void addRevealedComponent(int revealedComponentId) throws IOException {
        revealedComponents.addLast(new Component(revealedComponentId));
    }

    public void setStashedComponents(String nickname, List<Integer> stashedComponents) throws IOException {
        this.stashedComponents.put(
                playerToColor.get(nickname),
                stashedComponents.stream().map(Component::new).collect(Collectors.toList())
        );
    }

    public void setComponentInHand(String nickname, int componentInHandId) throws IOException {
        hands.put(playerToColor.get(nickname), new Component(componentInHandId));
    }

    public void clearComponentInHand() {
        hands.remove(playerToColor.get(myNickname));
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
    }

            // SHIPBOARD

    public void removeComponent (Point position, String nickname) throws IOException {
        this.ships.get(playerToColor.get(nickname)).remove(position);
    }

    public void setComponent(String nickname, int componentId, int direction, Point position) throws IOException {
        Component component = new Component(componentId);
        component.setDirection(direction);
        this.ships.get(playerToColor.get(nickname)).put(position, component);
    }

    public void setCabinStats(String nickname, Point position, CrewType crewType, int crewSize) throws IOException {
        ships.get(playerToColor.get(nickname)).get(position).setCrewType(crewType);
        ships.get(playerToColor.get(nickname)).get(position).setStat(crewSize);
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
        ships.get(playerToColor.get(nickname)).get(position).setStat(totalBatteries);
    }

    public void setCredits(String nickname, int credits) {
        stats.get(playerToColor.get(nickname)).put(StatType.CREDITS, credits);
    }

    public void setLostComponent(String nickname, int losses) {
        stats.get(playerToColor.get(nickname)).put(StatType.LOSSES, losses);
    }

    // place goods on ship
    public void setGoods(String nickname, Point position, List<GoodsType> goods) throws IOException {
        ships.get(playerToColor.get(nickname)).get(position).setGoods(goods);
    }

    // remove good from the buffer
    public void updateGoodsBuffer(int index) throws IOException {
        goodsBuffer.remove(index);
    }

    // in case of planets the goodBuffer must be set after the choice, this happens once, other update use updateGoodsBuffer
    public void setPlanetGoodBuffer(int planetIndex) {
        chosenPlanetIndex = planetIndex;
//        goodsBuffer = new GoodsBuffer(planets, planetId); // TODO: RESTORE
    }

    public void setProjectile(ProjectileType projectileType, int direction, int roll) {
        currentProjectile = new CurrentProjectile (roll, direction, projectileType);
    }

    public CurrentProjectile getCurrentProjectile() {
        return currentProjectile;
    }

    public void setCurrentCard(int cardId) throws IOException {
        currentCardId = cardId;
//        goodsBuffer = new GoodsBuffer(currentCard); // TODO: RESTORE
    }

    public void rotateCurrentComponentLeft() {
        hands.get(playerToColor.get(myNickname)).rotateLeft();
    }

        // OTHER

    public void setSelectablePoints(List<Point> selectablePoints) {
        for (Map.Entry<Point, Component> e : ships.get(playerToColor.get(myNickname)).entrySet()) {
            e.getValue().isSelectableProperty().set(
                    selectablePoints.contains(e.getKey())
            );
        }
    }

    public String getCurrentLeader() {
        return colorToPlace.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(playerToColor.inverse()::get)
                .orElse(null);
    }

    public List<Point> getSelectablePoints() {
        return selectablePoints;
    }

    public void addPlayer(String nickname) {
        ships.put(playerToColor.get(nickname), new HashMap<>());
    }
}
