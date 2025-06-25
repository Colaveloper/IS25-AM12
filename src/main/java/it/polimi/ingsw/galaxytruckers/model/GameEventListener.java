package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.serverController.utils.ConversionUtils;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class is responsible for handling game events and notifying the controller.
 * It provides methods to request snapshots, notify various game events, and manage game state updates.
 */
public class GameEventListener {
    private final EventListener<LobbyEvent> controllerListener;
    private final Supplier<LobbyDetailsDTO> lobbyDetailsSupplier;
    private Runnable startAdventureCallback = () -> {};

    public GameEventListener(EventListener<LobbyEvent> controllerListener, Supplier<LobbyDetailsDTO> lobbyDetailsSupplier) {
        this.controllerListener = controllerListener;
        this.lobbyDetailsSupplier = lobbyDetailsSupplier;
    }

    public void setStartAdventureCallback(Runnable startAdventureCallback) {
        this.startAdventureCallback = startAdventureCallback;
    }

    /**
     * Requests a snapshot of the current game state for the specified ship board.
     * Generates a {@link GameSnapshotEvent} to retrieve game information (e.g., after a disconnection).
     *
     * @param game      the game for which to request the snapshot
     * @param shipBoard the ship board for which to request the snapshot
     */
    public void requestSnapshot(Game game, ShipBoard shipBoard) {
        controllerListener.notifyEvent(new GameSnapshotEvent(
                ConversionUtils.convert(shipBoard),
                lobbyDetailsSupplier.get(),
                getGameSnapshot(game)
        ));
    }

    private GameSnapshot getGameSnapshot(Game game) {
        Map<String, ShipBoardDTO> ships = new HashMap<>();
        for (ShipBoard s : game.getShipBoards()) {
            ships.put(ConversionUtils.convert(s),DtoConverter.getShipBoard(s));
        }
        return new GameSnapshot(
                DtoConverter.getFlightBoard(game.getFlightBoard()),
                DtoConverter.getComplexState(game.getCurrentState()),
                ships,
                (game.getDeck().getCurrentCard() != null)
                        ? game.getDeck().getCurrentCard().getId()
                        : -1
        );
    }

    public void notifyStartAdventure() {
        startAdventureCallback.run();
    }

    /**
     * Notifies the controller about a component activation event.
     * @param shipBoard the ship board where the component is located
     * @param point the point on the ship board where the component is located
     * @param active true if the component is activated, false otherwise
     */
    public void notifyActivateComponentEvent(ShipBoard shipBoard, Point point, boolean active) {
        controllerListener.notifyEvent(new ActivateComponentEvent(ConversionUtils.convert(shipBoard), point, active));
    }

    /**
     * Notifies the controller about an update to the flight board.
     * @param shipBoard the ship board that has been moved
     * @param position the position on the flight board where the ship is now located
     */
    public void notifyFlightBoardUpdateEvent(ShipBoard shipBoard, int position) {
        controllerListener.notifyEvent(new FlightBoardUpdateEvent(
                ConversionUtils.convert(shipBoard),
                position
        ));
    }

