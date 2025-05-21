package it.polimi.ingsw.galaxytruckers.view.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ComponentType;
import it.polimi.ingsw.galaxytruckers.view.viewEnums.ProjectileType;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ClientModel {

    // META
    private String currentPlayerNickname;
    private String myNickname;
    private final Set<Point> shipArea;
    private final Map<FourColors, List<List<ObjectProperty<Component>>>> ships;
    private final BiMap<String, FourColors> playerToColor;
    private final ObservableList<Point> selectablePoints;
    private final ObservableMap<FourColors, Map<StatType, Integer>> stats;

    // BUILDING
    private Point upLeft; // the upper-left point of the ship-area
    private final ListProperty<Component> revealedComponents;
    private final IntegerProperty coveredComponentN;
    private final Map<FourColors, List<ObjectProperty<Component>>> stashedComponents;
    private final Map<FourColors, ObjectProperty<Component>> hands; // (former current component)
    private final ListProperty<Integer> forecastDeck;
    private final List<BooleanProperty> forecastAvailability;
    private List<Set<Point>> shipPieces;
    private boolean isValid;

    private final Map<FourColors, UnweldedComponent> unweldedComponent;
    // FLIGHTBOARD
    private int loopLength;
    private final ObservableList<Integer> startingPositionLeft;
    private final ObservableMap<FourColors, Integer> colorToPlace;


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
    private ProjectileRecord currentProjectile;

    // GOODS BUFFER
//    private List<Optional<GoodsType>> goodsBuffer;
    private List<GoodsType> goodsBuffer;

    public ClientModel() {
        shipArea = new HashSet<>();
        playerToColor = HashBiMap.create();
        shipPieces = new ArrayList<>();
        ships = new SimpleMapProperty<>(FXCollections.observableMap(new HashMap<>()));
        selectablePoints = FXCollections.observableArrayList();
        coveredComponentN = new SimpleIntegerProperty();
        revealedComponents = new SimpleListProperty<>(FXCollections.observableArrayList());
        forecastDeck = new SimpleListProperty<>(FXCollections.observableArrayList(null, null, null));
        forecastAvailability = new SimpleListProperty<>(FXCollections.observableArrayList(
                new SimpleBooleanProperty(true),
                new SimpleBooleanProperty(true),
                new SimpleBooleanProperty(true)
        ));
        unweldedComponent = new HashMap<>();
        stashedComponents = new HashMap<>();
        startingPositionLeft = FXCollections.observableArrayList();
        hands = new HashMap<>();
        colorToPlace = FXCollections.observableHashMap();
        stats = FXCollections.observableHashMap();
        planets = FXCollections.observableArrayList();
        goods = FXCollections.observableArrayList();

    }

    // SETUP PHASE

    public void setPlayerToPlace(String nickname, int position) {
        // translating nicknames to colors
        this.colorToPlace.putIfAbsent(playerToColor.get(nickname), position);
        this.startingPositionLeft.remove((Integer) position);
    }

    public void setFlightBoard(int loopLength, List<Integer> startingPositions) {
        this.loopLength = loopLength;
        this.startingPositionLeft.addAll(startingPositions);
    }

    public void setPlayerColor(String nickname, FourColors color) {
        playerToColor.putIfAbsent(nickname, color);
        ships.put(playerToColor.get(nickname), new ArrayList<>());
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }

    public String getMyNickname() {
        return myNickname;
    }

    // TODO: rename
    public void setShipArea(Set<Point> shipArea) {

        this.shipArea.addAll(shipArea);

        int minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        playerToColor.forEach((_, c) -> {

            hands.put(c, new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA)));

            unweldedComponent.put(c, new UnweldedComponent(false, false, null));

            stashedComponents.put(c, List.of(
                    new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA)),
                    new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA))
            ));

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
                }
                ships.get(c).add(row);
            }
        });
    }

    // SHIP BUILDING PHASE

        // COMPONENT BANK

    public void addRevealedComponent(int revealedComponentId) throws IOException {
        revealedComponents.addLast(new Component(revealedComponentId));
    }

    public void removeRevealedComponent(int componentId) {
        revealedComponents.removeIf(c -> c.getComponentId() == componentId);
    }

    public void stashComponents(String nickname, List<Integer> stashedComponents) throws IOException {
        setStashedComponents(nickname, stashedComponents);
        if (unweldedComponent.get(playerToColor.get(nickname)).getIsHand()) {
            hands.get(playerToColor.get(nickname)).set(new Component(ComponentType.EMPTY_AREA));
        } else {
            Point q = new Point(unweldedComponent.get(playerToColor.get(nickname)).getPosition());
            ships.get(playerToColor.get(nickname)).get(q.y-upLeft.y).get(q.x-upLeft.x).set(new Component(ComponentType.EMPTY_AREA));
        }

    }

    public void unstashComponents(String nickname, List<Integer> stashedComponents) throws IOException {
        setStashedComponents(nickname, stashedComponents);
    }

    private void setStashedComponents(String nickname, List<Integer> stashedComponents) {
        for(int i = 0; i < this.stashedComponents.size(); i++) {
            if (i < stashedComponents.size()) {
                this.stashedComponents.get(playerToColor.get(nickname)).get(i).set(new Component(stashedComponents.get(i)));
            } else {
                this.stashedComponents.get(playerToColor.get(nickname)).get(i).set(new Component(ComponentType.EMPTY_AREA));
            }
        }
    }


    // used to set the component in hand to a non-empty value
    public void setComponentInHand(String nickname, int componentInHandId) throws IOException {
        Component component = new Component(componentInHandId);
        hands.get(playerToColor.get(nickname)).set(component);
        unweldedComponent.get(playerToColor.get(nickname)).setExists(true);
        unweldedComponent.get(playerToColor.get(nickname)).setIsHand(true);
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
    }

        // CURRENT COMPONENT

    public boolean getExistsUnweldedComponent() {
        return unweldedComponent.get(playerToColor.get(myNickname)).getExists();
    }

    public void setExistsUnweldedComponent(String nickname, boolean existsUnweldedComponent) {
        unweldedComponent.get(playerToColor.get(nickname)).setExists(existsUnweldedComponent);
    }

    public void clearUnwelded(String nickname) {
        if (unweldedComponent.get(playerToColor.get(nickname)).getIsHand()) {
            hands.get(playerToColor.get(nickname)).set(new Component(ComponentType.EMPTY_AREA));
        } else {
            Point q = unweldedComponent.get(playerToColor.get(nickname)).getPosition();
            this.ships.get(playerToColor.get(nickname)).get(q.y-upLeft.y).get(q.x-upLeft.x).set(new Component(ComponentType.EMPTY_AREA));
            if(nickname.equals(myNickname)) {
                selectablePoints.clear();
            }
        }
    }

            // SHIPBOARD

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean shipIsValid() {
        return isValid;
    }

    public void removeComponent (Point p, String nickname) throws IOException {
        this.ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).set(
                new Component(ComponentType.EMPTY_AREA)
        );
    }

    // placing a component on any ship
    public void setComponent(String nickname, int componentId, int direction, Point p) throws IOException {
        Component component = new Component(componentId);
        component.setDirection(direction);
        this.ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x-upLeft.x).set(component);
        // clearing previous position
        // unwelded component surely exists
        if (unweldedComponent.get(playerToColor.get(nickname)).getIsHand()) {
            hands.get(playerToColor.get(nickname)).set(new Component(ComponentType.EMPTY_AREA));
            unweldedComponent.get(playerToColor.get(nickname)).setIsHand(false);
        } else {
            Point q = unweldedComponent.get(playerToColor.get(nickname)).getPosition();
            this.ships.get(playerToColor.get(nickname)).get(q.y-upLeft.y).get(q.x-upLeft.x).set(new Component(ComponentType.EMPTY_AREA));
            if(nickname.equals(myNickname)) {
                selectablePoints.clear();
                selectablePoints.add(p);
            }
        }
        unweldedComponent.get(playerToColor.get(nickname)).setPosition(p);
    }

    public Component getComponent(String nickname, Point p) {
        return ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get();
    }

    public void setCabinStats(String nickname, Point p, CrewType crewType, int crewSize) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setCrewType(crewType);
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setStat(crewSize);
    }

        //  FORECAST

    public void freeForecast(int deckIndex) throws IOException {
        //forecastAvailability.set(deckIndex, true);
        forecastAvailability.get(deckIndex).set(true);
    }

    public void blockForecast(int deckIndex) throws IOException {
        forecastAvailability.get(deckIndex).set(false);
    }

    public void setForecast(List<Integer> cardIds) {
        for(int i = 0; i < forecastDeck.size(); i++) {
            forecastDeck.set(i, cardIds.get(i));
        }
    }

    public ListProperty<Integer> getForecastDeck() {
        return forecastDeck;
    }

    public List<BooleanProperty> getForecastDeckAvailablility() {
        return forecastAvailability;
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


    public void setStat(String nickname, StatType statType, int value) {
        stats.get(playerToColor.get(nickname)).put(statType, value);
    }

    // place goods on ship
    public void setGoods(String nickname, Point p, List<GoodsType> goods) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setGoods(goods);
    }

    // remove good from the buffer
    public void updateGoodsBuffer(GoodsType type) throws IOException {
        goodsBuffer.remove(type);
    }

    // in case of planets the goodBuffer must be set after the choice, this happens once, other update use updateGoodsBuffer
    public void setPlanetGoodBuffer(int planetIndex) {
        chosenPlanetIndex = planetIndex;
//        goodsBuffer = new GoodsBuffer(planets, planetId); // TODO: RESTORE
    }

    public void setProjectile(ProjectileType projectileType, int direction, int roll) {
        currentProjectile = new ProjectileRecord(roll, direction, projectileType);
    }

    public ProjectileRecord getCurrentProjectile() {
        return currentProjectile;
    }

    public void setCurrentCard(int cardId) throws IOException {
        currentCardId = cardId;
//        goodsBuffer = new GoodsBuffer(currentCard); // TODO: RESTORE
    }

    public void rotateCurrentComponentLeft() {
        if (unweldedComponent.get(playerToColor.get(myNickname)).getIsHand()) {
            hands.get(playerToColor.get(myNickname)).get().rotateLeft();
        } else {
            Point p = new Point(unweldedComponent.get(playerToColor.get(myNickname)).getPosition());
            ships.get(playerToColor.get(myNickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().rotateLeft();
        }
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

    public void setBatteriesOnComponent(String nickname, Point p, int totalBatteries) throws IOException {
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setStat(totalBatteries);
    }

    public void setSelectableShipPieces(String nickname, List<Set<Point>> selectableShipPieces) {
        if(isMyNickname(nickname)) {
            shipPieces = selectableShipPieces;
        }
        List<List<ObjectProperty<Component>>> myShip = ships.get(playerToColor.get(nickname));
        for(int i = 0; i < selectableShipPieces.size(); i++) {
            for(Point p : selectableShipPieces.get(i)) {
                myShip.get(p.y - upLeft.y).get(p.x - upLeft.x).get().setShipPart(i + 1);
            }
        }
    }

    public List<Set<Point>> getSelectableShipPieces() {
        return shipPieces;
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

    // GETTERS (Javafx properties used only for attributes that change over time)

    public Set<String> getNicknames() {
        return playerToColor.keySet();
    }

    public IntegerProperty coveredComponentNProperty() {
        return coveredComponentN;
    }

    public ObjectProperty<Component> currentComponentProperty() {
        return hands.get(playerToColor.get(myNickname));
    }

    public int getLoopLength() {return loopLength;}

    public ObservableList<Integer> startingPositionLeftProperty() {
        return startingPositionLeft;
    }

    public ObservableMap<FourColors, Integer> colorToPlaceProperty() {
        return colorToPlace;
    }

    public Map<FourColors, List<List<ObjectProperty<Component>>>> getShips() {
        return ships;
    }

    public Point getUpLeft() {
        return upLeft;
    }

    public boolean isHandEmpty() {
        return hands.get(playerToColor.get(myNickname)).get().getType() != ComponentType.EMPTY_AREA;
    }

    public Map<FourColors, ObjectProperty<Component>> getHand() {
        return hands;
    }

    public Map<FourColors, List<ObjectProperty<Component>>> getStashed() {
        return stashedComponents;
    }

    public ListProperty<Component> revealedComponentsProperty() {
        return revealedComponents;
    }

    public BiMap<String, FourColors> getPlayerToColor() {
        return playerToColor;
    }
}
