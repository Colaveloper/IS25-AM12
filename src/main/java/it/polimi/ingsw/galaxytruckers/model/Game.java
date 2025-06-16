package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Game {
    private final Level level;
    private final GameFactory gameFactory;

    private final Set<ShipBoard> shipBoards = new HashSet<>();
    private final SurrenderPolicy surrenderPolicy;
    private final ScoresRegistry scoresRegistry;
    private volatile FlightBoard flightBoard;
    private volatile Deck deck;

    private final ReentrantReadWriteLock stateLock = new ReentrantReadWriteLock();
    private GameState currentState;
    private boolean gameOver = false;

    private final Map<ShipBoard, Integer> finalScores = new HashMap<>();

    private volatile GameEventListener eventListener;

    private final ExecutorService transitionExecutor = Executors.newSingleThreadExecutor();

    @VisibleForTesting
    private Runnable afterEach = () -> {};

    public Game(Level level, int shipsN) {
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
        this.surrenderPolicy = this.gameFactory.createSurrenderPolicy();
        this.scoresRegistry = this.gameFactory.createScoresRegistry();
        this.flightBoard = gameFactory.createFlightBoard(shipsN);
        try {
            this.deck = gameFactory.createDeck(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //region Setup methods
    /**
     * Adds shipboard of the given color to the game
     *
     * @param color the color of the added shipboard
     * @return the added shipboard
     */
    public ShipBoard addShipBoard(GameColor color) {
        synchronized (shipBoards) {
            ShipBoard shipBoard = gameFactory.createShipBoard(color);
            shipBoards.add(shipBoard);
            return shipBoard;
        }
    }

    /**
     * Sets a {@link GameEventListener} for the game
     * @param eventListener the new {@link GameEventListener}
     */
    public void setEventListener(GameEventListener eventListener) {
        this.eventListener = eventListener;
        shipBoards.forEach(s -> s.setGameEventListener(eventListener));
        surrenderPolicy.setEventListener(eventListener);
        flightBoard.setGameEventListener(eventListener);
    }

    /**
     * Starts the game by setting the current state to ShipBuilding
     */
    public void start() {
        setCurrentState(gameFactory.createShipBuildingState());
    }

    //endregion

    //region State methods

    public void submitStateTransition(Runnable runnable) {
        transitionExecutor.submit(() -> {
            withStateWriteLock(runnable);
            afterEach.run();
        });
    }
    /**
     * Sets the game's current state to the given state
     *
     * @param state the {@code GameState} to be set
     */
    public void setCurrentState(GameState state) {
        withStateWriteLock(() -> {
            this.currentState = state;
            state.setGame(this);
        });
    }

    //endregion

    //region Getters
    /**
     * @return the game's factory
     */
    public GameFactory getGameFactory() {
        return gameFactory;
    }

    /**
     * @return the game's current state
     */
    public GameState getCurrentState() {
        return withStateReadLock(() -> currentState);
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
        synchronized (shipBoards) {
            return new HashSet<>(shipBoards);
        }
    }

    /**
     * @return the game's level
     */
    public Level getLevel() {
        return level;
    }

    public SurrenderPolicy getSurrenderPolicy() {
        return surrenderPolicy;
    }

    public boolean isGameOver() {
        return withStateReadLock(() -> gameOver);
    }

    //endregion

    //region GameEnd methods
    /**
     * Assigns ship rewards to be used in the final score and
     * changes game state to the end game state
     */
    private void endGame() {
        gameOver = true;
        assignShipRewards();
        eventListener.notifyGameEndEvent(finalScores);
    }

    /**
     * Sets game state to the end game state if there are no
     * more ships playing
     */
    public boolean tryEndGame() {
        return withStateWriteLock(() -> {
            if (gameOver) return false;
            if (surrenderPolicy.getSurrenderedShips().size() == getShipBoards().size() ||
                    deck.isEmpty()) {
                endGame();
                return true;
            }
            return false;
        });
    }

    private void assignShipRewards() {
        Set<ShipBoard> shipsInPlay = new HashSet<>(shipBoards);
        shipsInPlay.removeAll(surrenderPolicy.getSurrenderedShips());

        // Best-looking ship reward
        shipsInPlay.stream()
                .min(Comparator.comparingInt(ShipBoard::getExposedConnectorsNumber))
                .ifPresent(bestLookingShip ->
                        finalScores.merge(bestLookingShip, scoresRegistry.getBestLookingShipAward(), Integer::sum));

        // Finish order reward
        List<ShipBoard> orderedShips = flightBoard.getOrderedShips();
        for (int i = 0; i < orderedShips.size(); i++) {
            finalScores.merge(orderedShips.get(i), scoresRegistry.getPositionScores()[i], Integer::sum);
        }

        // Credits reward (minus the losses)
        shipBoards.forEach(s ->
                finalScores.merge(s, s.getCredits() - s.getLosses(), Integer::sum)
        );

        // Goods reward
        shipBoards.forEach(s -> {
            if (shipsInPlay.contains(s)) {
                finalScores.merge(s, s.getGoodsValue(), Integer::sum);
            } else {
                // given up ships receive 1/2 reward
                finalScores.merge(s, (s.getGoodsValue() + 1) / 2, Integer::sum);
            }
        });
    }
    //endregion

    //region Player requests
    public GameEventListener getEventListener() {
        return eventListener;
    }

    public void requestRandComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.requestRandComponent(shipBoard));
    }

    public void requestComponent(ShipBoard shipBoard, int componentID) {
        runRequest(() -> currentState.requestComponent(shipBoard, componentID));
    }

    public void rejectComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.rejectComponent(shipBoard));
    }

    public void stashComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.stashComponent(shipBoard));
    }

    public void grabPlacedComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.grabPlacedComponent(shipBoard));
    }

    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        runRequest(() -> currentState.grabStashedComponent(shipBoard, index));
    }

    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        runRequest(() -> currentState.placeComponent(shipBoard, point, orientation));
    }

    public void flipHourglass(ShipBoard shipBoard) {
        runRequest(() -> currentState.flipHourglass(shipBoard));
    }

    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        runRequest(() -> currentState.placeShipOnFlightBoard(shipBoard, startingPosition));
    }

    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        runRequest(() -> currentState.acquireForecast(shipBoard, deckIndex));
    }

    public void releaseForecast(ShipBoard shipBoard) {
        runRequest(() -> currentState.releaseForecast(shipBoard));
    }

    public void removeComponent(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.removeComponent(shipBoard, point));
    }

    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        runRequest(() -> currentState.chooseShipPiece(shipBoard, pieceIndex));
    }

    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        runRequest(() -> currentState.initializeCabin(shipBoard, point, crewType));
    }

    public void activateComponent(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.activateComponent(shipBoard, point));
    }

    public void loseCrew(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.loseCrew(shipBoard, point));
    }

    public void grabReward(ShipBoard shipBoard) {
        runRequest(() -> currentState.grabReward(shipBoard));
    }

    public void placeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        runRequest(() -> currentState.addGood(shipBoard, point, goodsType));
    }

    public void removeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        runRequest(() -> currentState.removeGood(shipBoard, point, goodsType));
    }

    public void useBattery(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.spendBatteries(shipBoard, point));
    }

    public void choosePlanet(ShipBoard shipBoard, int choice) {
        runRequest(() -> currentState.choosePlanet(shipBoard, choice));
    }

    public void giveUp(ShipBoard shipBoard) {
        runRequest(() -> currentState.giveUp(shipBoard));
    }

    public void drawCard(ShipBoard shipBoard) {
        runRequest(() -> currentState.drawCard(shipBoard));
    }

    public void loseGood(ShipBoard shipBoard, Point point) {
        runRequest(() -> loseGood(shipBoard, point));
    }

    public void goNext(ShipBoard shipBoard) {
        runRequest(() -> currentState.goNext(shipBoard));
    }
    //endregion

    //region Utility methods
    private <T> T withStateReadLock(Supplier<T> method) {
        return withStateLock(method, false);
    }

    private void withStateReadLock(Runnable method) {
        withStateReadLock(() -> {
            method.run();
            return null;
        });
    }

    private <T> T withStateWriteLock(Supplier<T> method) {
        return withStateLock(method, true);
    }

    private void withStateWriteLock(Runnable method) {
        withStateWriteLock(() -> {
            method.run();
            return null;
        });
    }

    private  <T> T withStateLock(Supplier<T> method, boolean write) {
        Lock lock;
        if (write) {
            lock = stateLock.writeLock();
        } else {
            lock = stateLock.readLock();
        }
        lock.lock();
        try {
            return method.get();
        } finally {
            lock.unlock();
        }
    }

    private void runRequest(Runnable method) {
        withStateReadLock(() -> {
            if (gameOver) throw new IllegalStateException("The game is over");
            method.run();
        });
    }
    //endregion

    //region Test methods
    @VisibleForTesting
    public Game(Level level) {
        this(level, 4);
    }

    public Game(GameFactory gameFactory) {
        int shipsN = 4;
        this.level = null;
        this.gameFactory = gameFactory;
        this.surrenderPolicy = this.gameFactory.createSurrenderPolicy();
        this.scoresRegistry = this.gameFactory.createScoresRegistry();
        this.flightBoard = gameFactory.createFlightBoard(shipsN);
        try {
            this.deck = gameFactory.createDeck(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @VisibleForTesting
    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    @VisibleForTesting
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    @VisibleForTesting
    public void setAfterEach(Runnable afterEach) {
        this.afterEach = afterEach;
    }
    //endregion
}
