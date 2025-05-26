package it.polimi.ingsw.galaxytruckers.view.controller;


import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.serverController.events.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class EventHandler implements it.polimi.ingsw.galaxytruckers.serverController.events.EventHandler {
    private ClientModel clientModel;
    private PlayerRegistry playerRegistry;

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
                        activateComponentEvent.point());
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
                clientModel.addPlayer(player, GameColor.BLUE);
            }
            case LobbyDetailsEvent lobbyDetailsEvent -> {
                //TODO: send all lobby information
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
                //TODO: add method to model to notify a ship is not connected
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
                //TODO: add method to model to update correct shipBoards
            }
        }
    }
}
