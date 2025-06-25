package it.polimi.ingsw.galaxytruckers.model;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.factory.GameFactory;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.utils.LockUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class Game implements GameInterface {
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

    private final GameEventListener eventListener;

    private final ExecutorService transitionExecutor = Executors.newSingleThreadExecutor();

    @VisibleForTesting
    private Runnable afterEach = () -> {};

    public Game(Level level, int shipsN, GameEventListener eventListener) {
        this.eventListener = eventListener;
        this.level = level;
        this.gameFactory = GameFactory.getFactory(level);
        this.surrenderPolicy = this.gameFactory.createSurrenderPolicy(eventListener);
        this.scoresRegistry = this.gameFactory.createScoresRegistry();
        this.flightBoard = gameFactory.createFlightBoard(shipsN, eventListener);
        try {
            this.deck = gameFactory.createDeck(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestSnapshot(ShipBoard shipBoard) {
        withStateWriteLock(() -> {
            if (!isGameOver()) {
                getCurrentState().cancelSkip(shipBoard);
                eventListener.requestSnapshot(this, shipBoard);
            }
        });
    }

    //region Setup methods
    /**
     * Adds shipboard of the given color to the game
     *
     * @param color the color of the added shipboard
     * @return the added shipboard
     */
    @Override
    public ShipBoard addShipBoard(GameColor color) {
        ShipBoard shipBoard;
        synchronized (shipBoards) {
            shipBoard = gameFactory.createShipBoard(color, eventListener);
            shipBoards.add(shipBoard);
        }
        return shipBoard;
    }

    /**
     * Starts the game by setting the current state to ShipBuilding
     */
    @Override
    public void start() {
        setCurrentState(gameFactory.createShipBuildingState());
    }

    @Override
    public void skipBuilding() {
        withStateWriteLock(() -> {
            deck.initMasterDeck();
            flightBoard.setup(shipBoards);
            currentState = new DrawCardState();
            currentState.setGame(this);
            for (ShipBoard shipBoard : shipBoards) {
                eventListener.requestSnapshot(this, shipBoard);
            }
        });
    }

    @Override
    public void setStartAdventureCallback(Runnable startAdventureCallback) {
        eventListener.setStartAdventureCallback(startAdventureCallback);
    }

    //endregion

    //region State methods

    public void submitStateTransition(Runnable runnable) {
        transitionExecutor.submit(() -> {
            try {
                withStateWriteLock(runnable);
                afterEach.run();
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
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
            eventListener.notifyGameStateUpdateEvent(this.currentState);
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
        Set<ShipBoard> res;
        synchronized (shipBoards) {
            res = new HashSet<>(shipBoards);
        }
        return res;
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

    public GameEventListener getEventListener() {
        return eventListener;
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
     * Ends the game and computes the final scores if either the deck is empty or
     * all ships have surrendered
     * @return {@code true} if the game was ended, {@code false} otherwise
     */
    public boolean tryEndGame() {
        return withStateWriteLock(() -> {
            if (gameOver) return false;
            if ((surrenderPolicy.isSurrenderEnabled() && surrenderPolicy.getSurrenderedShips().size() == getShipBoards().size()) ||
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
        int minExposedConnectors = shipsInPlay.stream().mapToInt(ShipBoard::getExposedConnectorsNumber).min().orElse(-1);
        shipsInPlay.stream()
                .filter(s -> s.getExposedConnectorsNumber() == minExposedConnectors)
                .forEach(bestLookingShip ->
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


    @Override
    public void skip(ShipBoard shipBoard) {
        withStateReadLock(() -> {
            if (!gameOver) {
                currentState.skip(shipBoard);
            }
        });
    }

    @Override
    public void requestRandComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.requestRandComponent(shipBoard));
    }

    @Override
    public void requestComponent(ShipBoard shipBoard, int componentID) {
        runRequest(() -> currentState.requestComponent(shipBoard, componentID));
    }

    @Override
    public void rejectComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.rejectComponent(shipBoard));
    }

    @Override
    public void stashComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.stashComponent(shipBoard));
    }

    @Override
    public void grabPlacedComponent(ShipBoard shipBoard) {
        runRequest(() -> currentState.grabPlacedComponent(shipBoard));
    }

    @Override
    public void grabStashedComponent(ShipBoard shipBoard, int index) {
        runRequest(() -> currentState.grabStashedComponent(shipBoard, index));
    }

    @Override
    public void placeComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        runRequest(() -> currentState.placeComponent(shipBoard, point, orientation));
    }

    @Override
    public void flipHourglass(ShipBoard shipBoard) {
        runRequest(() -> currentState.flipHourglass(shipBoard));
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
        runRequest(() -> currentState.placeShipOnFlightBoard(shipBoard, startingPosition));
    }

    @Override
    public void placeShipOnFlightBoard(ShipBoard shipBoard) {
        runRequest(() -> currentState.placeShipOnFlightBoard(shipBoard));
    }

    @Override
    public void acquireForecast(ShipBoard shipBoard, int deckIndex) {
        runRequest(() -> currentState.acquireForecast(shipBoard, deckIndex));
    }

    @Override
    public void releaseForecast(ShipBoard shipBoard) {
        runRequest(() -> currentState.releaseForecast(shipBoard));
    }

    @Override
    public void removeComponent(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.removeComponent(shipBoard, point));
    }

    @Override
    public void chooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        runRequest(() -> currentState.chooseShipPiece(shipBoard, pieceIndex));
    }

    @Override
    public void initializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        runRequest(() -> currentState.initializeCabin(shipBoard, point, crewType));
    }

    @Override
    public void activateComponent(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.activateComponent(shipBoard, point));
    }

    @Override
    public void loseCrew(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.loseCrew(shipBoard, point));
    }

    @Override
    public void grabReward(ShipBoard shipBoard) {
        runRequest(() -> currentState.grabReward(shipBoard));
    }

    @Override
    public void placeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        runRequest(() -> currentState.addGood(shipBoard, point, goodsType));
    }

    @Override
    public void removeGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        runRequest(() -> currentState.removeGood(shipBoard, point, goodsType));
    }

    @Override
    public void useBattery(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.spendBatteries(shipBoard, point));
    }

    @Override
    public void choosePlanet(ShipBoard shipBoard, int choice) {
        runRequest(() -> currentState.choosePlanet(shipBoard, choice));
    }

    @Override
    public void giveUp(ShipBoard shipBoard) {
        runRequest(() -> currentState.giveUp(shipBoard));
    }

    @Override
    public void drawCard(ShipBoard shipBoard) {
        runRequest(() -> currentState.drawCard(shipBoard));
    }

    @Override
    public void loseGood(ShipBoard shipBoard, Point point) {
        runRequest(() -> currentState.loseGood(shipBoard, point));
    }

    @Override
    public void goNext(ShipBoard shipBoard) {
        runRequest(() -> currentState.goNext(shipBoard));
    }
    //endregion

    //region Utility methods
    private <T> T withStateReadLock(Supplier<T> method) {
        return LockUtils.withLock(stateLock, method, false);
    }

    private void withStateReadLock(Runnable method) {
        LockUtils.withLock(stateLock, method, false);
    }

    private <T> T withStateWriteLock(Supplier<T> method) {
        return LockUtils.withLock(stateLock, method, true);
    }

    private void withStateWriteLock(Runnable method) {
        LockUtils.withLock(stateLock, method, true);
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

    @VisibleForTesting
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @VisibleForTesting
    public ScoresRegistry getScoresRegistry() {
        return scoresRegistry;
    }

    @VisibleForTesting
    public Map<ShipBoard, Integer> getFinalScores() {
        return finalScores;
    }
    //endregion
}
