package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class StateDTOConverter {
    public static StateDTO convert(GameStateInterface gameState) {
        switch (gameState) {
            case AddGoodsState addGoodsState -> {
                return new AddGoodsDTO(
                        Player.getPlayer(addGoodsState.getShipBoard()).getNickname(),
                        addGoodsState.getGoodsBuffer()
                );
            }
            case ChoosePlanetState choosePlanetState -> {
                return new ChoosePlanetDTO(
                        Player.getPlayer(choosePlanetState.getCurrentShip()).getNickname(),
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
            case SecondShipBuildingState secondShipBuildingState -> {
                return new ShipBuildingDTO();
            }
            case ShipCorrectionState shipCorrectionState -> {
                return new ShipCorrectionDTO(
                        shipCorrectionState.getValidShipBoards().stream()
                                .map(s -> Player.getPlayer(s).getNickname()).collect(Collectors.toSet()),
                        shipCorrectionState.getShipPieces().entrySet().stream()
                                .map(e -> Map.entry(
                                        Player.getPlayer(e.getKey()).getNickname(),
                                        e.getValue()
                                )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                        shipCorrectionState.getShouldDiscard()
                );
            }
            case ShipInitializationState shipInitializationState -> {
                return new ShipInitializationDTO(shipInitializationState.getShipRelevantCabins().entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> Player.getPlayer(e.getKey()).getNickname(),
                                Map.Entry::getValue)));
            }
            case TestShipBuildingState testShipBuildingState -> {
                return new ShipBuildingDTO();
            }
            case GameState state -> {
                throw new RuntimeException("Invalid state");
            }
        }
    }
}
