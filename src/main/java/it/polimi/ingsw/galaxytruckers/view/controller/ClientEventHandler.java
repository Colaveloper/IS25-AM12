package it.polimi.ingsw.galaxytruckers.view.controller;


import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventHandler;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.Event;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientEventHandler implements EventHandler<Event> {
    private final ClientModel clientModel;
    private final PlayerRegistry playerRegistry;

    public ClientEventHandler(ClientModel clientModel, PlayerRegistry playerRegistry) {
        this.clientModel = clientModel;
        this.playerRegistry = playerRegistry;
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
            case ActivateComponentEvent activateComponentEvent -> {
                clientModel.notifyActivateComponent(
                        playerRegistry.getByNickname(activateComponentEvent.playerName()).getShipBoard(),
                        activateComponentEvent.point()
                );
            }
            case FlightBoardUpdateEvent flightBoardUpdateEvent -> {
                clientModel.notifyFlightBoardPosition(
                        playerRegistry.getByNickname(flightBoardUpdateEvent.playerName()).getShipBoard(),
                        flightBoardUpdateEvent.position()
                );
            }
            case FlipHourglassEvent flipHourglassEvent -> {
                clientModel.notifyFlipHourglass(
                        playerRegistry.getByNickname(flipHourglassEvent.playerName()).getShipBoard()
                );
            }
            case ForecastDetailsEvent forecastDetailsEvent -> {
                clientModel.setForecastDeck(
                        forecastDetailsEvent.forecastDeckIds().stream()
                                .map(id -> AdventureCardRegistry.getInstance().getCard(id))
                                .toList()
                );
            }
            case GameEndEvent gameEndEvent -> {
                clientModel.setFinalScores(
                        gameEndEvent.playerToScore().entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> playerRegistry.getByNickname(e.getKey()), Map.Entry::getValue))
                );
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
            case GrabStashedComponentEvent grabStashedComponentEvent -> {
                clientModel.notifyGrabStashedComponent(
                        playerRegistry.getByNickname(grabStashedComponentEvent.playerName()).getShipBoard(),
                        grabStashedComponentEvent.index()
                );
            }
            case HourglassEndEvent hourglassEndEvent -> {
                clientModel.notifyHourglassEnd();
            }
            case InitializeCabinEvent initializeCabinEvent -> {
                clientModel.notifyInitializeCabin(
                        playerRegistry.getByNickname(initializeCabinEvent.playerName()).getShipBoard(),
                        initializeCabinEvent.point(),
                        initializeCabinEvent.crewType()
                );
            }
            case JoinLobbyEvent joinLobbyEvent -> {
                Player player = playerRegistry.addPlayer(joinLobbyEvent.playerName());
                //TODO: also send the player color
                // Only add to model if the player is not already present
                if(!clientModel.getPlayers().contains(player)) {
                    clientModel.addPlayer(player, joinLobbyEvent.color());
                }
            }
            case LobbyDetailsEvent lobbyDetailsEvent -> {
                //TODO: update client state
                clientModel.createGame(lobbyDetailsEvent.level(), lobbyDetailsEvent.playersN());
                for (String name : lobbyDetailsEvent.playerColors().keySet()) {
                    Player player = playerRegistry.addPlayer(name);
                    if(!clientModel.getPlayers().contains(player)) {
                        clientModel.addPlayer(player, lobbyDetailsEvent.playerColors().get(name));
                    }
                }
                clientModel.setMetaState(MetaState.INLOBBY);
            }
            case NewCardEvent newCardEvent -> {
                clientModel.notifyDrawCard(
                        AdventureCardRegistry.getInstance().getCard(newCardEvent.cardId())
                );
            }
            case PeekForecastEvent peekForecastEvent -> {
                clientModel.notifyPeekForecast(
                        playerRegistry.getByNickname(peekForecastEvent.playerName()).getShipBoard(),
                        peekForecastEvent.forecastIndex()
                );
            }
            case PlaceComponentEvent placeComponentEvent -> {
                clientModel.notifyPlaceComponent(
                        playerRegistry.getByNickname(placeComponentEvent.playerName()).getShipBoard(),
                        placeComponentEvent.position(),
                        placeComponentEvent.rotation()
                );
            }
            case PlanetChoiceEvent planetChoiceEvent -> {
                clientModel.notifyChoosePlanet(
                        playerRegistry.getByNickname(planetChoiceEvent.playerName()).getShipBoard(),
                        planetChoiceEvent.planetId()
                );
            }
            case PlayerDisconnectionEvent playerDisconnectionEvent -> {
                //TODO: handle player disconnection
            }
            case PlayerExitEvent playerExitEvent -> {
                //TODO: handle player exit
            }
            case RejectComponentEvent rejectComponentEvent -> {
                clientModel.notifyRejectComponent(
                        playerRegistry.getByNickname(rejectComponentEvent.playerName()).getShipBoard()
                );
            }
            case ReleaseForecastEvent releaseForecastEvent -> {
                clientModel.notifyReleaseForecast(
                        playerRegistry.getByNickname(releaseForecastEvent.playerName()).getShipBoard()
                );
            }
            case RemoveComponentEvent removeComponentEvent -> {
                clientModel.notifyRemoveComponent(
                        playerRegistry.getByNickname(removeComponentEvent.playerName()).getShipBoard(),
                        removeComponentEvent.point()
                );
            }
            case RequestFaceDownComponentEvent requestFaceDownComponentEvent -> {
                clientModel.notifyRequestRandComponent(
                        playerRegistry.getByNickname(requestFaceDownComponentEvent.playerName()).getShipBoard(),
                        ComponentRegistry.getInstance().getComponent(requestFaceDownComponentEvent.componentId())
                );
            }
            case RequestFaceUpComponentEvent requestFaceUpComponentEvent -> {
                clientModel.notifyRequestComponent(
                        playerRegistry.getByNickname(requestFaceUpComponentEvent.playerName()).getShipBoard(),
                        ComponentRegistry.getInstance().getComponent(requestFaceUpComponentEvent.componentId())
                );
            }
            case ShipNotConnectedEvent shipNotConnectedEvent -> {
                clientModel.notifyShipNotConnected(
                        playerRegistry.getByNickname(shipNotConnectedEvent.playerName()).getShipBoard(),
                        shipNotConnectedEvent.shipPieces()
                );
            }
            case ShipPieceRemoveEvent shipPieceRemoveEvent -> {
                clientModel.notifyChooseShipPiece(
                        playerRegistry.getByNickname(shipPieceRemoveEvent.playerName()).getShipBoard(),
                        shipPieceRemoveEvent.index()
                );
            }
            case ShipStatUpdateEvent shipStatUpdateEvent -> {
                //TODO: decide whether this method is truly needed
            }
            case StashComponentEvent stashComponentEvent -> {
                clientModel.notifyStashComponent(
                        playerRegistry.getByNickname(stashComponentEvent.playerName()).getShipBoard()
                );
            }
            case SurrenderEvent surrenderEvent -> {
                //TODO: handle player surrender
            }
            case UseBatteryEvent useBatteryEvent -> {
                clientModel.notifyUseBattery(
                        playerRegistry.getByNickname(useBatteryEvent.playerName()).getShipBoard(),
                        useBatteryEvent.point()
                );
            }
            case ValidateShipEvent validateShipEvent -> {
                clientModel.notifyShipValidated(
                        playerRegistry.getByNickname(validateShipEvent.playerName()).getShipBoard()
                );
            }
            case GameStateUpdateEvent gameStateUpdateEvent -> {
                updateGameState(gameStateUpdateEvent.stateDTO());
            }
            case LoseCrewEvent loseCrewEvent -> {
                clientModel.notifyLoseCrew(
                        playerRegistry.getByNickname(loseCrewEvent.playerName()).getShipBoard(),
                        loseCrewEvent.point()
                );
            }
            case AddActiveLobbyEvent addActiveLobbyEvent -> {

            }
            case RemoveActiveLobbyEvent removeActiveLobbyEvent -> {

            }
            case SetActiveLobbiesEvent setActiveLobbyEvent -> {

            }
        }
    }

    private void updateGameState(StateDTO stateDTO) {
        GameState gameState = null;
        switch (stateDTO) {
            case AddGoodsDTO addGoodsDTO -> {
                 gameState = new AddGoodsState(
                        addGoodsDTO.goodsBuffer(),
                        playerRegistry.getByNickname(addGoodsDTO.playerName()).getShipBoard()
                );
            }
            case ChoosePlanetDTO choosePlanetDTO -> {
                gameState = new ChoosePlanetState(
                        playerRegistry.getByNickname(choosePlanetDTO.playerName()).getShipBoard(),
                        choosePlanetDTO.availablePlanets()
                );
            }
            case ChooseShipPieceDTO chooseShipPieceDTO -> {
                gameState = new ChooseShipPieceState(
                        chooseShipPieceDTO.shipPieces(),
                        playerRegistry.getByNickname(chooseShipPieceDTO.playerName()).getShipBoard()
                );
            }
            case HandleProjectileDTO handleProjectileDTO -> {
                gameState = new HandleProjectileState(
                        playerRegistry.getByNickname(handleProjectileDTO.playerName()).getShipBoard(),
                        new Projectile(handleProjectileDTO.diceRoll(), handleProjectileDTO.direction(), handleProjectileDTO.projectileType()),
                        handleProjectileDTO.availablePoints()
                );
            }
            case RemoveCrewDTO removeCrewDTO -> {
                gameState = new RemoveCrewState(
                        removeCrewDTO.crewLoss(),
                        playerRegistry.getByNickname(removeCrewDTO.playerName()).getShipBoard()
                );
            }
            case RemoveGoodsDTO removeGoodsDTO -> {
                gameState = new RemoveGoodsState(
                        removeGoodsDTO.goodsLoss(),
                        playerRegistry.getByNickname(removeGoodsDTO.playerName()).getShipBoard()
                );
            }
            case ShipBuildingDTO shipBuildingDTO -> {
                gameState = clientModel.getGame().getGameFactory().createShipBuildingState();
            }
            case ShipCorrectionDTO shipCorrectionDTO -> {
                gameState = new ShipCorrectionState();
            }
            case ShipInitializationDTO shipInitializationDTO -> {
                Map<ShipBoard, Map<CrewType, Set<Point>>> setMap = shipInitializationDTO.crewTypeToCabins()
                        .entrySet()
                        .stream()
                        .map(e -> Map.entry(
                                playerRegistry.getByNickname(e.getKey()).getShipBoard(),
                                e.getValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
                Map<ShipBoard, Map<CrewType, List<Point>>> finalMap = new HashMap<>();
                for (ShipBoard shipBoard : setMap.keySet()) {
                    finalMap.put(shipBoard, new HashMap<>());
                    for (CrewType crewType : setMap.get(shipBoard).keySet()) {
                        finalMap.get(shipBoard).put(crewType, setMap.get(shipBoard).get(crewType).stream().toList());
                    }
                }
                gameState = new ShipInitializationState(
                        finalMap
                );
            }
            case SimpleStateDTO simpleStateDTO -> {
                ShipBoard shipBoard = playerRegistry.getByNickname(simpleStateDTO.playerName()).getShipBoard();
                switch (simpleStateDTO.type()) {
                    case DECLARE_ENGINE_POWER -> {
                        gameState = new DeclareEnginePowerState(shipBoard);
                    }
                    case DECLARE_FIRE_POWER -> {
                        gameState = new DeclareFirePowerState(shipBoard);
                    }
                    case DRAW_CARD -> {
                        gameState = new DrawCardState(shipBoard);
                    }
                    case GRAB_REWARD -> {
                        gameState = new GrabRewardState(shipBoard);
                    }
                }
            }
        }
        clientModel.notifyCurrentState(gameState);
        clientModel.setMetaState(MetaState.INGAME);
    }
}
