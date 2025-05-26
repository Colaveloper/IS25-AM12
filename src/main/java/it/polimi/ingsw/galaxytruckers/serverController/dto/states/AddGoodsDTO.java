package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.Map;

public record AddGoodsDTO(String playerName, Map<GoodsType, Integer> goodsBuffer) implements StateDTO {
}
