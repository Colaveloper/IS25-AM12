package it.polimi.ingsw.galaxytruckers.server.controller.utils;

import it.polimi.ingsw.galaxytruckers.server.model.ComponentRegistry;
import it.polimi.ingsw.galaxytruckers.server.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ComponentDTO;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SetupUtilsTest {

    @Test
    void setupComponent() {
        List<Component> components = new ArrayList<>();
        List<Integer> ids = List.of(60, 125, 91, 148); //IDs of Components to test
        for (int id : ids) {
            Component component = ComponentRegistry.getInstance().getComponentById(id);  //ID of a Component
            component.setOrientation(Direction.LEFT);
            ComponentDTO componentDTO = DtoConverter.getComponent(component);
            Component result = SetupUtils.setupComponent(componentDTO);
            assertNotNull(result);
            assertEquals(component.getId(), result.getId());
            assertEquals(component.getOrientation(), result.getOrientation());
        }
    }

    @Test
    void setupBattery() {
        Battery battery = (Battery) ComponentRegistry.getInstance().getComponentById(17);  //ID of a Battery
        ComponentDTO batteryDTO = DtoConverter.getComponent(battery);
        Component result = SetupUtils.setupComponent(batteryDTO);
        assertInstanceOf(Battery.class, result);
        assertEquals(battery.getNumBatteries(), ((Battery) result).getNumBatteries());
    }

    @Test
    void setupCabin() {
        Cabin component = (Cabin) ComponentRegistry.getInstance().getComponentById(51);  //ID of a Cabin
        component.initialize(CrewType.HUMAN);
        ComponentDTO componentDTO = DtoConverter.getComponent(component);
        Component result = SetupUtils.setupComponent(componentDTO);
        assertInstanceOf(Cabin.class, result);
        assertEquals(component.getNumResidents(), ((Cabin) result).getNumResidents());
        assertEquals(component.getCrewType(), ((Cabin) result).getCrewType());
    }

    @Test
    void setupCargoHold() {
        CargoHold cargoHold = (CargoHold) ComponentRegistry.getInstance().getComponentById(20);  //ID of a CargoHold
        cargoHold.addGoods(GoodsType.YELLOW, 1);
        ComponentDTO cargoHoldDTO = DtoConverter.getComponent(cargoHold);
        Component result = SetupUtils.setupComponent(cargoHoldDTO);
        assertInstanceOf(CargoHold.class, result);
        assertEquals(cargoHold.getGoods(), ((CargoHold) result).getGoods());
    }

    @Test
    void setupActivatable() {
        Activatable activatable = (Activatable) ComponentRegistry.getInstance().getComponentById(136);  //ID of an Activatable
        ComponentDTO activatableDTO = DtoConverter.getComponent(activatable);
        Component result = SetupUtils.setupComponent(activatableDTO);
        assertInstanceOf(Activatable.class, result);
        assertFalse(((Activatable) result).isActive());
        activatable.activate();
        activatableDTO = DtoConverter.getComponent(activatable);
        result = SetupUtils.setupComponent(activatableDTO);
        assertTrue(((Activatable) result).isActive());
    }

    @Test
    void setupShipBoard() {
        ShipBoard shipBoard = new SecondShipBoardForTesting();
        ShipBoardDTO shipBoardDTO = DtoConverter.getShipBoard(shipBoard);
        SetupUtils.setupShipBoard(shipBoard, shipBoardDTO);
        assertEquals(shipBoardDTO.componentMap().size(), shipBoard.getComponentMap().size());
        assertFalse(shipBoard.getLastComponent().isPresent());
        assertFalse(shipBoard.getLastPosition().isPresent());
    }
}