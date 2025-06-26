package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.Map;

/**
 * DTO for AddGoodsState.
 *
 * @param playerName  the name of the player who is adding goods
 * @param goodsBuffer the map of goods types and their quantities to be added
 */
public record AddGoodsDTO(String playerName, Map<GoodsType, Integer> goodsBuffer) implements StateDTO, ComplexStateDTO {
}
