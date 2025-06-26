package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.serverController.utils.ConversionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for converting game states and other objects into their corresponding DTOs.
 */
public class DtoConverter {
    /**
     * Converts a GameStateInterface to a StateDTO.
     *
     * @param gameState the game state to convert
     * @return the corresponding StateDTO
     */
    public static StateDTO getState(GameStateInterface gameState) {
        switch (gameState) {
            case AddGoodsState addGoodsState -> {
                return stateDtoOf(addGoodsState);
            }
            case ChoosePlanetState choosePlanetState -> {
                return stateDtoOf(choosePlanetState);
            }
            case ChooseShipPieceState chooseShipPieceState -> {
                return stateDtoOf(chooseShipPieceState);
            }
            case DeclareEnginePowerState declareEnginePowerState -> {
                return stateDtoOf(declareEnginePowerState);
            }
            case DeclareFirePowerState declareFirePowerState -> {
                return stateDtoOf(declareFirePowerState);
            }
            case DrawCardState drawCardState -> {
                return new SimpleStateDTO(Player.getPlayer(drawCardState.getShipBoard()).getNickname(), StateDTOType.DRAW_CARD);
            }
            case GrabRewardState grabRewardState -> {
                return stateDtoOf(grabRewardState);
            }
            case HandleProjectileState handleProjectileState -> {
                return stateDtoOf(handleProjectileState);
            }
            case RemoveCrewState removeCrewState -> {
                return stateDtoOf(removeCrewState);
            }
            case RemoveGoodsState removeGoodsState -> {
                return stateDtoOf(removeGoodsState);
            }
            case ShipCorrectionState shipCorrectionState -> {
                return stateDtoOf(shipCorrectionState);
            }
            case ShipInitializationState shipInitializationState -> {
                return stateDtoOf(shipInitializationState);
            }
            case SecondShipBuildingState _, TestShipBuildingState _ -> {
                return new ShipBuildingDTO();
            }
            case GameState _ -> throw new RuntimeException("Invalid state");
        }
    }

    /**
     * Converts a GameStateInterface to a ComplexStateDTO.
     *
     * @param gameState the game state to convert
     * @return the corresponding ComplexStateDTO
     */
    public static ComplexStateDTO getComplexState(GameStateInterface gameState) {
        switch (gameState) {
            case AddGoodsState addGoodsState -> {
                return stateDtoOf(addGoodsState);
            }
            case ChoosePlanetState choosePlanetState -> {
                return new ComplexChoosePlanetDTO(stateDtoOf(choosePlanetState), ConversionUtils.convertArray(choosePlanetState.getChosenPlanets()));
            }
            case ChooseShipPieceState chooseShipPieceState -> {
                return stateDtoOf(chooseShipPieceState);
            }
            case DeclareEnginePowerState declareEnginePowerState -> {
                return stateDtoOf(declareEnginePowerState);
            }
            case DeclareFirePowerState declareFirePowerState -> {
                return stateDtoOf(declareFirePowerState);
            }
            case DrawCardState drawCardState -> {
                return new ComplexDrawCardDTO(ConversionUtils.convert(drawCardState.getShipBoard()), drawCardState.hasDrawn());
            }
            case GrabRewardState grabRewardState -> {
                return stateDtoOf(grabRewardState);
            }
            case HandleProjectileState handleProjectileState -> {
                return stateDtoOf(handleProjectileState);
            }
            case RemoveCrewState removeCrewState -> {
                return stateDtoOf(removeCrewState);
            }
            case RemoveGoodsState removeGoodsState -> {
                return stateDtoOf(removeGoodsState);
            }
            case SecondShipBuildingState secondShipBuildingState -> {
                return new SecondShipBuildingDTO(getBuildingData(secondShipBuildingState), getHourglass(secondShipBuildingState.getHourglass()), ConversionUtils.convertMap(secondShipBuildingState.getShipToForecasts()));
            }
            case ShipCorrectionState shipCorrectionState -> {
                return stateDtoOf(shipCorrectionState);
            }
            case ShipInitializationState shipInitializationState -> {
                return stateDtoOf(shipInitializationState);
            }
            case TestShipBuildingState testShipBuildingState -> {
                return new TestShipBuildingDTO(getBuildingData(testShipBuildingState));
            }
            case GameState _ -> {
                throw new RuntimeException("Invalid state");
            }
        }
    }

    /**
     * Converts a Lobby object to an ActiveLobbyDTO.
     *
     * @param lobby the lobby to convert
     * @return the corresponding ActiveLobbyDTO
     */
    public static ActiveLobbyDTO getActiveLobby(Lobby lobby) {
        return new ActiveLobbyDTO(lobby.getId(), lobby.getLevel(), lobby.getNumPlayers(), lobby.getPlayers().stream().map(Player::getNickname).toList(), lobby.getHost().getNickname());
    }

    /**
     * Converts a Lobby object to a LobbyDetailsDTO.
     *
     * @param lobby the lobby to convert
     * @return the corresponding LobbyDetailsDTO
     */
    public static LobbyDetailsDTO getLobbyDetails(Lobby lobby) {
        return new LobbyDetailsDTO(lobby.getId(), lobby.getPlayerColors().entrySet().stream().collect(Collectors.toMap(e -> e.getKey().getNickname(), Map.Entry::getValue)), lobby.getLevel(), lobby.getNumPlayers());
    }

    /**
     * Converts a ShipBuildingState to a BuildingDataDTO.
     *
     * @param shipBuildingState the ship building state to convert
     * @return the corresponding BuildingDataDTO
     */
    public static BuildingDataDTO getBuildingData(ShipBuildingState shipBuildingState) {
        return new BuildingDataDTO(shipBuildingState.getComponentBank().getNumCovered(), shipBuildingState.getComponentBank().getUncoveredIds(), ConversionUtils.convertCollection(shipBuildingState.getCompletedShipBoards(), HashSet::new));
    }

