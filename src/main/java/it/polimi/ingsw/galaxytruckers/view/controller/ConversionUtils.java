package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

public class ConversionUtils {
    private final PlayerRegistry playerRegistry;

    public ConversionUtils(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
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
}
