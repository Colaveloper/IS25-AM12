package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

public class StateDTOConverter {
    public static StateDTO convert(GameState gameState) {
        switch (gameState) {
            case AdventureState adventureState -> {
                return convert(adventureState);
            }
            case GameStateStub gameStateStub -> {
                throw new IllegalStateException("This state is meant for test only");
            }
            case ShipBuildingState shipBuildingState -> {
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
                                )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                );
            }
            case ShipInitializationState shipInitializationState -> {
                return new ShipInitializationDTO(shipInitializationState.getShipRelevantCabins().entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> Player.getPlayer(e.getKey()).getNickname(),
                                Map.Entry::getValue)));
            }
        }
    }

    private static StateDTO convert(AdventureState adventureState) {
        switch (adventureState) {
            case ActivateState activateState -> {
                switch (activateState) {
                    case ActivateStateStub activateStateStub -> {
                        throw new IllegalStateException("This test is meant for test only");
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
                    case HandleProjectileState handleProjectileState -> {
                        return new HandleProjectileDTO(
                                Player.getPlayer(handleProjectileState.getShipBoard()).getNickname(),
                                handleProjectileState.getProjectile().getProjectileType(),
                                handleProjectileState.getProjectile().getDiceRoll(),
                                handleProjectileState.getProjectile().getDirection(),
                                handleProjectileState.getAvailablePositions()
                        );
                    }
                }
            }
            case AddGoodsState addGoodsState -> {
                return new AddGoodsDTO(
                        Player.getPlayer(addGoodsState.getShipBoard()).getNickname(),
                        addGoodsState.getGoodsBuffer()
                );
            }
            case ChoosePlanetState choosePlanetState -> {
                return new ChoosePlanetDTO(
                        Player.getPlayer(choosePlanetState.getShipBoard()).getNickname(),
                        choosePlanetState.getOptions()
                );
            }
            case ChooseShipPieceState chooseShipPieceState -> {
                return new ChooseShipPieceDTO(
                        Player.getPlayer(chooseShipPieceState.getShipBoard()).getNickname(),
                        chooseShipPieceState.getShipPieces()
                );
            }
            case DrawCardState drawCardState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(drawCardState.getShipBoard()).getNickname(),
                        StateDTOType.DRAW_CARD
                );
            }
            case EndGameState endGameState -> {
                //TODO: decide whether to implement this method
                return null;
            }
            case GrabRewardState grabRewardState -> {
                return new SimpleStateDTO(
                        Player.getPlayer(grabRewardState.getShipBoard()).getNickname(),
                        StateDTOType.GRAB_REWARD
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
            case AdventureStateStub adventureStateStub -> {
                throw new IllegalStateException("This state is meant for test only");
            }
        }
    }
}
