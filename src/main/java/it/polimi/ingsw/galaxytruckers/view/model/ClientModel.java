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

/**
 * Client-side model for the Galaxy Truckers game.
 * This class manages game state, player information, and ship data on the client side.
 * It implements the Observer pattern to notify view components of model changes.
 * Thread safety is maintained through strategic synchronization.
 */
public class ClientModel {

    /** List of observers that will be notified of model changes */
    private final List<ModelObserver> observers = new ArrayList<>();
    
    /** Map of active lobbies indexed by their UUID */
    private final Map<UUID, Lobby> activeLobbies = new HashMap<>();

    /** The player associated with this client */
    private Player clientPlayer = null;

    /** The current game instance */
    private Game game = null;

    /** Set of all players in the game */
    private final Set<Player> players = new HashSet<>();

    /** Map connecting ship boards to their owning players */
    private final Map<ShipBoard, Player> shipToPlayer = new HashMap<>();

    /** Current meta state of the game (e.g., REGISTER, LOBBY, INGAME) */
    private MetaState metaState = MetaState.REGISTER;

    /** Final scores of players at the end of the game */
    private Map<Player, Integer> finalScores;

    // Locks
    /** Lock for player-related operations */
    private final Object playersLock = new Object();

    /** Lock for game-related operations */
    private final Object gameLock = new Object();

    /** Lock for observer-related operations */
    private final Object observersLock = new Object();

