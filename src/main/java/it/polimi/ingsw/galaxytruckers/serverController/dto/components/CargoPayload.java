package it.polimi.ingsw.galaxytruckers.serverController.dto.components;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.util.Map;

/**
 * Represents the payload for a cargo hold component in the Galaxy Truckers game.
 * This record contains a map of goods types and their quantities stored in the cargo hold.
 *
 * @param goods a map where keys are goods types and values are their respective quantities
 */
public record CargoPayload(Map<GoodsType, Integer> goods) implements ComponentPayload {
}
