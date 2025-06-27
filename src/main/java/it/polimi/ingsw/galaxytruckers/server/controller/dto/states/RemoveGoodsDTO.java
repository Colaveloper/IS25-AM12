package it.polimi.ingsw.galaxytruckers.server.controller.dto.states;

/**
 * DTO for RemoveGoodsState.
 *
 * @param playerName the name of the player who is removing goods
 * @param goodsLoss  the number of goods being removed
 */
public record RemoveGoodsDTO(
        String playerName,
        int goodsLoss
) implements StateDTO, ComplexStateDTO {
}
