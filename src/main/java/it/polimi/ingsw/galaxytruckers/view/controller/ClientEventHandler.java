package it.polimi.ingsw.galaxytruckers.view.controller;


import it.polimi.ingsw.galaxytruckers.serverController.dto.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ClientEventHandler implements EventHandler<Event> {
    private final ClientModel clientModel;
    private final PlayerRegistry playerRegistry;
    private final ConversionUtils conversionUtils;

    public ClientEventHandler(ClientModel clientModel, PlayerRegistry playerRegistry) {
        this.clientModel = clientModel;
        this.playerRegistry = playerRegistry;
        this.conversionUtils = new ConversionUtils(playerRegistry);
    }

    /**
     * Handles the {@code ModelEvent} passed as the argument, updating all
     * affected classes
     *
     * @param event the event to be handled
     */
    @Override
    public void handleEvent(Event event) {
        switch (event) {
            case ActivateComponentEvent activateComponentEvent -> clientModel.notifyActivateComponent(
                    playerRegistry.getByNickname(activateComponentEvent.playerName()).getShipBoard(),
                    activateComponentEvent.point()
            );
            case FlightBoardUpdateEvent flightBoardUpdateEvent -> clientModel.notifyFlightBoardPosition(
                    playerRegistry.getByNickname(flightBoardUpdateEvent.playerName()).getShipBoard(),
                    flightBoardUpdateEvent.position()
            );
            case FlipHourglassEvent flipHourglassEvent -> clientModel.notifyFlipHourglass(
                    playerRegistry.getByNickname(flipHourglassEvent.playerName()).getShipBoard()
            );
            case ForecastDetailsEvent forecastDetailsEvent -> clientModel.setForecastDeck(
                    forecastDetailsEvent.forecastDeckIds().stream()
                            .map(id -> AdventureCardRegistry.getInstance().getCard(id))
                            .toList()
            );
            case GameEndEvent gameEndEvent -> {
                clientModel.setFinalScores(
                        gameEndEvent.playerToScore().entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> playerRegistry.getByNickname(e.getKey()), Map.Entry::getValue))
                );
                clientModel.setMetaState(MetaState.ENDGAME);
            }
            case GoodsUpdateEvent goodsUpdateEvent -> {
                if (goodsUpdateEvent.add()) {
                    clientModel.notifyPlaceGoods(
                            playerRegistry.getByNickname(goodsUpdateEvent.playerName()).getShipBoard(),
                            goodsUpdateEvent.point(),
                            goodsUpdateEvent.goodsType()
                    );
                } else {
                    clientModel.notifyRemoveGoods(
                            playerRegistry.getByNickname(goodsUpdateEvent.playerName()).getShipBoard(),
                            goodsUpdateEvent.point(),
                            goodsUpdateEvent.goodsType()
                    );
                }
            }
            case GrabStashedComponentEvent grabStashedComponentEvent -> clientModel.notifyGrabStashedComponent(
                    playerRegistry.getByNickname(grabStashedComponentEvent.playerName()).getShipBoard(),
                    grabStashedComponentEvent.index()
            );
            case HourglassEndEvent hourglassEndEvent -> clientModel.notifyHourglassEnd();
            case InitializeCabinEvent initializeCabinEvent -> clientModel.notifyInitializeCabin(
                    playerRegistry.getByNickname(initializeCabinEvent.playerName()).getShipBoard(),
                    initializeCabinEvent.point(),
                    initializeCabinEvent.crewType()
            );
            case JoinLobbyEvent joinLobbyEvent -> {
                Player player = playerRegistry.addPlayer(joinLobbyEvent.playerName());
                if(!clientModel.getPlayers().contains(player)) {
                    clientModel.addPlayer(player, joinLobbyEvent.color());
                }
            }
            case LobbyDetailsEvent lobbyDetailsEvent -> {
                LobbyDetailsDTO details = lobbyDetailsEvent.details();
                setupLobby(details,true);
            }
            case NewCardEvent newCardEvent -> clientModel.notifyDrawCard(
                    AdventureCardRegistry.getInstance().getCard(newCardEvent.cardId())
            );
            case PeekForecastEvent peekForecastEvent -> clientModel.notifyPeekForecast(
                    playerRegistry.getByNickname(peekForecastEvent.playerName()).getShipBoard(),
                    peekForecastEvent.forecastIndex()
            );
            case PlaceComponentEvent placeComponentEvent -> clientModel.notifyPlaceComponent(
                    playerRegistry.getByNickname(placeComponentEvent.playerName()).getShipBoard(),
                    placeComponentEvent.position(),
                    placeComponentEvent.rotation()
            );
            case PlanetChoiceEvent planetChoiceEvent -> clientModel.notifyChoosePlanet(
                    playerRegistry.getByNickname(planetChoiceEvent.playerName()).getShipBoard(),
                    planetChoiceEvent.planetId(),
                    playerRegistry.getByNickname(planetChoiceEvent.nextPlayerName()).getShipBoard()
            );
            case PlayerDisconnectionEvent playerDisconnectionEvent -> {
                //TODO: handle player disconnection
            }
            case PlayerExitEvent playerExitEvent -> {
                //TODO: handle player exit
            }
            case RejectComponentEvent rejectComponentEvent -> clientModel.notifyRejectComponent(
                    playerRegistry.getByNickname(rejectComponentEvent.playerName()).getShipBoard()
            );
            case ReleaseForecastEvent releaseForecastEvent -> clientModel.notifyReleaseForecast(
                    playerRegistry.getByNickname(releaseForecastEvent.playerName()).getShipBoard()
            );
            case RemoveComponentEvent removeComponentEvent -> clientModel.notifyRemoveComponent(
                    playerRegistry.getByNickname(removeComponentEvent.playerName()).getShipBoard(),
                    removeComponentEvent.point()
            );
            case RequestFaceDownComponentEvent requestFaceDownComponentEvent -> clientModel.notifyRequestRandComponent(
                    playerRegistry.getByNickname(requestFaceDownComponentEvent.playerName()).getShipBoard(),
                    ComponentRegistry.getInstance().getComponent(requestFaceDownComponentEvent.componentId())
            );
            case RequestFaceUpComponentEvent requestFaceUpComponentEvent -> clientModel.notifyRequestComponent(
                    playerRegistry.getByNickname(requestFaceUpComponentEvent.playerName()).getShipBoard(),
                    ComponentRegistry.getInstance().getComponent(requestFaceUpComponentEvent.componentId())
            );
            case ShipNotConnectedEvent shipNotConnectedEvent -> clientModel.notifyShipNotConnected(
                    playerRegistry.getByNickname(shipNotConnectedEvent.playerName()).getShipBoard(),
                    shipNotConnectedEvent.shipPieces()
            );
            case ShipPieceRemoveEvent shipPieceRemoveEvent -> clientModel.notifyChooseShipPiece(
                    playerRegistry.getByNickname(shipPieceRemoveEvent.playerName()).getShipBoard(),
                    shipPieceRemoveEvent.index()
            );
            case GrabCreditsEvent grabCreditsEvent -> clientModel.notifyGrabCredits(
                    playerRegistry.getByNickname(grabCreditsEvent.playerName()).getShipBoard(),
                    grabCreditsEvent.value()
            );
            case StashComponentEvent stashComponentEvent -> clientModel.notifyStashComponent(
                    playerRegistry.getByNickname(stashComponentEvent.playerName()).getShipBoard()
            );
            case SurrenderEvent surrenderEvent -> //TODO: handle player surrender
                    clientModel.notifySurrenderShip(
                            surrenderEvent.playerNames().stream()
                                    .map(playerRegistry::getByNickname)
                                    .map(Player::getShipBoard)
                                    .collect(Collectors.toSet())
                    );
            case UseBatteryEvent useBatteryEvent -> clientModel.notifyUseBattery(
                    playerRegistry.getByNickname(useBatteryEvent.playerName()).getShipBoard(),
                    useBatteryEvent.point()
            );
            case ValidateShipEvent validateShipEvent -> clientModel.notifyShipValidated(
                    playerRegistry.getByNickname(validateShipEvent.playerName()).getShipBoard()
            );
            case GameStateUpdateEvent gameStateUpdateEvent -> updateGameState(gameStateUpdateEvent.stateDTO());
            case LoseCrewEvent loseCrewEvent -> clientModel.notifyLoseCrew(
                    playerRegistry.getByNickname(loseCrewEvent.playerName()).getShipBoard(),
                    loseCrewEvent.point()
            );
            case AddActiveLobbyEvent addActiveLobbyEvent -> {
                ActiveLobbyDTO lobby = addActiveLobbyEvent.newLobby();
                clientModel.notifyNewLobby(new Lobby(lobby.id(), lobby.numPlayers(), lobby.level(), lobby.host()));
            }
            case RemoveActiveLobbyEvent removeActiveLobbyEvent -> clientModel.notifyRemoveLobby(removeActiveLobbyEvent.lobbyId());
            case SetActiveLobbiesEvent setActiveLobbyEvent -> {
                for(ActiveLobbyDTO lobby : setActiveLobbyEvent.activeLobbies()) {
                    clientModel.notifyNewLobby(new Lobby(lobby.id(), lobby.numPlayers(), lobby.level(), lobby.host()));
                }
                clientModel.setMetaState(MetaState.JOINORCREATE);
            }
            case CurrentPlayerUpdateEvent currentPlayerUpdateEvent -> clientModel.notifyCurrentPlayerUpdate(
                    playerRegistry.getByNickname(currentPlayerUpdateEvent.playerName()).getShipBoard()
            );
            case SurrenderRequestEvent surrenderRequestEvent -> clientModel.notifySurrenderRequest(
                    playerRegistry.getByNickname(surrenderRequestEvent.playerName()));

            case GrabPlacedComponentEvent grabPlacedComponentEvent -> {
                clientModel.notifyGrabPlacedComponent(
                        playerRegistry.getByNickname(grabPlacedComponentEvent.playerName()).getShipBoard()
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

                    //Set Game State
                    updateGameState(gameSnapshot.state());
                } else {
                    setupLobby(details,true);
                }
            }
        }
    }

    private void setupLobby(LobbyDetailsDTO details, boolean setMetaState) {
        clientModel.createGame(details.level());
        for (String name : details.playerColors().keySet()) {
            Player player = playerRegistry.addPlayer(name);
            if(!clientModel.getPlayers().contains(player)) {
                clientModel.addPlayer(player, details.playerColors().get(name));
            }
        }
        if (setMetaState) clientModel.setMetaState(MetaState.INLOBBY);
    }

    private void updateGameState(StateDTO stateDTO) {
        GameState gameState = conversionUtils.getGameState(stateDTO,clientModel);
        if (gameState == null) System.err.println("GameState is null");
        else {
            clientModel.notifyCurrentState(gameState);
            clientModel.setMetaState(MetaState.INGAME);
        }
    }

    private void updateGameState(ComplexStateDTO complexStateDTO) {
        GameState state = conversionUtils.getGameState(complexStateDTO,clientModel);
        if (state == null) System.err.println("GameState is null");
        else {
            clientModel.notifyCurrentState(state);
            clientModel.setMetaState(MetaState.INGAME);
        }
    }
}
