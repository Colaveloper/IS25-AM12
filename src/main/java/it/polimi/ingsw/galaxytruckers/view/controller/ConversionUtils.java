package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.HourglassDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;

import java.util.*;
import java.util.function.Supplier;

/**
 * Utility class for converting between server and client representations of game data.
 * <p>
 * This class provides methods to convert player names, collections, and maps between server-side (String-based)
 * and client-side (Player, ShipBoard, etc.) representations. It also assists in converting DTOs to client model objects.
 * </p>
 */
public class ConversionUtils {
    private final PlayerRegistry playerRegistry;

    /**
     * Constructs a ConversionUtils with the given player registry.
     *
     * @param playerRegistry the registry for player information
     */
    public ConversionUtils(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    /**
     * Converts a map from player nicknames to values into a map from Player objects to values.
     *
     * @param map the map with String keys (nicknames)
     * @param <T> the type of the values
     * @return a map with Player keys
     */
    public <T> Map<Player,T> convertPlayerMap(Map<String,T> map) {
        Map<Player, T> convertedMap = new HashMap<>();
        for (String name : map.keySet()) {
            convertedMap.put(playerRegistry.getByNickname(name), map.get(name));
        }
        return convertedMap;
    }

    /**
     * Converts a map from player nicknames to values into a map from ShipBoard objects to values.
     *
     * @param map the map with String keys (nicknames)
     * @param <T> the type of the values
     * @return a map with ShipBoard keys
     */
    public <T> Map<ShipBoard, T> convertMap(Map<String, T> map) {
        Map<ShipBoard, T> convertedMap = new HashMap<>();
        for (String name : map.keySet()) {
            convertedMap.put(playerRegistry.getByNickname(name).getShipBoard(), map.get(name));
        }
        return convertedMap;
    }

    /**
     * Converts a collection of player nicknames to a collection of ShipBoard objects.
     *
     * @param nicknames the collection of player nicknames
     * @param supplier a supplier for the resulting collection type
     * @param <T> the type of the input collection
     * @param <R> the type of the output collection
     * @return a collection of ShipBoard objects
     */
    public <T extends Collection<String>, R extends Collection<ShipBoard>> R convertCollection(T nicknames, Supplier<R> supplier) {
        return nicknames.stream()
                .map(x -> playerRegistry.getByNickname(x).getShipBoard())
                .collect(supplier,R::add,R::addAll);
    }

    /**
     * Converts a player nickname to a ShipBoard object.
     *
     * @param nickname the player's nickname
     * @return the corresponding ShipBoard
     */
    public ShipBoard convertName(String nickname) {
        return playerRegistry.getByNickname(nickname).getShipBoard();
    }

    /**
     * Converts an array of player nicknames to an array of ShipBoard objects.
     *
     * @param nicknames the array of player nicknames
     * @return an array of ShipBoard objects
     */
    public ShipBoard[] convertArray(String[] nicknames) {
        return Arrays.stream(nicknames).map(this::convertName).toArray(ShipBoard[]::new);
    }

    /**
     * Converts building data from a DTO to the client model's ShipBuildingState.
     *
     * @param shipBuildingState the client model's ship building state
     * @param data the DTO containing building data
     */
    public void convertBuildingData(ShipBuildingState shipBuildingState, BuildingDataDTO data) {
        //Set completed shipboards
        shipBuildingState.setCompletedShipBoards(convertCollection(data.completedNames(), HashSet::new));

        //Set ComponentBank
        ComponentBank componentBank = shipBuildingState.getComponentBank();
        componentBank.setCoveredComponentsN(data.numCovered());
        componentBank.setUncoveredComponents(data.uncoveredIds().stream()
                .map(id -> ComponentRegistry.getInstance().getComponent(id))
                .toList());
    }

    /**
     * Converts a ComponentDTO to a Component, updating its properties as needed.
     * <p>
     * This method retrieves the component by ID from the registry, sets its orientation, and updates
     * any additional properties (such as batteries, crew, goods, or activation state) based on the payload.
     * </p>
     * @param componentDTO the DTO containing component data
     * @return the updated Component instance
     */
    public Component convertComponent(ComponentDTO componentDTO) {
        Component component = ComponentRegistry.getInstance().getComponent(componentDTO.id());
        component.setOrientation(componentDTO.orientation());
        switch (component) {
            case Battery battery -> {
                battery.setNumBatteries(componentDTO.payload().numBatteries());
            }
            case Cabin cabin -> {
                cabin.setCrewType(componentDTO.payload().crewType());
                cabin.setNumResidents(componentDTO.payload().numResidents());
            }
            case CargoHold cargoHold -> {
                cargoHold.setGoods(componentDTO.payload().goods());
            }
            case Activatable activatable -> {
                activatable.setActive(componentDTO.payload().active());
            }
            case Cannon _, Engine _, LifeSupport _, Component _ -> {}
        }
        return component;
    }

    /**
     * Converts a StateDTO to a GameState for the client model.
     * <p>
     * This method dispatches to the appropriate state conversion method based on the type of StateDTO.
     * </p>
     * @param stateDTO the state DTO to convert
     * @param clientModel the client model instance
     * @return the corresponding GameState
     */
    public GameState getGameState(StateDTO stateDTO, ClientModel clientModel) {
        ShipBoard myShip = clientModel.getMyShip();
        switch (stateDTO) {
            case AddGoodsDTO addGoodsDTO -> {
                return getAddGoodsState(addGoodsDTO, myShip);
            }
            case ChoosePlanetDTO choosePlanetDTO -> {
                return getChoosePlanetState(choosePlanetDTO, myShip);
            }
            case ChooseShipPieceDTO chooseShipPieceDTO -> {
                return getChooseShipPieceState(chooseShipPieceDTO, myShip);
            }
            case HandleProjectileDTO handleProjectileDTO -> {
                return getHandleProjectileState(handleProjectileDTO, myShip);
            }
            case RemoveCrewDTO removeCrewDTO -> {
                return getRemoveCrewState(removeCrewDTO, myShip);
            }
            case RemoveGoodsDTO removeGoodsDTO -> {
                return getRemoveGoodsState(removeGoodsDTO, myShip);
            }
            case ShipBuildingDTO _ -> {
                return getShipBuildingState(clientModel);
            }
            case ShipCorrectionDTO shipCorrectionDTO -> {
                return getShipCorrectionState(shipCorrectionDTO, myShip);
            }
            case ShipInitializationDTO shipInitializationDTO -> {
                return getShipInitializationState(shipInitializationDTO, myShip);
            }
            case SimpleStateDTO simpleStateDTO -> {
                return getSimpleState(simpleStateDTO, myShip);
            }
        }
    }

    /**
     * Converts a ComplexStateDTO to a GameState for the client model.
     * <p>
     * This method dispatches to the appropriate state conversion method based on the type of ComplexStateDTO.
     * It handles additional complex state types such as planet choices and draw card states.
     * </p>
     * @param stateDTO the complex state DTO to convert
     * @param clientModel the client model instance
     * @return the corresponding GameState
     */
    public GameState getGameState(ComplexStateDTO stateDTO, ClientModel clientModel) {
        ShipBoard myShip = clientModel.getMyShip();
        switch (stateDTO) {
            case AddGoodsDTO addGoodsDTO -> {
                return getAddGoodsState(addGoodsDTO, myShip);
            }
            case ChooseShipPieceDTO chooseShipPieceDTO -> {
                return getChooseShipPieceState(chooseShipPieceDTO, myShip);
            }
            case HandleProjectileDTO handleProjectileDTO -> {
                return getHandleProjectileState(handleProjectileDTO, myShip);
            }
            case RemoveCrewDTO removeCrewDTO -> {
                return getRemoveCrewState(removeCrewDTO, myShip);
            }
            case RemoveGoodsDTO removeGoodsDTO -> {
                return getRemoveGoodsState(removeGoodsDTO, myShip);
            }
            case ShipCorrectionDTO shipCorrectionDTO -> {
                return getShipCorrectionState(shipCorrectionDTO, myShip);
            }
            case ShipInitializationDTO shipInitializationDTO -> {
                return getShipInitializationState(shipInitializationDTO, myShip);
            }
            case SimpleStateDTO simpleStateDTO -> {
                if (simpleStateDTO.type().isComplex())
                    return getSimpleState(simpleStateDTO, myShip);
                else return null;
            }
            case ComplexChoosePlanetDTO complexChoosePlanetDTO -> {
                ChoosePlanetState choosePlanetState = getChoosePlanetState(
                        complexChoosePlanetDTO.baseData(),
                        clientModel.getMyShip()
                );
                choosePlanetState.setOptions(convertArray(complexChoosePlanetDTO.choices()));
                return choosePlanetState;
            }
            case ComplexDrawCardDTO complexDrawCardDTO -> {
                DrawCardState state = new DrawCardState(
                        clientModel.getMyShip(),
                        convertName(complexDrawCardDTO.playerName())
                );
                state.setHasDrawn(complexDrawCardDTO.hasDrawn());
                return state;
            }
            case SecondShipBuildingDTO secondShipBuildingDTO -> {
                SecondShipBuildingState state = new SecondShipBuildingState();
                convertBuildingData(state,secondShipBuildingDTO.baseData());
                state.setMyShip(clientModel.getMyShip());
                //Set blocked forecasts
                Map<ShipBoard, Integer> blockedForecasts = convertMap(secondShipBuildingDTO.blockedForecasts());
                for (ShipBoard shipBoard : blockedForecasts.keySet()) {
                    state.getBlockedForecasts()[blockedForecasts.get(shipBoard)] = shipBoard;
                }
                //Set hourglass
                HourglassDTO hourglassDTO = secondShipBuildingDTO.hourglass();
                state.getHourglass().setup(hourglassDTO.flipsLeft(), hourglassDTO.timeLeft(), hourglassDTO.isRunning());
                return state;
            }
            case TestShipBuildingDTO testShipBuildingDTO -> {
                TestShipBuildingState state = new TestShipBuildingState();
                convertBuildingData(state,testShipBuildingDTO.data());
                state.setMyShip(clientModel.getMyShip());
                return state;
            }
        }
    }

    private AdventureState getSimpleState(SimpleStateDTO simpleStateDTO, ShipBoard myShip) {
        ShipBoard shipBoard = convertName(simpleStateDTO.playerName());
        switch (simpleStateDTO.type()) {
            case DECLARE_ENGINE_POWER -> {
                return new DeclareEnginePowerState(myShip, shipBoard);
            }
            case DECLARE_FIRE_POWER -> {
                return new DeclareFirePowerState(myShip, shipBoard);
            }
            case DRAW_CARD -> {
                return new DrawCardState(myShip, shipBoard);
            }
            case GRAB_REWARD -> {
                return new GrabRewardState(myShip, shipBoard);
            }
            case null -> {
                return null;
            }
        }
    }

    private ShipInitializationState getShipInitializationState(ShipInitializationDTO shipInitializationDTO, ShipBoard myShip) {
        return new ShipInitializationState(
                myShip,
                convertMap(shipInitializationDTO.crewTypeToCabins())
        );
    }

    private ShipCorrectionState getShipCorrectionState(ShipCorrectionDTO shipCorrectionDTO, ShipBoard myShip) {
        return new ShipCorrectionState(
                myShip,
                convertCollection(shipCorrectionDTO.validShips(),HashSet::new),
                convertMap(shipCorrectionDTO.shipPieces()),
                shipCorrectionDTO.shouldDiscard());
    }

    private ShipBuildingState getShipBuildingState(ClientModel clientModel) {
        ShipBuildingState gameState;
        gameState = clientModel.getGame().getGameFactory().createShipBuildingState();
        gameState.setMyShip(clientModel.getMyShip());
        return gameState;
    }

    private RemoveGoodsState getRemoveGoodsState(RemoveGoodsDTO removeGoodsDTO, ShipBoard myShip) {
        return new RemoveGoodsState(
                myShip,
                removeGoodsDTO.goodsLoss(),
                convertName(removeGoodsDTO.playerName())
        );
    }

    private RemoveCrewState getRemoveCrewState(RemoveCrewDTO removeCrewDTO, ShipBoard myShip) {
        return new RemoveCrewState(
                myShip,
                removeCrewDTO.crewLoss(),
                convertName(removeCrewDTO.playerName())
        );
    }

    private HandleProjectileState getHandleProjectileState(HandleProjectileDTO handleProjectileDTO, ShipBoard myShip) {
        return new HandleProjectileState(
                myShip,
                convertName(handleProjectileDTO.playerName()),
                new Projectile(handleProjectileDTO.diceRoll(), handleProjectileDTO.direction(), handleProjectileDTO.projectileType()),
                handleProjectileDTO.availablePoints()
        );
    }

    private ChooseShipPieceState getChooseShipPieceState(ChooseShipPieceDTO chooseShipPieceDTO, ShipBoard myShip) {
        return new ChooseShipPieceState(
                myShip,
                chooseShipPieceDTO.shipPieces(),
                convertName(chooseShipPieceDTO.playerName())
        );
    }

    private ChoosePlanetState getChoosePlanetState(ChoosePlanetDTO choosePlanetDTO, ShipBoard myShip) {
        return new ChoosePlanetState(
                myShip,
                convertName(choosePlanetDTO.playerName()),
                choosePlanetDTO.numPlanets()
        );
    }

    private AddGoodsState getAddGoodsState(AddGoodsDTO addGoodsDTO, ShipBoard myShip) {
        return new AddGoodsState(
                myShip,
                addGoodsDTO.goodsBuffer(),
                convertName(addGoodsDTO.playerName())
        );
    }
}