    /**
     * Notifies the controller about the flipping of the hourglass.
     * @param shipBoard the ship board responsible for the hourglass flipping
     */
    public void notifyFlipHourglassEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new FlipHourglassEvent(ConversionUtils.convert(shipBoard)));
    }

    /**
     * Notifies the controller about a forecast details request event.
     * @param shipBoard the ship board that is requesting the forecast details
     * @param adventureCards the list of adventure cards associated with the forecast
     */
    public void notifyForecastDetailsEvent(ShipBoard shipBoard, List<AdventureCard> adventureCards) {
        controllerListener.notifyEvent(new ForecastDetailsEvent(
                ConversionUtils.convert(shipBoard),
                adventureCards.stream().map(AdventureCard::getId).toList()));
    }

    /**
     * Notifies the controller about a game end event with final scores.
     * @param finalScores a map containing the final scores of each ship board
     */
    public void notifyGameEndEvent(Map<ShipBoard, Integer> finalScores) {
        controllerListener.notifyEvent(new GameEndEvent(
                ConversionUtils.convertMap(finalScores)
        ));
    }

    /**
     * Notifies the controller about a game state update event.
     * @param gameState the current game state
     */
    public void notifyGameStateUpdateEvent(GameState gameState) {
        System.out.println("Game state update: " + gameState.getClass().getSimpleName());
        controllerListener.notifyEvent(new GameStateUpdateEvent(
                DtoConverter.getState(gameState)
        ));
    }

    /**
     * Notifies the controller about the update of a ship board cargo hold.
     * @param shipBoard the ship board whose cargo hold has been updated
     * @param point the point on the ship board where the cargo hold is located
     * @param goodsType the type of goods that have been added or removed
     * @param add true if goods have been added, false if they have been removed
     */
    public void notifyGoodsUpdateEvent(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
        controllerListener.notifyEvent(new GoodsUpdateEvent(
                ConversionUtils.convert(shipBoard),
                point,
                goodsType,
                add
        ));
    }

    /**
     * Notifies the controller about that a placed component has been grabbed in the hand.
     * @param shipBoard the ship board where the component has been placed
     */
    public void notifyGrabPlacedComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new GrabPlacedComponentEvent(
                ConversionUtils.convert(shipBoard)
        ));
    }

    /**
     * Notifies the controller about that a stashed component has been grabbed in the hand.
     * @param shipBoard the ship board where the component has been stashed
     * @param index the index of the stashed component
     */
    public void notifyGrabStashedComponentEvent(ShipBoard shipBoard, int index) {
        controllerListener.notifyEvent(new GrabStashedComponentEvent(
                ConversionUtils.convert(shipBoard),
                index
        ));
    }

    /**
     * Notifies the controller about the hourglass ending.
     */
    public void notifyHourglassEndEvent() {
        controllerListener.notifyEvent(new HourglassEndEvent());
    }

    /**
     * Notifies the controller about a cabin being initialized with a specific crew type.
     * @param shipBoard the ship board where the cabin is located
     * @param point the point on the ship board where the cabin is located
     * @param crewType the type of crew that will occupy the cabin
     */
    public void notifyCabinInitializationEvent(ShipBoard shipBoard, Point point, CrewType crewType) {
        controllerListener.notifyEvent(new InitializeCabinEvent(ConversionUtils.convert(shipBoard), point, crewType));
    }

    /**
     * Notifies the controller about a crew member being lost from a ship board.
     * @param shipBoard the ship board from which the crew member is lost
     * @param point the point on the ship board where the crew member was located
     */
    public void notifyLoseCrewEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new LoseCrewEvent(
                ConversionUtils.convert(shipBoard),
                point
        ));
    }

    /**
     * Notifies the controller about a new card being drawn.
     * @param adventureCard the adventure card that has been drawn
     */
    public void notifyNewCardEvent(AdventureCard adventureCard) {
        controllerListener.notifyEvent(new NewCardEvent(
                adventureCard.getId()
        ));
    }

    /**
     * Notifies the controller about the peeking of a forecast by a player with a specific ship board.
     * @param shipBoard the ship board of the player who peeked the forecast
     * @param deckIndex the index of the deck that was peeked
     */
    public void notifyPeekForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new PeekForecastEvent(
                ConversionUtils.convert(shipBoard),
                deckIndex));
    }

    /**
     * Notifies the controller about the placement of a component on a ship board.
     * @param shipBoard the ship board where the component is placed
     *                  @param orientation the orientation of the component
     *                                     @param position the position on the ship board where the component is placed
     */
    public void notifyPlaceComponentEvent(ShipBoard shipBoard, Direction orientation, Point position) {
        controllerListener.notifyEvent(new PlaceComponentEvent(
                ConversionUtils.convert(shipBoard),
                position,
                orientation
        ));
    }

    /**
     * Notifies the controller about a planet being chosen by a player with a specific ship board.
     * @param shipBoard the ship board of the player who chose the planet
     *                  @param planetId the ID of the chosen planet
     *                                  @param nextShipBoard the ship board that will be asked next
     */
    public void notifyPlanetChoiceEvent(ShipBoard shipBoard, int planetId, ShipBoard nextShipBoard) {
        controllerListener.notifyEvent(new PlanetChoiceEvent(
                ConversionUtils.convert(shipBoard),
                planetId,
                ConversionUtils.convert(nextShipBoard)
        ));
    }

    /**
     * Notifies the controller about a component being rejected from a ship board.
     * @param shipBoard the ship board from which the component is rejected
     */
    public void notifyRejectComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new RejectComponentEvent(ConversionUtils.convert(shipBoard)));
    }

    /**
     * Notifies the controller about a request to acquire a forecast for a ship board.
     * @param shipBoard the ship board requesting the forecast
     * @param deckIndex the index of the deck from which to acquire the forecast
     */
    public void notifyReleaseForecastEvent(ShipBoard shipBoard, int deckIndex) {
        controllerListener.notifyEvent(new ReleaseForecastEvent(
                ConversionUtils.convert(shipBoard), deckIndex));
    }

    /**
     * Notifies the controller about a request to remove a component from a ship board at a specific point.
     * @param shipBoard the ship board from which the component is removed
     * @param point the point on the ship board where the component is located
     */
    public void notifyRemoveComponentEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new RemoveComponentEvent(
                ConversionUtils.convert(shipBoard),
                point
        ));
    }

    /**
     * Notifies the controller about the acquisition of a covered component by a player with a specific ship board.
     * @param shipBoard the ship board of the player acquiring the component
     * @param component the component being acquired
     */
    public void notifyRequestFaceDownComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(new RequestFaceDownComponentEvent(
                ConversionUtils.convert(shipBoard),
                component.getId()
        ));
    }

    /**
     * Notifies the controller about the acquisition of a face-up component by a player with a specific ship board.
     * @param shipBoard the ship board of the player acquiring the component
     * @param component the component being acquired
     */
    public void notifyRequestFaceUpComponentEvent(ShipBoard shipBoard, Component component) {
        controllerListener.notifyEvent(new RequestFaceUpComponentEvent(
                ConversionUtils.convert(shipBoard),
                component.getId()
        ));
    }

    /**
     * Notifies the controller about a ship being broken into sets of connected pieces.
     * @param shipBoard the ship board that has been broken
     * @param shipPieces a list of point sets representing the connected pieces of the ship
     */
    public void notifyShipNotConnectedEvent(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        controllerListener.notifyEvent(new ShipNotConnectedEvent(
                ConversionUtils.convert(shipBoard),
                shipPieces
        ));
    }

    /**
     * Notifies the controller about a ship piece being removed from a ship board.
     * @param shipBoard the ship board from which the piece is removed
     * @param pieceIndex the index of the piece being removed
     */
    public void notifyShipPieceRemovalEvent(ShipBoard shipBoard, int pieceIndex) {
        controllerListener.notifyEvent(new ShipPieceRemoveEvent(
                ConversionUtils.convert(shipBoard),
                pieceIndex
        ));
    }

    /**
     * Notifies the controller about a ship acquiring credits
     * @param shipBoard the ship board acquiring the credits
     * @param value the number of credits acquired
     */
    public void notifyGrabCreditsEvent(ShipBoard shipBoard, int value) {
        controllerListener.notifyEvent(new GrabCreditsEvent(
                ConversionUtils.convert(shipBoard),
                value
        ));
    }

    /**
     * Notifies the controller about the stashing of the current component by a player with a specific ship board.
     * @param shipBoard the ship board of the player stashing the component
     */
    public void notifyStashComponentEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new StashComponentEvent(ConversionUtils.convert(shipBoard)));
    }

    /**
     * Notifies the controller about the surrender of some players with specific ship boards.
     * @param ships the list of ship boards that have surrendered
     */
    public void notifySurrenderEvent(List<ShipBoard> ships) {
        controllerListener.notifyEvent(new SurrenderEvent(
                ConversionUtils.convertCollection(ships, ArrayList::new)
        ));
    }

    /**
     * Notifies the controller about a surrender request event for a specific ship board.
     * @param shipBoard the ship board requesting surrender
     * @param cause the cause of the surrender
     */
    public void notifySurrenderRequestEvent(ShipBoard shipBoard, SurrenderCause cause) {
        controllerListener.notifyEvent(new SurrenderRequestEvent(ConversionUtils.convert(shipBoard), cause));
    }

    /**
     * Notifies the controller about a ship board update event.
     * @param shipBoard the ship board that has been updated
     */
    public void notifyUseBatteryEvent(ShipBoard shipBoard, Point point) {
        controllerListener.notifyEvent(new UseBatteryEvent(ConversionUtils.convert(shipBoard), point));
    }

    /**
     * Notifies the controller about a ship board validation event.
     * @param shipBoard the ship board that is being validated
     */
    public void notifyValidateShipEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new ValidateShipEvent(
                ConversionUtils.convert(shipBoard)
        ));
    }

    /**
     * Notifies the controller about the update of the current player.
     * @param shipBoard the ship board of the current player
     */
    public void notifyCurrentPlayerUpdateEvent(ShipBoard shipBoard) {
        controllerListener.notifyEvent(new CurrentPlayerUpdateEvent(ConversionUtils.convert(shipBoard)));
    }
}
