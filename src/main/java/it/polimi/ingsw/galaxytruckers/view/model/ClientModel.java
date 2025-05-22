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
    private List<Point> selectablePoints;
    private List<Point> selectableBatteries;
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
    private Map<CrewType, List<Point>> unplacedCrew;

    private boolean unweldedComponent;
    // FLIGHTBOARD
    private int loopLength;
    private final ObservableList<Integer> startingPositionLeft;
    private final ObservableMap<FourColors, Integer> colorToPlace;


    // FLIGHT
    private int currentCardId;
    private int remainingCards;
    private int batteryBalance;

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
        coveredComponentN = new SimpleIntegerProperty();
        revealedComponents = new SimpleListProperty<>(FXCollections.observableArrayList());
        selectablePoints = new ArrayList<>();
        forecastDeck = new SimpleListProperty<>(FXCollections.observableArrayList(null, null, null));
        forecastAvailability = new SimpleListProperty<>(FXCollections.observableArrayList(
                new SimpleBooleanProperty(true),
                new SimpleBooleanProperty(true),
                new SimpleBooleanProperty(true)
        ));

        stashedComponents = new HashMap<>();
        startingPositionLeft = FXCollections.observableArrayList();
        hands = new HashMap<>();
        unweldedComponent = false;
        colorToPlace = FXCollections.observableHashMap();
        stats = FXCollections.observableHashMap();
        planets = FXCollections.observableArrayList();
        goods = FXCollections.observableArrayList();
        batteryBalance = 0;
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

    // call after having added all players
    public void setShipArea(Set<Point> shipArea) {

        // fill hands with empty components
        playerToColor.forEach((_, c) -> hands.put(c, new SimpleObjectProperty<>(
                new Component(ComponentType.EMPTY_AREA)
        )));

        for(FourColors c : playerToColor.values()) {
            hands.put(c, new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA)));
            stashedComponents.put(c, List.of(
                    new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA)),
                    new SimpleObjectProperty<>(new Component(ComponentType.EMPTY_AREA))
            ));
        }

        this.shipArea.addAll(shipArea);

        int minX = shipArea.stream().mapToInt(p -> p.x).min().orElse(0);
        int maxX = shipArea.stream().mapToInt(p -> p.x).max().orElse(0);
        int minY = shipArea.stream().mapToInt(p -> p.y).min().orElse(0);
        int maxY = shipArea.stream().mapToInt(p -> p.y).max().orElse(0);

        // Save top-left point
        upLeft = new Point(minX, minY);

        this.ships.forEach((c, s)-> {
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

    public void setStashedComponents(String nickname, List<Integer> stashedComponents) throws IOException {
        for(int i = 0; i < this.stashedComponents.size(); i++) {
            if (i < stashedComponents.size()) {
                this.stashedComponents.get(playerToColor.get(nickname)).get(i).set(new Component(stashedComponents.get(i)));
            } else {
                this.stashedComponents.get(playerToColor.get(nickname)).get(i).set(new Component(ComponentType.EMPTY_AREA));
            }
        }
                //stashedComponents.stream().map(Component::new).collect(Collectors.toList())
    }

    public void setComponentInHand(String nickname, int componentInHandId) throws IOException {
        Component component = new Component(componentInHandId);
        hands.get(playerToColor.get(nickname)).set(component);
    }

    public void clearComponentInHand(String nickname) {
        hands.get(playerToColor.get(nickname)).set(new Component(ComponentType.EMPTY_AREA));
    }

    public void setCoveredComponents(int coveredComponentsN) {
        this.coveredComponentN.set(coveredComponentsN);
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

    public void setComponent(String nickname, int componentId, int direction, Point p) throws IOException {
        Component component = new Component(componentId);
        component.setDirection(direction);
        this.ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).set(component);
        if(nickname.equals(myNickname)) {
            setUnwelded(true);
        }
        clearComponentInHand(nickname);
//        lastPosition = position;
    }

    public Component getComponent(String nickname, Point p) {
        return ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get();
    }

    public void setCabinStats(String nickname, Point p, CrewType crewType, int crewSize) throws IOException {
        if(nickname.equals(myNickname)) {
            unplacedCrew.remove(crewType);
            resetAllSelections(myNickname);
        }
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setCrewType(crewType);
        ships.get(playerToColor.get(nickname)).get(p.y-upLeft.y).get(p.x- upLeft.x).get().setStat(crewSize);
    }

    public void setUnplacedCrew(Map<CrewType, List<Point>> unplacedCrew) {
        this.unplacedCrew = unplacedCrew;
    }

    public List<Point> getUnplacedCrewPoints(CrewType crewType) {
        return unplacedCrew.get(crewType);
    }
    public CrewType getUnplacedCrewType() {
        if(unplacedCrew.containsKey(CrewType.PURPLE)){
            return CrewType.PURPLE;
        } else if (unplacedCrew.containsKey(CrewType.BROWN)) {
            return CrewType.BROWN;
        }
        else return CrewType.HUMAN;
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
    public void updateGoodsBuffer(boolean adding, GoodsType type) throws IOException {
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

    public int getCurrentCard() {
        return currentCardId;
    }

    public void rotateCurrentComponentLeft() {
        hands.get(playerToColor.get(myNickname)).get().rotateLeft();
    }

        // OTHER

    public void setSelectablePoints(String nickname, List<Point> selectablePoints) {
        this.selectablePoints = selectablePoints;
        for (Point p : selectablePoints) {
            getComponent(nickname, p).setShipPart(1);
        }

//        List<List<ObjectProperty<Component>>> myShip = ships.get(playerToColor.get(myNickname));
//        for (int i = 0, y = upLeft.y; i < myShip.size(); i++, y++) {
//            for (int j = 0, x = upLeft.x; j<myShip.getFirst().size(); j++, x++) {
//                myShip.get(i).get(j).get().isSelectableProperty().set(
//                        selectablePoints.contains(new Point(x, y))
//                );
//            }
//        }
    }

    public void activateComponent(String nickname, Point p) throws IOException {
        getComponent(nickname, p).setShipPart(3);
        if(isMyNickname(nickname)) {
            if (getComponent(nickname, p).getType() == ComponentType.BATTERY) {
                batteryBalance += 1;
            }
            else{
                batteryBalance -= 1;
            }
        }
    }

    public int getBatteryBalance() {
        return batteryBalance;
    }


    public List<Point> getSelectablePoints() {
        return selectablePoints;
    }

    public void setSelectableBatteries(String nickname, List<Point> selectableBatteries) {
        if (nickname.equals(myNickname)) {
            this.selectableBatteries = selectableBatteries;
        }
        for (Point p : selectablePoints) {
            getComponent(nickname, p).setShipPart(2);
        }
    }

    public List<Point> getSelectableBatteries() {
        return selectableBatteries;
    }

    public void resetAllSelections(String nickname) {
        selectablePoints.clear();
        isValid = true;
        for (List<ObjectProperty<Component>> list : ships.get(playerToColor.get(nickname))){
            for(ObjectProperty<Component> p : list){
                p.get().setShipPart(0);
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

    public boolean existsUnwelded() {
        //return hands.get(playerToColor.get(myNickname)).get().getType() != ComponentType.EMPTY_AREA;
        return unweldedComponent;
    }

    public void setUnwelded(boolean unwelded) {
        unweldedComponent = unwelded;
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