    /**
     * Converts a FlightBoard object to a FlightBoardDTO.
     *
     * @param flightBoard the flight board to convert
     * @return the corresponding FlightBoardDTO
     */
    public static FlightBoardDTO getFlightBoard(FlightBoard flightBoard) {
        return new FlightBoardDTO(ConversionUtils.convertMap(flightBoard.getShipToPlace()));
    }

    /**
     * Converts a ShipBoard object to a ShipBoardDTO.
     *
     * @param shipBoard the ship board to convert
     * @return the corresponding ShipBoardDTO
     */
    public static ShipBoardDTO getShipBoard(ShipBoard shipBoard) {
        return new ShipBoardDTO(shipBoard.getComponentMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> getComponent(entry.getValue()))), shipBoard.getLastComponent().map(Component::getId).orElse(-1), shipBoard.getLastPosition().orElse(null), shipBoard.getStashedComponents().stream().map(Component::getId).toList(), shipBoard.getCredits(), shipBoard.getLosses());
    }

    /**
     * Converts an Hourglass object to an HourglassDTO.
     *
     * @param hourglass the hourglass to convert
     * @return the corresponding HourglassDTO
     */
    public static HourglassDTO getHourglass(Hourglass hourglass) {
        return new HourglassDTO(hourglass.getMissingTime(), hourglass.getFlipsLeft(), hourglass.getIsRunning());
    }

    /**
     * Converts a ComponentInterface to a ComponentDTO.
     *
     * @param component the component to convert
     * @return the corresponding ComponentDTO
     */
    public static ComponentDTO getComponent(ComponentInterface component) {
        ComponentPayload payload = null;
        switch (component) {
            case Activatable activatable -> {
                payload = new ActivatablePayload(activatable.isActive());
            }
            case Battery battery -> {
                payload = new BatteryPayload(battery.getNumBatteries());
            }
            case Cabin cabin -> {
                payload = new CabinPayload(cabin.getCrewType(), cabin.getNumResidents());
            }
            case CargoHold cargoHold -> {
                payload = new CargoPayload(cargoHold.getGoods());
            }
            case Cannon _, Engine _, LifeSupport _, Component _ -> {
            }
        }
        return new ComponentDTO(component.getId(), component.getOrientation(), payload);
    }

    //region Specific state conversion methods
    private static ShipCorrectionDTO stateDtoOf(ShipCorrectionState shipCorrectionState) {
        return new ShipCorrectionDTO(ConversionUtils.convertCollection(shipCorrectionState.getValidShipBoards(), HashSet::new), ConversionUtils.convertMap(shipCorrectionState.getShipPiecesMap()), shipCorrectionState.getShouldDiscard());
    }

    private static ShipInitializationDTO stateDtoOf(ShipInitializationState shipInitializationState) {
        return new ShipInitializationDTO(ConversionUtils.convertMap(shipInitializationState.getShipRelevantCabins()));
    }

    private static SimpleStateDTO stateDtoOf(DeclareFirePowerState declareFirePowerState) {
        return new SimpleStateDTO(ConversionUtils.convert(declareFirePowerState.getShipBoard()), StateDTOType.DECLARE_FIRE_POWER);
    }

    private static SimpleStateDTO stateDtoOf(DeclareEnginePowerState declareEnginePowerState) {
        return new SimpleStateDTO(ConversionUtils.convert(declareEnginePowerState.getShipBoard()), StateDTOType.DECLARE_ENGINE_POWER);
    }

    private static SimpleStateDTO stateDtoOf(GrabRewardState grabRewardState) {
        return new SimpleStateDTO(ConversionUtils.convert(grabRewardState.getShipBoard()), StateDTOType.GRAB_REWARD);
    }

    private static AddGoodsDTO stateDtoOf(AddGoodsState addGoodsState) {
        return new AddGoodsDTO(ConversionUtils.convert(addGoodsState.getShipBoard()), addGoodsState.getGoodsBuffer());
    }

    private static ChooseShipPieceDTO stateDtoOf(ChooseShipPieceState chooseShipPieceState) {
        return new ChooseShipPieceDTO(Player.getPlayer(chooseShipPieceState.getShipBoard()).getNickname(), chooseShipPieceState.getShipPieces());
    }

    private static HandleProjectileDTO stateDtoOf(HandleProjectileState handleProjectileState) {
        return new HandleProjectileDTO(Player.getPlayer(handleProjectileState.getShipBoard()).getNickname(), handleProjectileState.getProjectile().getProjectileType(), handleProjectileState.getProjectile().getDiceRoll(), handleProjectileState.getProjectile().getDirection(), handleProjectileState.getAvailablePositions());
    }

    private static RemoveCrewDTO stateDtoOf(RemoveCrewState removeCrewState) {
        return new RemoveCrewDTO(Player.getPlayer(removeCrewState.getShipBoard()).getNickname(), removeCrewState.getCrewSacrifice());
    }

    private static RemoveGoodsDTO stateDtoOf(RemoveGoodsState removeGoodsState) {
        return new RemoveGoodsDTO(Player.getPlayer(removeGoodsState.getShipBoard()).getNickname(), removeGoodsState.getGoodsToLose());
    }

    private static ChoosePlanetDTO stateDtoOf(ChoosePlanetState choosePlanetState) {
        return new ChoosePlanetDTO(ConversionUtils.convert(choosePlanetState.getCurrentShip()), choosePlanetState.getNumPlanets());
    }

    //endregion
}
