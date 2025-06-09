package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.EndGameState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Game {
    private final Object lock;

    private final GameFactory gameFactory;
    private final Set<ShipBoard> shipBoards = new HashSet<>();
    private FlightBoard flightBoard;
    private final Set<ShipBoard> givenUpShips = new HashSet<>();
    private Deck deck;
    private GameState currentState;
    private Map<ShipBoard, Integer> finalScores;
    Level level;

    private GameEventListener eventListener;

    public Game(Level level, Object lock) {
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
        this.lock = lock;
    }

    @VisibleForTesting
    public Game(Level level) {
        this(level, new Object());
    }

    /**
     * Adds shipboard of the given color to the game
     * @param color the color of the added shipboard
     * @return the added shipboard
     */
    public ShipBoard addShipBoard(GameColor color) {
        ShipBoard shipBoard = gameFactory.createShipBoard(color);
        shipBoard.setGameEventListener(eventListener);
        shipBoards.add(shipBoard);
        return shipBoard;
    }

    /**
     * Instantiates the game's flightBoard and Deck and sets the
     * current state of the game to shipbuilding
     * @throws IOException if an error occurs when trying to load the deck's
     * cards from disk
     */
    public void start() throws IOException{
        this.flightBoard = gameFactory.createFlightBoard(shipBoards.size());
        this.flightBoard.setGameEventListener(eventListener);
        this.deck = gameFactory.createDeck(this);
        setCurrentState(gameFactory.createFirstGameState());
    }

    /**
     * @return the game's factory
     */
    @VisibleForTesting
    public GameFactory getGameFactory() {
        return gameFactory;
    }

    /**
     * Sets the game's current state to the given state
     * @param state the {@code GameState} to be set
     */
    public void setCurrentState(GameState state) {
        this.currentState = state;
        state.setGame(this);
    }

    /**
     * @return the game's current state
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * @return the game's deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return the game's flight board
     */
    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    /**
     * @return a {@link Set} containing all the game's ship boards
     */
    public Set<ShipBoard> getShipBoards() {
        return shipBoards;
    }

    /**
     * @return the game's level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Adds ships that have no crew or that have been lapped
     * to the set of given up ships and removes them from the
     * flightboard
     * */
    public void forceShipsToGiveUp(){
        if(level != Level.TEST){ // TODO: avoid predicating directly on the instances of Level
            // if ship has no crew -> force give up
            givenUpShips.addAll(
                    shipBoards.stream()
                            .filter(s -> s.getCrewSize() == 0)
                            .collect(Collectors.toSet())
            );

            // if you get lapped -> also force give up
            givenUpShips.addAll(flightBoard.getLappedShips());
            flightBoard.removeShips(givenUpShips);
        }
    }

    /**
     * Adds a shipboard to the set of given up ships and
     * removes that ship from the flightboard
     * @param ship the ship to place in the set of given up ships
     * */
    public void forceShipToGiveUp(ShipBoard ship){
        givenUpShips.add(ship);
    }

    /**
     * @return the game's set of given up ships*/
    public Set<ShipBoard> getGivenUpShips(){return givenUpShips;}

    /**
     * Assigns ship rewards to be used in the final score and
     * changes game state to the end game state*/
    public void endGame(){
        assignShipRewards();
        setCurrentState(new EndGameState(finalScores));
    }

    /**
     * Sets game state to the end game state if there are no
     * more ships playing*/
    public void endGameIfAllShipsHaveGivenUp(){
        if(givenUpShips.size() == shipBoards.size()){
            endGame();
        }
    }

    /**
     * @return the game's final score*/
    public Map<ShipBoard, Integer> getFinalScores(){
        return finalScores;
    }

    private void assignShipRewards(){
        // Best-looking ship reward
        int minExposedConnectors = shipBoards.stream()// who gave up does not count!
                .mapToInt(ShipBoard::getExposedConnectorsNumber)
                .min()
                .orElse(0); // no ship on board

        shipBoards.forEach(s ->
                finalScores.merge(s, s.getExposedConnectorsNumber() == minExposedConnectors ? 2 : 0, Integer::sum)
        );

        // Finish order reward
        shipBoards.forEach(s ->
                finalScores.merge(s, 4 - flightBoard.getOrderedShips().indexOf(s), Integer::sum)
        );

        // Credits reward (minus the losses)
        shipBoards.forEach(s ->
                finalScores.merge(s, s.getCredits() - s.getLosses(), Integer::sum)
        );

        // Goods reward
        shipBoards.forEach(s -> {
            if (shipBoards.contains(s)) {
                finalScores.merge(s, s.getGoodsValue(), Integer::sum);
            } else {
                // given up ships receive 1/2 reward
                finalScores.merge(s, (s.getGoodsValue()+1)/2, Integer::sum);
            }
        });
    }

    @VisibleForTesting
    public void setFlightBoard(FlightBoard flightBoard){
        this.flightBoard = flightBoard;
    }
    @VisibleForTesting
    public void setDeck(Deck deck){
        this.deck = deck;
    }

    public void setEventListener(GameEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameEventListener getEventListener() {
        return eventListener;
    }

    public Object getLock() {
        return lock;
    }

    public void requestRandComponent(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.requestRandComponent(shipBoard);
        }
    }

    public void requestComponent(ShipBoard shipBoard, int componentID) {
        synchronized (lock) {
            currentState.requestComponent(shipBoard, componentID);
        }
    }

    public void rejectComponent(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.rejectComponent(shipBoard);
        }
    }

    public void stashComponent(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.stashComponent(shipBoard);
        }
    }

    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        synchronized (lock) {
            currentState.grabStashedComponent(shipBoard, index);
        }
    }

    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        synchronized (lock) {
            currentState.placeComponent(shipBoard, point, orientation);
        }
    }

    public void flipHourglass(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.flipHourglass(shipBoard);
        }
    }

    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        synchronized (lock) {
            currentState.placeShipOnFlightBoard(shipBoard, startingPosition);
        }
    }

    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        synchronized (lock) {
            currentState.acquireForecast(shipBoard, deckIndex);
        }
    }

    public void releaseForecast(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.releaseForecast(shipBoard);
        }
    }

    public void removeComponent(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            currentState.removeComponent(shipBoard, point);
        }
    }

    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        synchronized (lock) {
            currentState.chooseShipPiece(shipBoard, pieceIndex);
        }
    }

    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        synchronized (lock) {
            currentState.initializeCabin(shipBoard, point, crewType);
        }
    }

    public void activateComponent(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            currentState.activateComponent(shipBoard, point);
        }
    }

    public void loseCrew(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            currentState.loseCrew(shipBoard, point);
        }
    }

    public void grabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        synchronized (lock) {
            currentState.grabReward(shipBoard);
        }
    }

    public void placeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        synchronized (lock) {
            currentState.addGood(shipBoard, point, goodsType);
        }
    }

    public void removeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        synchronized (lock) {
            currentState.removeGood(shipBoard, point, goodsType);
        }
    }

    public void useBattery(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            currentState.spendBatteries(shipBoard, point, 1);
        }
    }

    public void choosePlanet(ShipBoard shipBoard, int choice) {
        synchronized (lock) {
            currentState.choosePlanet(shipBoard, choice);
        }
    }

    public void giveUp(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.giveUp(shipBoard);
        }
    }

    public void drawCard(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.drawCard(shipBoard);
        }
    }

    public void loseGood(ShipBoard shipBoard, Point point) {
        synchronized (lock) {
            currentState.loseGood(shipBoard, point);
        }
    }

    public void goNext(ShipBoard shipBoard) {
        synchronized (lock) {
            currentState.goNext(shipBoard);
        }
    }

}
