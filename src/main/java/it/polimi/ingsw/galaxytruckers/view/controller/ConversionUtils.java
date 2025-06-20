package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;
import it.polimi.ingsw.galaxytruckers.view.model.state.ShipBuildingState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.BuildingDataDTO;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

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

    public ShipBoard convert(String nickname) {
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
}
