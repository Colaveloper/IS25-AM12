package it.polimi.ingsw.galaxytruckers.serverController.dto.states;

public record RemoveGoodsDTO(
        String playerName,
        int goodsLoss
) implements StateDTO, ComplexStateDTO {
}
