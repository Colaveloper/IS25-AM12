package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.ModelObserver;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClientModel {

    private final List<ModelObserver> observers = new ArrayList<>();
    
    private final Map<UUID, Lobby> activeLobbies = new HashMap<>();

    private Player clientPlayer = null;
    private Game game = null;
    private final Set<Player> players = new HashSet<>();
    private final Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    private MetaState metaState = MetaState.REGISTER;

    private Map<Player, Integer> finalScores;

    // Locks
    private final Object playersLock = new Object();
    private final Object gameLock = new Object();
    private final Object observersLock = new Object();

    //region Observer methods
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }
    //endregion

    //region Setup methods

    public void setPlayer(Player player) {
        synchronized (playersLock) {
            this.clientPlayer = player;
        }
    }

    public void createGame(Level level) {
        synchronized (gameLock) {
            game = new Game(level);
            game.setObservers(observers);
        }
    }

    public void notifyNewLobby(Lobby lobby) {
        activeLobbies.put(lobby.getId(), lobby);
        observers.forEach(o -> o.notifyNewLobby(lobby));
    }

    public void notifyRemoveLobby(UUID uuid) {
        activeLobbies.remove(uuid);
        observers.forEach(o -> o.notifyRemoveLobby(uuid));
    }

    public void addPlayer(Player player, GameColor color) {
        synchronized (playersLock) {
            synchronized (gameLock) {
                players.add(player);
                player.setShipBoard(game.addShipBoard(color));
                shipToPlayer.put(player.getShipBoard(), player);
                //TODO: fix myShipLogic to not be included in the states
                if (getClientPlayer().equals(player)) {
                    game.setMyShip(player.getShipBoard());
                }
            }
        }
    }

    public void activateCheats(int input) {
        CheatCodes.activateCheats(input);
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
            return game;
        }
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
            game.setCurrentState(gameState);
        }
        observers.forEach(modelObserver -> modelObserver.notifyCurrentState(gameState));
    }

    private GameState safeGetCurrentState() {
        synchronized (gameLock) {
            return game.getCurrentState();
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

    public void notifyGrabPlacedComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyGrabPlacedComponent(shipBoard);
    }

    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        safeGetCurrentState().notifyGrabStashedComponent(shipBoard, index);
    }

    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        safeGetCurrentState().notifyPlaceComponent(shipBoard, point, orientation);
    }

    public void notifyFlipHourglass(ShipBoard shipBoard) {
        safeGetCurrentState().notifyFlipHourglass(shipBoard);
    }

    public void notifyHourglassEnd() {
        safeGetCurrentState().notifyHourglassEnd();
    }

    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        safeGetCurrentState().notifyFlightBoardPosition(shipBoard, position, shipBoard == getMyShip());
    }

    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        safeGetCurrentState().notifyPeekForecast(shipBoard, deckIndex);
    }

    public void setForecastDeck(List<AdventureCard> adventureCards) {
        safeGetCurrentState().setForecastDeck(adventureCards);
    }

    public void notifyReleaseForecast(ShipBoard shipBoard) {
        safeGetCurrentState().notifyReleaseForecast(shipBoard, shipBoard == getMyShip());
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

    public void notifyGrabCredits(ShipBoard shipBoard, int credits) {
        safeGetCurrentState().notifyGrabReward(shipBoard, credits);
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

    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        safeGetCurrentState().notifyChoosePlanet(shipBoard, choice, nextShipBoard);
    }

    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        safeGetCurrentState().notifyCurrentPlayerUpdate(shipBoard);
    }

    public void notifyGiveUpMessage(ShipBoard shipBoard) {
        safeGetCurrentState().notifyGiveUp(shipBoard);
    }

    public void notifySurrenderShip(Set<ShipBoard> shipBoards) {
        synchronized (gameLock) {
            game.setGivenUpShips(shipBoards);
        }
        safeGetCurrentState().notifySurrenderShip(shipBoards);
    }

    public void notifySurrenderRequest(Player player) {
        safeGetCurrentState().notifySurrenderRequest(player);
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

    public MetaState getMetaState() {
        return metaState;
    }

    public void setMetaState(MetaState metaState) {
        this.metaState = metaState;
        observers.forEach(observer -> observer.notifyMetaState(metaState));
    }
}
