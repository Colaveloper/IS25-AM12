package it.polimi.ingsw.galaxytruckers.serverController.utils;

import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ShipBoardDTO;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class SetupUtils {
    public static Component setupComponent(ComponentDTO componentDTO) {
        ComponentRegistry componentRegistry = ComponentRegistry.getInstance();
        Component component = componentRegistry.getComponentById(componentDTO.id());
        component.setOrientation(componentDTO.orientation());
        switch ((ComponentInterface) component) {
            case Battery battery -> {
                battery.setNumBatteries(componentDTO.payload().numBatteries());
            }
            case Cabin cabin -> {
                cabin.setNumResidents(componentDTO.payload().numResidents());
                cabin.setCrewType(componentDTO.payload().crewType());
            }
            case CargoHold cargoHold -> {
                cargoHold.setGoods(componentDTO.payload().goods());
            }
            case Engine _ -> {}
            case LifeSupport _ -> {}
            case Activatable _ -> {}
            case Cannon _ -> {}
            case Component _ -> {}
        }
        return component;
    }

    public static void setupShipBoard(ShipBoard shipBoard, ShipBoardDTO shipBoardDTO) {
        Map<Point, Component> componentMap = shipBoardDTO.componentMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> setupComponent(entry.getValue()))
                );
        shipBoard.setup(componentMap, shipBoardDTO.credits(), shipBoardDTO.losses());
    }
}
