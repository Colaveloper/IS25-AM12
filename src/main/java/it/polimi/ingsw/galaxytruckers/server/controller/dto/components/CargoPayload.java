package it.polimi.ingsw.galaxytruckers.server.controller.dto.components;

import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;

import java.util.Map;

/**
 * Represents the payload for a cargo hold component in the Galaxy Truckers game.
 * This record contains a map of goods types and their quantities stored in the cargo hold.
 *
 * @param goods a map where keys are goods types and values are their respective quantities
 */
public record CargoPayload(Map<GoodsType, Integer> goods) implements ComponentPayload {
}
