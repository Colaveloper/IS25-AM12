package it.polimi.ingsw.galaxytruckers.view.controller;


import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.serverController.dto.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Handles events received from the server and updates the client model accordingly.
 * <p>
 * This class implements the {@link EventHandler} interface for {@link Event} types. It processes various
 * game events, converting data as needed and notifying the {@link ClientModel} and related classes to update
 * the client-side state. The handler uses a {@link PlayerRegistry} and {@link ConversionUtils} to map player
 * names and data between server and client representations.
 * </p>
 */
public class ClientEventHandler implements EventHandler<Event> {
    private final ClientController controller;
    private final ClientModel clientModel;
    private final PlayerRegistry playerRegistry;
    private final ConversionUtils conversionUtils;

    /**
     * Constructs a ClientEventHandler with the given client model and player registry.
     *
     * @param clientController
     * @param clientModel      the client-side model to update
     * @param playerRegistry   the registry for player information
     */
    public ClientEventHandler(ClientController clientController, ClientModel clientModel, PlayerRegistry playerRegistry) {
        this.controller = clientController;
        this.clientModel = clientModel;
        this.playerRegistry = playerRegistry;
        this.conversionUtils = new ConversionUtils(playerRegistry);
    }

    /**
     * Handles the {@code ModelEvent} passed as the argument, updating all
     * affected classes.
     *
     * @param event the event to be handled
     */
    @Override
    public void handleEvent(Event event) {
        switch (event) {
            case ActivateComponentEvent activateComponentEvent -> clientModel.notifyActivateComponent(
                    conversionUtils.convertName(activateComponentEvent.playerName()),
                    activateComponentEvent.point()
            );
            case FlightBoardUpdateEvent flightBoardUpdateEvent -> clientModel.notifyFlightBoardPosition(
                    conversionUtils.convertName(flightBoardUpdateEvent.playerName()),
                    flightBoardUpdateEvent.position()
            );
            case FlipHourglassEvent flipHourglassEvent -> clientModel.notifyFlipHourglass(
                    conversionUtils.convertName(flipHourglassEvent.playerName())
            );
            case ForecastDetailsEvent forecastDetailsEvent -> clientModel.setForecastDeck(
                    forecastDetailsEvent.forecastDeckIds().stream()
                            .map(id -> AdventureCardRegistry.getInstance().getCard(id))
                            .toList()
            );
            case GameEndEvent gameEndEvent -> {
                clientModel.setFinalScores(
                        conversionUtils.convertPlayerMap(gameEndEvent.playerToScore())
                );
                clientModel.setMetaState(MetaState.ENDGAME);
            }
            case GoodsUpdateEvent goodsUpdateEvent -> {
                if (goodsUpdateEvent.add()) {
                    clientModel.notifyPlaceGoods(
                            conversionUtils.convertName(goodsUpdateEvent.playerName()),
                            goodsUpdateEvent.point(),
                            goodsUpdateEvent.goodsType()
                    );
                } else {
                    clientModel.notifyRemoveGoods(
                            conversionUtils.convertName(goodsUpdateEvent.playerName()),
                            goodsUpdateEvent.point(),
                            goodsUpdateEvent.goodsType()
                    );
                }
            }
            case GrabStashedComponentEvent grabStashedComponentEvent -> clientModel.notifyGrabStashedComponent(
                    conversionUtils.convertName(grabStashedComponentEvent.playerName()),
                    grabStashedComponentEvent.index()
            );
            case HourglassEndEvent _ -> clientModel.notifyHourglassEnd();
            case InitializeCabinEvent initializeCabinEvent -> clientModel.notifyInitializeCabin(
                    conversionUtils.convertName(initializeCabinEvent.playerName()),
                    initializeCabinEvent.point(),
                    initializeCabinEvent.crewType()
            );
            case JoinLobbyEvent joinLobbyEvent -> clientModel.addPlayer(
                    playerRegistry.addPlayer(joinLobbyEvent.playerName()),
                    joinLobbyEvent.color()
            );
            case LobbyDetailsEvent lobbyDetailsEvent -> {
                LobbyDetailsDTO details = lobbyDetailsEvent.details();
                setupLobby(details,true);
            }
            case NewCardEvent newCardEvent -> clientModel.notifyDrawCard(
                    AdventureCardRegistry.getInstance().getCard(newCardEvent.cardId())
            );
            case PeekForecastEvent peekForecastEvent -> clientModel.notifyPeekForecast(
                    conversionUtils.convertName(peekForecastEvent.playerName()),
                    peekForecastEvent.forecastIndex()
            );
            case PlaceComponentEvent placeComponentEvent -> clientModel.notifyPlaceComponent(
                    conversionUtils.convertName(placeComponentEvent.playerName()),
                    placeComponentEvent.position(),
                    placeComponentEvent.rotation()
            );
            case PlanetChoiceEvent planetChoiceEvent -> clientModel.notifyChoosePlanet(
                    conversionUtils.convertName(planetChoiceEvent.playerName()),
                    planetChoiceEvent.planetIndex(),
                    conversionUtils.convertName(planetChoiceEvent.nextPlayerName())
            );
            case PlayerDisconnectionEvent playerDisconnectionEvent -> {
                    controller.reportError("Player " + playerDisconnectionEvent.playerName() + " has disconnected.");
            }
            case PlayerExitEvent playerExitEvent -> {
                    controller.reportError("Player " + playerExitEvent.playerName() + " has left the game.");
                    controller.clearModel();
            }
            case RejectComponentEvent rejectComponentEvent -> clientModel.notifyRejectComponent(
                    conversionUtils.convertName(rejectComponentEvent.playerName())
            );
            case ReleaseForecastEvent releaseForecastEvent -> clientModel.notifyReleaseForecast(
                    conversionUtils.convertName(releaseForecastEvent.playerName())
            );
            case RemoveComponentEvent removeComponentEvent -> clientModel.notifyRemoveComponent(
                    conversionUtils.convertName(removeComponentEvent.playerName()),
                    removeComponentEvent.point()
            );
            case RequestFaceDownComponentEvent requestFaceDownComponentEvent -> clientModel.notifyRequestRandComponent(
                    conversionUtils.convertName(requestFaceDownComponentEvent.playerName()),
                    ComponentRegistry.getInstance().getComponent(requestFaceDownComponentEvent.componentId())
            );
            case RequestFaceUpComponentEvent requestFaceUpComponentEvent -> clientModel.notifyRequestComponent(
                    conversionUtils.convertName(requestFaceUpComponentEvent.playerName()),
                    ComponentRegistry.getInstance().getComponent(requestFaceUpComponentEvent.componentId())
            );
            case ShipNotConnectedEvent shipNotConnectedEvent -> clientModel.notifyShipNotConnected(
                    conversionUtils.convertName(shipNotConnectedEvent.playerName()),
                    shipNotConnectedEvent.shipPieces()
            );
            case ShipPieceRemoveEvent shipPieceRemoveEvent -> clientModel.notifyChooseShipPiece(
                    conversionUtils.convertName(shipPieceRemoveEvent.playerName()),
                    shipPieceRemoveEvent.index()
            );
            case GrabCreditsEvent grabCreditsEvent -> clientModel.notifyGrabCredits(
                    conversionUtils.convertName(grabCreditsEvent.playerName()),
                    grabCreditsEvent.value()
            );
            case StashComponentEvent stashComponentEvent -> clientModel.notifyStashComponent(
                    conversionUtils.convertName(stashComponentEvent.playerName())
            );
            case SurrenderEvent surrenderEvent -> //TODO: handle player surrender
                    clientModel.notifySurrenderShip(
                            conversionUtils.convertCollection(surrenderEvent.playerNames(), HashSet::new)
                    );
            case UseBatteryEvent useBatteryEvent -> clientModel.notifyUseBattery(
                    conversionUtils.convertName(useBatteryEvent.playerName()),
                    useBatteryEvent.point()
            );
            case ValidateShipEvent validateShipEvent -> clientModel.notifyShipValidated(
                    conversionUtils.convertName(validateShipEvent.playerName())
            );
            case GameStateUpdateEvent gameStateUpdateEvent -> updateGameState(gameStateUpdateEvent.stateDTO());
            case LoseCrewEvent loseCrewEvent -> clientModel.notifyLoseCrew(
                    conversionUtils.convertName(loseCrewEvent.playerName()),
                    loseCrewEvent.point()
            );
            case AddActiveLobbyEvent addActiveLobbyEvent -> {
                ActiveLobbyDTO lobby = addActiveLobbyEvent.newLobby();
                clientModel.notifyNewLobby(new Lobby(lobby.id(), lobby.numPlayers(), lobby.level(), lobby.host()));
            }
            case RemoveActiveLobbyEvent removeActiveLobbyEvent -> clientModel.notifyRemoveLobby(removeActiveLobbyEvent.lobbyId());
            case SetActiveLobbiesEvent setActiveLobbyEvent -> {
                clientModel.setPlayer(playerRegistry.addPlayer(setActiveLobbyEvent.playerName()));
                for(ActiveLobbyDTO lobby : setActiveLobbyEvent.activeLobbies()) {
                    clientModel.notifyNewLobby(new Lobby(lobby.id(), lobby.numPlayers(), lobby.level(), lobby.host()));
                }
                if (!setActiveLobbyEvent.reconnect()) clientModel.setMetaState(MetaState.JOINORCREATE);
            }
            case CurrentPlayerUpdateEvent currentPlayerUpdateEvent -> clientModel.notifyCurrentPlayerUpdate(
                    conversionUtils.convertName(currentPlayerUpdateEvent.playerName())
            );
            case SurrenderRequestEvent surrenderRequestEvent -> clientModel.notifySurrenderRequest(
                    playerRegistry.getByNickname(surrenderRequestEvent.playerName())
            );
            case GrabPlacedComponentEvent grabPlacedComponentEvent -> {
                clientModel.notifyGrabPlacedComponent(
                        conversionUtils.convertName(grabPlacedComponentEvent.playerName())
                );
            }
            case GameSnapshotEvent gameSnapshotEvent -> {
                LobbyDetailsDTO details = gameSnapshotEvent.lobbyDetails();
                GameSnapshot gameSnapshot = gameSnapshotEvent.gameSnapshot();
                if (gameSnapshot != null) {
                    //Set lobby details
                    setupLobby(details,false);

                    Game game = clientModel.getGame();
                    //Set FlightBoard
                    Map<String,Integer> playerToPlace = gameSnapshot.flightBoardDTO().playerToPlace();
                    game.getFlightBoard().setShipToPlace(conversionUtils.convertMap(playerToPlace));

                    //Set Ships
                    Map<String, ShipBoardDTO> ships = gameSnapshot.ships();
                    for (String nickname : ships.keySet()) {
                        ShipBoardDTO ship = ships.get(nickname);
                        ShipBoard shipBoard = conversionUtils.convertName(nickname);
                        //Set ComponentMap
                        Map<Point, Component> componentMap = new HashMap<>();
                        Map<Point, ComponentDTO> componentDTOMap = ship.componentMap();
                        for (Point point : componentDTOMap.keySet()) {
                            componentMap.put(point,conversionUtils.convertComponent(componentDTOMap.get(point)));
                        }
                        shipBoard.setComponentMap(componentMap);
                        //Set hand
                        Component lastComponent = (ship.lastComponent() != -1)
                                ? ComponentRegistry.getInstance().getComponent(ship.lastComponent())
                                : null;
                        Point lastPosition = ship.lastPosition();
                        shipBoard.setHand(lastComponent,lastPosition);
                        //Set stash
                        List<Component> stashedComponents = ship.stashedComponents().stream()
                                .map(id -> ComponentRegistry.getInstance().getComponent(id))
                                .toList();
                        shipBoard.setStashedComponents(stashedComponents);
                        //Set Stats
                        shipBoard.setCredits(ship.credits());
                        shipBoard.setLosses(ship.losses());
                    }

                    //Set current card
                    AdventureCard adventureCard = (gameSnapshot.currentCardId() != -1)
                            ? AdventureCardRegistry.getInstance().getCard(gameSnapshot.currentCardId())
                            : null;
                    clientModel.getGame().setCurrentCard(adventureCard);

                    //Set Game State
                    updateGameState(gameSnapshot.state());
                } else {
                    setupLobby(details,true);
                }
            }
            case EndByDisconnectionEvent endByDisconnectionEvent -> {
                controller.reportError("The game has ended because all other players disconnected.");
                clientModel.clearGame();
            }
        }
    }

    private void setupLobby(LobbyDetailsDTO details, boolean setMetaState) {
        clientModel.createGame(details.level());
        for (String name : details.playerColors().keySet()) {
            clientModel.addPlayer(playerRegistry.addPlayer(name), details.playerColors().get(name));
        }
        if (setMetaState) clientModel.setMetaState(MetaState.INLOBBY);
    }

    private void updateGameState(StateDTO stateDTO) {
        GameState gameState = conversionUtils.getGameState(stateDTO,clientModel);
        if (gameState == null) System.err.println("GameState is null");
        else {
            clientModel.notifyCurrentState(gameState);
        }
    }

    private void updateGameState(ComplexStateDTO complexStateDTO) {
        GameState state = conversionUtils.getGameState(complexStateDTO,clientModel);
        if (state == null) System.err.println("GameState is null");
        else {
            clientModel.notifyCurrentState(state);
        }
    }
}