    //region Observer methods
    /**
     * Adds an observer to be notified of model changes.
     *
     * @param observer The model observer to add
     */
    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer so it no longer receives notifications.
     *
     * @param observer The model observer to remove
     */
    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }
    //endregion

    //region Setup methods
    /**
     * Sets the player associated with this client.
     *
     * @param player The client player
     */
    public void setPlayer(Player player) {
        synchronized (playersLock) {
            this.clientPlayer = player;
        }
    }

    /**
     * Creates a new game with the specified difficulty level.
     *
     * @param level The difficulty level of the game
     */
    public void createGame(Level level) {
        synchronized (gameLock) {
            game = new Game(level);
            game.setObservers(observers);
        }
    }

    /**
     * Notifies observers about a new lobby and adds it to active lobbies.
     *
     * @param lobby The new lobby to add
     */
    public void notifyNewLobby(Lobby lobby) {
        activeLobbies.put(lobby.getId(), lobby);
        observers.forEach(o -> o.notifyNewLobby(lobby));
    }

    /**
     * Removes a lobby and notifies observers.
     *
     * @param uuid The UUID of the lobby to remove
     */
    public void notifyRemoveLobby(UUID uuid) {
        activeLobbies.remove(uuid);
        observers.forEach(o -> o.notifyRemoveLobby(uuid));
    }

    /**
     * Adds a player to the game with the specified color.
     * If the player is the client player, sets the player's ship as the "my ship" reference.
     *
     * @param player The player to add
     * @param color The color for the player's ship
     */
    public void addPlayer(Player player, GameColor color) {
        synchronized (playersLock) {
            synchronized (gameLock) {
                if (players.add(player)) {
                    player.setShipBoard(game.addShipBoard(color));
                    shipToPlayer.put(player.getShipBoard(), player);
                    if (getClientPlayer().equals(player)) {
                        game.setMyShip(player.getShipBoard());
                    }
                    observers.forEach(observer -> observer.notifyPlayerJoin(player));
                }
            }
        }
    }

    /**
     * Activates cheat codes for testing purposes.
     *
     * @param input The cheat code input: 2 for first to build and 3 for second to build phases
     */
    public void activateCheats(int input) {
        CheatCodes.activateCheats(input);
    }
    //endregion

    //region Getters
    /**
     * Gets the map of active lobbies.
     *
     * @return Map of active lobbies indexed by UUID
     */
    public Map<UUID, Lobby> getActiveLobbies() {
        synchronized (gameLock) {
            //sincronizzare su una copia se serve
            return activeLobbies;
        }
    }

    /**
     * Gets the player associated with this client.
     *
     * @return The client player
     */
    public Player getClientPlayer() {
        synchronized (playersLock) {
            return clientPlayer;
        }
    }

    /**
     * Gets the current game instance.
     *
     * @return The current game
     */
    public Game getGame() {
        synchronized (gameLock) {
            return game;
        }
    }

    /**
     * Gets the set of all players in the game.
     *
     * @return Set of all players
     */
    public Set<Player> getPlayers() {
        synchronized (playersLock) {
            return new HashSet<>(players);
        }
    }

    /**
     * Gets the player owning the specified ship board.
     *
     * @param shipBoard The ship board to look up
     * @return The player who owns the ship board
     */
    public Player getPlayerByShip(ShipBoard shipBoard) {
        synchronized (playersLock) {
            return shipToPlayer.get(shipBoard);
        }
    }

    /**
     * Gets a copy of the map connecting ship boards to players.
     *
     * @return Map of ship boards to players
     */
    public Map<ShipBoard, Player> getShipToPlayer() {
        synchronized (playersLock) {
            return new HashMap<>(shipToPlayer);
        }
    }

    /**
     * Gets the final scores of players.
     *
     * @return Map of players to their final scores
     */
    public Map<Player, Integer> getFinalScores() {
        synchronized (gameLock) {
            return finalScores;
        }
    }
    //endregion

    //region Event update methods
    /**
     * Updates the current game state and notifies observers.
     *
     * @param gameState The new game state
     */
    public void notifyCurrentState(GameState gameState) {
        synchronized (gameLock) {
            game.setCurrentState(gameState);
        }
        metaState = MetaState.INGAME;
        observers.forEach(modelObserver -> modelObserver.notifyCurrentState(gameState));
    }

    /**
     * Safely gets the current game state with proper synchronization.
     *
     * @return The current game state
     */
    private GameState safeGetCurrentState() {
        synchronized (gameLock) {
            return game.getCurrentState();
        }
    }

    /**
     * Notifies that a random component has been requested.
     *
     * @param shipBoard The ship board requesting the component
     * @param component The randomly selected component
     */
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        safeGetCurrentState().notifyRequestRandComponent(shipBoard, component);
    }

    /**
     * Notifies that a specific component has been requested.
     *
     * @param shipBoard The ship board requesting the component
     * @param component The requested component
     */
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        safeGetCurrentState().notifyRequestComponent(shipBoard, component);
    }

    /**
     * Notifies that a component has been rejected.
     *
     * @param shipBoard The ship board where the component was rejected
     */
    public void notifyRejectComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyRejectComponent(shipBoard);
    }

    /**
     * Notifies that a component has been stashed.
     *
     * @param shipBoard The ship board where the component was stashed
     */
    public void notifyStashComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyStashComponent(shipBoard);
    }

    /**
     * Notifies that a placed component has been grabbed.
     *
     * @param shipBoard The ship board where the component was grabbed
     */
    public void notifyGrabPlacedComponent(ShipBoard shipBoard) {
        safeGetCurrentState().notifyGrabPlacedComponent(shipBoard);
    }

    /**
     * Notifies that a stashed component has been grabbed.
     *
     * @param shipBoard The ship board where the component was grabbed
     * @param index The index of the grabbed component
     */
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index) {
        safeGetCurrentState().notifyGrabStashedComponent(shipBoard, index);
    }

    /**
     * Notifies that a component has been placed on the ship.
     *
     * @param shipBoard The ship board where the component was placed
     * @param point The coordinates where the component was placed
     * @param orientation The orientation of the placed component
     */
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        safeGetCurrentState().notifyPlaceComponent(shipBoard, point, orientation);
    }

    /**
     * Notifies that the hourglass has been flipped.
     *
     * @param shipBoard The ship board where the hourglass was flipped
     */
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        safeGetCurrentState().notifyFlipHourglass(shipBoard);
    }

    /**
     * Notifies that the hourglass has ended.
     */
    public void notifyHourglassEnd() {
        safeGetCurrentState().notifyHourglassEnd();
    }

    /**
     * Notifies the position of the ship on the flight board.
     *
     * @param shipBoard The ship board being updated
     * @param position The new position on the flight board
     */
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        safeGetCurrentState().notifyFlightBoardPosition(shipBoard, position, shipBoard == getMyShip());
    }

    /**
     * Notifies that the forecast has been peeked at.
     *
     * @param shipBoard The ship board where the forecast was peeked
     * @param deckIndex The index of the deck that was peeked at
     */
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        safeGetCurrentState().notifyPeekForecast(shipBoard, deckIndex);
    }

    /**
     * Sets the forecast deck with a new list of adventure cards.
     *
     * @param adventureCards The new list of adventure cards for the forecast
     */
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        safeGetCurrentState().setForecastDeck(adventureCards);
    }

    /**
     * Notifies that the forecast has been released.
     *
     * @param shipBoard The ship board releasing the forecast
     */
    public void notifyReleaseForecast(ShipBoard shipBoard) {
        safeGetCurrentState().notifyReleaseForecast(shipBoard, shipBoard == getMyShip());
    }

    /**
     * Notifies that a component has been removed from the ship.
     *
     * @param shipBoard The ship board where the component was removed
     * @param point The coordinates of the removed component
     */
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyRemoveComponent(shipBoard, point);
    }

    /**
     * Notifies that a ship piece has been chosen.
     *
     * @param shipBoard The ship board where the ship piece was chosen
     * @param pieceIndex The index of the chosen ship piece
     */
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex) {
        safeGetCurrentState().notifyChooseShipPiece(shipBoard, pieceIndex);
    }

    /**
     * Notifies that a ship is not connected.
     *
     * @param shipBoard The ship board that is not connected
     * @param shipPieces The disconnected ship pieces
     */
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        safeGetCurrentState().notifyShipNotConnected(shipBoard, shipPieces);
    }

    /**
     * Notifies that a ship has been validated.
     *
     * @param shipBoard The ship board that has been validated
     */
    public void notifyShipValidated(ShipBoard shipBoard) {
        safeGetCurrentState().notifyShipValidated(shipBoard);
    }

    /**
     * Notifies that the cabin has been initialized.
     *
     * @param shipBoard The ship board where the cabin was initialized
     * @param point The coordinates of the initialized cabin
     * @param crewType The type of crew assigned to the cabin
     */
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType) {
        safeGetCurrentState().notifyInitializeCabin(shipBoard, point, crewType);
    }

    /**
     * Notifies that a card has been drawn.
     *
     * @param adventureCard The drawn adventure card
     */
    public void notifyDrawCard(AdventureCard adventureCard) {
        safeGetCurrentState().notifyDrawCard(adventureCard);
    }

    /**
     * Notifies that a component has been activated.
     *
     * @param shipBoard The ship board where the component was activated
     * @param point The coordinates of the activated component
     */
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyActivateComponent(shipBoard, point);
    }

    /**
     * Notifies that crew has been lost.
     *
     * @param shipBoard The ship board where the crew was lost
     * @param point The coordinates of the lost crew
     */
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyLoseCrew(shipBoard, point);
    }

    /**
     * Notifies that credits have been grabbed.
     *
     * @param shipBoard The ship board where the credits were grabbed
     * @param credits The amount of credits grabbed
     */
    public void notifyGrabCredits(ShipBoard shipBoard, int credits) {
        safeGetCurrentState().notifyGrabReward(shipBoard, credits);
    }

    /**
     * Notifies that goods have been placed on the ship.
     *
     * @param shipBoard The ship board where the goods were placed
     * @param point The coordinates where the goods were placed
     * @param goodsType The type of goods that were placed
     */
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        safeGetCurrentState().notifyPlaceGoods(shipBoard, point, goodsType);
    }

    /**
     * Notifies that goods have been removed from the ship.
     *
     * @param shipBoard The ship board where the goods were removed
     * @param point The coordinates of the removed goods
     * @param goodsType The type of goods that were removed
     */
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        safeGetCurrentState().notifyRemoveGoods(shipBoard, point, goodsType);
    }

    /**
     * Notifies that the battery has been used.
     *
     * @param shipBoard The ship board where the battery was used
     * @param point The coordinates of the used battery
     */
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        safeGetCurrentState().notifyUseBattery(shipBoard,point);
    }

    /**
     * Notifies that a planet has been chosen.
     *
     * @param shipBoard The ship board where the planet was chosen
     * @param choice The index of the chosen planet
     * @param nextShipBoard The ship board to move to the chosen planet
     */
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice, ShipBoard nextShipBoard) {
        safeGetCurrentState().notifyChoosePlanet(shipBoard, choice, nextShipBoard);
    }

    /**
     * Notifies that the current player has been updated.
     *
     * @param shipBoard The ship board of the updated current player
     */
    public void notifyCurrentPlayerUpdate(ShipBoard shipBoard) {
        safeGetCurrentState().notifyCurrentPlayerUpdate(shipBoard);
    }

    /**
     * Notifies that a give up message has been sent.
     *
     * @param shipBoard The ship board sending the give up message
     */
    public void notifyGiveUpMessage(ShipBoard shipBoard) {
        safeGetCurrentState().notifyGiveUp(shipBoard);
    }

    /**
     * Notifies that a ship has been surrendered.
     *
     * @param shipBoards The set of ship boards that have been surrendered
     */
    public void notifySurrenderShip(Set<ShipBoard> shipBoards) {
        synchronized (gameLock) {
            game.setGivenUpShips(shipBoards);
        }
        safeGetCurrentState().notifySurrenderShip(shipBoards);
    }

    /**
     * Notifies that a surrender request has been made by a player.
     *
     * @param player The player who made the surrender request
     */
    public void notifySurrenderRequest(Player player) {
        safeGetCurrentState().notifySurrenderRequest(player);
    }

    /**
     * Sets the final scores of players and notifies observers.
     *
     * @param finalScores The map of players to their final scores
     */
    public void setFinalScores(Map<Player, Integer> finalScores) {
        synchronized (gameLock) {
            this.finalScores = finalScores;
        }
    }
    //endregion

    /**
     * Gets the ship board associated with the client player.
     *
     * @return The client player's ship board
     */
    public ShipBoard getMyShip() {
        synchronized (playersLock) {
            return clientPlayer.getShipBoard();
        }
    }

    /**
     * Gets the current meta state of the game.
     *
     * @return The current meta state
     */
    public MetaState getMetaState() {
        return metaState;
    }

    /**
     * Sets the meta state of the game and notifies observers.
     *
     * @param metaState The new meta state
     */
    public void setMetaState(MetaState metaState) {
        this.metaState = metaState;
        observers.forEach(observer -> observer.notifyMetaState(metaState));
    }

    public void clearGame() {
        synchronized (gameLock) {
            game = null;
        }
        synchronized (playersLock) {
            this.players.clear();
            shipToPlayer.clear();
        }
        finalScores = null;
        setMetaState(MetaState.JOINORCREATE);
    }
}
