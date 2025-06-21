package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;

import java.util.*;
import java.util.function.Supplier;

public class ConversionUtils {
    private final PlayerRegistry playerRegistry;

    public ConversionUtils(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    public <T> Map<Player,T> convertPlayerMap(Map<String,T> map) {
        Map<Player, T> convertedMap = new HashMap<>();
        for (String name : map.keySet()) {
            convertedMap.put(playerRegistry.getByNickname(name), map.get(name));
        }
        return convertedMap;
    }

    public <T> Map<ShipBoard, T> convertMap(Map<String, T> map) {
        Map<ShipBoard, T> convertedMap = new HashMap<>();
        for (String name : map.keySet()) {
            convertedMap.put(playerRegistry.getByNickname(name).getShipBoard(), map.get(name));
        }
        return convertedMap;
    }

    public <T extends Collection<String>, R extends Collection<ShipBoard>> R convertCollection(T nicknames, Supplier<R> supplier) {
        return nicknames.stream()
                .map(x -> playerRegistry.getByNickname(x).getShipBoard())
                .collect(supplier,R::add,R::addAll);
    }

    public ShipBoard convertName(String nickname) {
        return playerRegistry.getByNickname(nickname).getShipBoard();
    }

    public ShipBoard[] convertArray(String[] nicknames) {
        return Arrays.stream(nicknames).map(this::convertName).toArray(ShipBoard[]::new);
    }

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
