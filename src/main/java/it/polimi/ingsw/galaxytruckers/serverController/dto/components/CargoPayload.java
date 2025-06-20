package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.Map;

public record CargoPayload(Map<GoodsType, Integer> goods) implements ComponentPayload {
}
