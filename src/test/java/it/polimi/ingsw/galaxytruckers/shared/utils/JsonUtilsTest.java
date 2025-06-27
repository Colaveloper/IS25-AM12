package it.polimi.ingsw.galaxytruckers.shared.utils;

import com.fasterxml.jackson.databind.JsonNode;
import it.polimi.ingsw.galaxytruckers.server.model.SecondShipBoardForTesting;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CargoHold;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.server.controller.dto.ShipBoardDTO;
import it.polimi.ingsw.galaxytruckers.shared.enums.Direction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {
    Point point = new Point(6,7);
    Direction direction = Direction.LEFT;
    ShipBoard shipBoard;
    ShipBoardDTO shipBoardDTO;
    GoodsType goodsType1 = GoodsType.YELLOW;
    GoodsType goodsType2 = GoodsType.GREEN;
    CrewType crewType = CrewType.PURPLE;

    @BeforeEach
    void setUp() {
        shipBoard = new SecondShipBoardForTesting();
        shipBoard.addWeldedComponent(new CargoHold(3), point, direction);
        shipBoard.placeGoods(point, goodsType1,1);
        shipBoard.placeGoods(point, goodsType2,1);
        shipBoard.initializeCabin(shipBoard.getCenter(),crewType);
        shipBoardDTO = DtoConverter.getShipBoard(shipBoard);
    }

    @Test
    void serializeShipBoardDTO() {
        assertDoesNotThrow(() -> {
            JsonUtils.serializeShipBoardDTO(shipBoardDTO);
        });
    }

    @Test
    void deserializeShipBoardDTO() {
        JsonNode serializedNode = JsonUtils.serializeShipBoardDTO(shipBoardDTO);
        assertEquals(shipBoardDTO, JsonUtils.deserializeShipBoardDTO(serializedNode));
    }
}