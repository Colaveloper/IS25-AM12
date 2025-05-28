package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Observer;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientModel implements ModelObservable {
    
    private final Map<UUID, Lobby> activeLobbies = new HashMap<>();

    private Player clientPlayer = null;
    private ObservableProperty<Game> game = new ObservableProperty<>(null);
    private final Set<Player> players = new HashSet<>();
    private final Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private final ObservableProperty<MetaState> clientState = new ObservableProperty<>(MetaState.REGISTER);

    private final List<Observer> observers = new ArrayList<>();

    private Map<Player, Integer> finalScores;

    public ClientModel() {

    }

    // Locks
    private final Object playersLock = new Object();
    private final Object gameLock = new Object();
    private final Object observersLock = new Object();

    //region Setup methods

    public void setPlayer(Player player) {
        synchronized (playersLock) {
            this.clientPlayer = player;
        }
    }

    public void createGame(Level level, int playersN) {
        synchronized (gameLock) {
            game.setValue(new Game(level, playersN));
        }
    }

    public void addPlayer(Player player, GameColor color) {
        synchronized (playersLock) {
            synchronized (gameLock) {
                players.add(player);
                player.setShipBoard(game.getValue().addShipBoard(color));
                shipToPlayer.put(player.getShipBoard(), player);
            }
        }
    }

    //endregion

    //region Getters
    public Map<UUID, Lobby> getActiveLobbies() {
        synchronized (gameLock) {
            //sincronizzare su una copia se serve
            return activeLobbies;
        }
    }

    public Player getClientPlayer() {
        synchronized (playersLock) {
            return clientPlayer;
        }
    }

    public Game getGame() {
        synchronized (gameLock) {
            return game.getValue();
        }
    }

    public ObservableProperty<Game> getGameProperty() {
        //sincronizzare se serve
        return game;
    }

    public Set<Player> getPlayers() {
        synchronized (playersLock) {
            //sincronizzare su una copia se serve
            return players;
        }
    }

    public Player getPlayerByShip(ShipBoard shipBoard) {
        synchronized (playersLock) {
            return shipToPlayer.get(shipBoard);
        }
    }

    public Map<ShipBoard, Player> getShipToPlayer() {
        synchronized (playersLock) {
            return new HashMap<>(shipToPlayer);
        }
    }

    public Map<Player, Integer> getFinalScores() {
        //forse anche su player?
        synchronized (gameLock) {
            return finalScores;
        }
    }
    //endregion

    //region Event update methods
    public void notifyCurrentState(GameState gameState) {
        synchronized (gameLock) {
            game.getValue().setCurrentState(gameState);
        }
        System.out.println("Updated to state: " + gameState.getClass().getSimpleName());
    }

    private GameState safeGetCurrentState() {
        synchronized (gameLock) {
            return game.getValue().getCurrentState();
        }
    }

    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        safeGetCurrentState().notifyRequestRandComponent(shipBoard, component);
    }

    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        safeGetCurrentState().notifyRequestComponent(shipBoard, component);
    }

    public void notifyRejectComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyRejectComponent(shipBoard);
    }

    public void notifyStashComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyStashComponent(shipBoard);
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        safeGetCurrentState().notifyGrabStashedComponent(shipBoard, index);
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {
        safeGetCurrentState().notifyPlaceComponent(shipBoard, point, orientation);
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        safeGetCurrentState().notifyFlipHourglass(shipBoard);
    }

    public void notifyHourglassEnd() {
        safeGetCurrentState().notifyHourglassEnd();
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        safeGetCurrentState().notifyFlightBoardPosition(shipBoard, position);
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        safeGetCurrentState().notifyPeekForecast(shipBoard, deckIndex);
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        safeGetCurrentState().setForecastDeck(adventureCards);
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        safeGetCurrentState().notifyReleaseForecast(shipBoard);
    }

    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyRemoveComponent(shipBoard, point);
    }

    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        safeGetCurrentState().notifyChooseShipPiece(shipBoard, pieceIndex);
    }

    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        safeGetCurrentState().notifyShipNotConnected(shipBoard, shipPieces);
    }

    public void notifyShipValidated(ShipBoard shipBoard) {
        safeGetCurrentState().notifyShipValidated(shipBoard);
    }

    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        safeGetCurrentState().notifyInitializeCabin(shipBoard, point, crewType);
    }

    public void notifyDrawCard(AdventureCard adventureCard) {
        safeGetCurrentState().notifyDrawCard(adventureCard);
    }

    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyActivateComponent(shipBoard, point);
    }

    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyLoseCrew(shipBoard, point);
    }

    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        safeGetCurrentState().notifyGrabReward(shipBoard, rewardGrabbed);
    }

    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        safeGetCurrentState().notifyPlaceGoods(shipBoard, point, goodsType);
    }

    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        safeGetCurrentState().notifyRemoveGoods(shipBoard, point, goodsType);
    }

    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyUseBattery(shipBoard,point);
    }

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        safeGetCurrentState().notifyChoosePlanet(shipBoard, choice);
    }

    public void notifyGiveUp(ShipBoard shipBoard) {
        safeGetCurrentState().notifyGiveUp(shipBoard);
    }

    public void setFinalScores(Map<Player, Integer> finalScores) {
        synchronized (gameLock) {
            this.finalScores = finalScores;
        }
    }
    //endregion

    public ShipBoard getMyShip() {
        synchronized (playersLock) {
            return clientPlayer.getShipBoard();
        }
    }

    public void notifyObservers() {
        List<Observer> copy;
        synchronized (observersLock) {
            copy = new ArrayList<>(observers);
        }
        for (Observer o : copy) {
            o.onNotified();
        }
    }

    public ObservableProperty<MetaState> getMetaState() {
        //non sicuro se lockare
        return clientState;
    }

    public void setMetaState(MetaState metaState) {
        this.clientState.setValue(metaState);
    }

    @Override
    public void addObserver(Observer o) {
        synchronized (observersLock) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        synchronized (observersLock) {
            observers.remove(o);
        }
    }
}
