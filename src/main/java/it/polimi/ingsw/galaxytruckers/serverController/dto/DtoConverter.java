package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.serverController.utils.NetworkUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class DtoConverter {
    public static StateDTO getState(GameStateInterface gameState) {
        switch (gameState) {
            case AddGoodsState addGoodsState -> {
                return new AddGoodsDTO(
                        NetworkUtils.convert(addGoodsState.getShipBoard()),
                        addGoodsState.getGoodsBuffer()
                );
            }
            case ChoosePlanetState choosePlanetState -> {
                return new ChoosePlanetDTO(
                        NetworkUtils.convert(choosePlanetState.getCurrentShip()),
                        choosePlanetState.getNumPlanets()
                );
            }
            case ChooseShipPieceState chooseShipPieceState -> {
                return new ChooseShipPieceDTO(
                        Player.getPlayer(chooseShipPieceState.getShipBoard()).getNickname(),
                        chooseShipPieceState.getShipPieces()
                );
            }
            case DeclareEnginePowerState declareEnginePowerState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(declareEnginePowerState.getShipBoard()).getNickname(),
                        StateDTOType.DECLARE_ENGINE_POWER
                );
            }
            case DeclareFirePowerState declareFirePowerState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(declareFirePowerState.getShipBoard()).getNickname(),
                        StateDTOType.DECLARE_FIRE_POWER
                );
            }
            case DrawCardState drawCardState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(drawCardState.getShipBoard()).getNickname(),
                        StateDTOType.DRAW_CARD
                );
            }
            case GrabRewardState grabRewardState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(grabRewardState.getShipBoard()).getNickname(),
                        StateDTOType.GRAB_REWARD
                );
            }
            case HandleProjectileState handleProjectileState -> {
                return new HandleProjectileDTO(
                        Player.getPlayer(handleProjectileState.getShipBoard()).getNickname(),
                        handleProjectileState.getProjectile().getProjectileType(),
                        handleProjectileState.getProjectile().getDiceRoll(),
                        handleProjectileState.getProjectile().getDirection(),
                        handleProjectileState.getAvailablePositions()
                );
            }
            case RemoveCrewState removeCrewState -> {
                return new RemoveCrewDTO(
                        Player.getPlayer(removeCrewState.getShipBoard()).getNickname(),
                        removeCrewState.getCrewSacrifice()
                );
            }
            case RemoveGoodsState removeGoodsState -> {
                return new RemoveGoodsDTO(
                        Player.getPlayer(removeGoodsState.getShipBoard()).getNickname(),
                        removeGoodsState.getGoodsToLose()
                );
            }
            case SecondShipBuildingState _ -> {
                return new ShipBuildingDTO();
            }
            case ShipCorrectionState shipCorrectionState -> {
                return new ShipCorrectionDTO(
                        NetworkUtils.convertCollection(shipCorrectionState.getValidShipBoards(), HashSet::new),
                        NetworkUtils.convertMap(shipCorrectionState.getShipPiecesMap()),
                        shipCorrectionState.getShouldDiscard()
                );
            }
            case ShipInitializationState shipInitializationState -> {
                return new ShipInitializationDTO(
                        NetworkUtils.convertMap(shipInitializationState.getShipRelevantCabins())
                );
            }
            case TestShipBuildingState _ -> {
                return new ShipBuildingDTO();
            }
            case GameState _ -> throw new RuntimeException("Invalid state");
        }
    }

    public static ComplexStateDTO getComplexState(GameStateInterface gameState) {
        switch (gameState) {
            case AddGoodsState addGoodsState -> {
            }
            case ChoosePlanetState choosePlanetState -> {
            }
            case ChooseShipPieceState chooseShipPieceState -> {
            }
            case DeclareEnginePowerState declareEnginePowerState -> {
            }
            case DeclareFirePowerState declareFirePowerState -> {
            }
            case DrawCardState drawCardState -> {
            }
            case GrabRewardState grabRewardState -> {
            }
            case HandleProjectileState handleProjectileState -> {
            }
            case RemoveCrewState removeCrewState -> {
            }
            case RemoveGoodsState removeGoodsState -> {
            }
            case SecondShipBuildingState secondShipBuildingState -> {
                return new SecondShipBuildingDTO(
                        getBuildingData(secondShipBuildingState),
                        getHourglass(secondShipBuildingState.getHourglass()),
                        NetworkUtils.convertMap(secondShipBuildingState.getShipToForecasts())
                );
            }
            case ShipCorrectionState shipCorrectionState -> {
            }
            case ShipInitializationState shipInitializationState -> {
            }
            case TestShipBuildingState testShipBuildingState -> {
                return new TestShipBuildingDTO(
                        getBuildingData(testShipBuildingState)
                );
            }
            case GameState _ -> {
                throw new RuntimeException("Invalid state");
            }
        }
        return new ShipInitializationDTO(new HashMap<>());
    }

    public static ActiveLobbyDTO getActiveLobby(Lobby lobby) {
        return new ActiveLobbyDTO(
                lobby.getId(),
                lobby.getLevel(),
                lobby.getNumPlayers(),
                lobby.getPlayers().stream().map(Player::getNickname).toList(),
                lobby.getHost().getNickname());
    }

    public static LobbyDetailsDTO getLobbyDetails(Lobby lobby) {
        return new LobbyDetailsDTO(
                lobby.getId(),
                lobby.getPlayerColors().entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> e.getKey().getNickname(),
                                Map.Entry::getValue
                        )),
                lobby.getLevel(),
                lobby.getNumPlayers()
        );
    }

    public static BuildingDataDTO getBuildingData(ShipBuildingState shipBuildingState) {
        return new BuildingDataDTO(
                shipBuildingState.getComponentBank().getNumCovered(),
                shipBuildingState.getComponentBank().getUncoveredIds(),
                NetworkUtils.convertCollection(shipBuildingState.getCompletedShipBoards(), HashSet::new)
        );
    }

    public static FlightBoardDTO getFlightBoard(FlightBoard flightBoard) {
        return new FlightBoardDTO(
                NetworkUtils.convertMap(flightBoard.getShipToPlace())
        );
    }

    public static ShipBoardDTO getShipBoard(ShipBoard shipBoard) {
        return new ShipBoardDTO(
                shipBoard.getComponentMap().entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getId()
                ))
        );
    }

    public static HourglassDTO getHourglass(Hourglass hourglass) {
        return new HourglassDTO(
                hourglass.getMissingTime(),
                hourglass.getFlipsLeft(),
                hourglass.getIsRunning()
        );
    }
}
