package it.polimi.ingsw.galaxytruckers.view.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.cli.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
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
    private final LinkedHashMap<Colors, List<List<ObjectProperty<Component>>>> ships;
    private final BiMap<String, Colors> playerToColor;
    private final List<Point> selectablePoints;
    private final Map<Colors, Map<StatType, Integer>> stats;

    // BUILDING
    private Point upLeft; // the upper-left point of the ship-area
    private final List<Component> revealedComponents;
    private final IntegerProperty coveredComponentN;
    private final Map<Colors, List<Component>> stashedComponents;
    private final Map<Colors, ObjectProperty<Component>> hands; // (former current component)
    private CliComponent unweldedComponent;

    // FLIGHTBOARD
    private int loopLength;
    private ListProperty<Integer>  startingPositionLeft;
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

    public LinkedHashMap<Colors, List<List<ObjectProperty<Component>>>> getShipboards() {
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
        this.startingPositionLeft.addAll(startingPositions);
    }

    public void setPlayerColor(String nickname, Colors color) {
        playerToColor.putIfAbsent(nickname, color);
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
        addPlayer(myNickname); // TODO: ??
    }

    // call after having added all players
    public void setShipArea(Set<Point> shipArea) {
        int minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        this.ships.forEach((_, s)-> {
            for (int y = minY; y <= maxY; y++) {
                List<ObjectProperty<Component>> row = new ArrayList<>();
                for (int x = minX; x <= maxX; x++) {
                    ObjectProperty<Component> component = new SimpleObjectProperty<>(
                        new Component(
                            shipArea.contains(new Point(x, y))
                                    ? ComponentType.EMPTY_AREA
                                    : ComponentType.EMPTY_SPACE
                        ));
                    row.add(component);
//                    super.registerObservables(component);
//                    component.get().setChangeListener(this);
                }
            }
        });
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
        hands.put(playerToColor.get(nickname), new SimpleObjectProperty<>(new Component(componentInHandId)));
    }

    public void clearComponentInHand() {
        hands.remove(playerToColor.get(myNickname));
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
    }

            // SHIPBOARD

    public void removeComponent (Point p, String nickname) throws IOException {
        this.ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).set(
                new Component(ComponentType.EMPTY_AREA)
        );
    }

    public void setComponent(String nickname, int componentId, int direction, Point p) throws IOException {
        Component component = new Component(componentId);
        component.setDirection(direction);
        this.ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).set(component);
//        lastPosition = position;
    }

    public void setCabinStats(String nickname, Point p, CrewType crewType, int crewSize) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setCrewType(crewType);
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setStat(crewSize);
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

    public void setBatteries(String nickname, Point p, int totalBatteries) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setStat(totalBatteries);
    }

    public void setCredits(String nickname, int credits) {
        stats.get(playerToColor.get(nickname)).put(StatType.CREDITS, credits);
    }

    public void setLostComponent(String nickname, int losses) {
        stats.get(playerToColor.get(nickname)).put(StatType.LOSSES, losses);
    }

    // place goods on ship
    public void setGoods(String nickname, Point p, List<GoodsType> goods) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setGoods(goods);
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
        hands.get(playerToColor.get(myNickname)).get().rotateLeft();
    }

        // OTHER

    public void setSelectablePoints(List<Point> selectablePoints) {
        List<List<ObjectProperty<Component>>> myShip = ships.get(playerToColor.get(myNickname));
        for (int i = 0, y = upLeft.y; i < myShip.size(); i++, y++) {
            for (int j = 0, x = upLeft.x; j<myShip.getFirst().size(); j++, x++) {
                myShip.get(i).get(j).get().isSelectableProperty().set(
                        selectablePoints.contains(new Point(x, y))
                );
            }
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
        ships.put(playerToColor.get(nickname), new ArrayList<>());
    }


    // GETTERS (Javafx properties used only for attributes that change over time)

    public IntegerProperty coveredComponentNProperty() {
        return coveredComponentN;
    }

    public ObjectProperty<Component> currentComponentProperty() {
        return hands.get(playerToColor.get(myNickname));
    }

    public int getLoopLength() {return loopLength;}

    public ListProperty<Integer> startingPositionLeftProperty() {
        return startingPositionLeft;
    }

    public MapProperty<Colors, Integer> colorToPlaceProperty() {
        return colorToPlace;
    }

    public LinkedHashMap<Colors, List<List<ObjectProperty<Component>>>> getShips() {
        return ships;
    }

    public Point getUpLeft() {
        return upLeft;
    }
}
