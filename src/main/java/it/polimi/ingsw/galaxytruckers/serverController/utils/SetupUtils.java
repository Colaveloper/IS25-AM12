package it.polimi.ingsw.galaxytruckers.serverController.utils;

import it.polimi.ingsw.galaxytruckers.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.ShipBoardDTO;

import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for setting up components and ship boards from DTOs in order
 * to load specific scenarios
 */
public class SetupUtils {
    /**
     * Creates a {@link Component} from a {@link ComponentDTO}.
     *
     * @param componentDTO the DTO containing the component's data
     * @return the created {@link Component}
     */
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
            case Activatable activatable -> {
                if (componentDTO.payload().active()) activatable.activate();
            }
            case Engine _ -> {
            }
            case LifeSupport _ -> {
            }
            case Cannon _ -> {
            }
            case Component _ -> {
            }
        }
        return component;
    }

    /**
     * Sets up a {@link ShipBoard} using the data from a {@link ShipBoardDTO}.
     *
     * @param shipBoard    the ship board to set up
     * @param shipBoardDTO the DTO containing the ship board's data
     */
    public static void setupShipBoard(ShipBoard shipBoard, ShipBoardDTO shipBoardDTO) {
        Map<Point, Component> componentMap = shipBoardDTO.componentMap().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> setupComponent(entry.getValue()))
                );
        shipBoard.setup(componentMap, shipBoardDTO.credits(), shipBoardDTO.losses());
    }
}
