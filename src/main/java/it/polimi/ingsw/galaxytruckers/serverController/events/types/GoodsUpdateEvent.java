package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;

import java.awt.*;

/**
 * Event representing an update to the goods on a player's ship.
 *
 * @param playerName the name of the player whose goods are updated
 * @param point the location on the ship where the goods are updated
 * @param goodsType the type of goods being updated
 * @param add true if goods are being added, false if removed
 */
public record GoodsUpdateEvent(String playerName, Point point, GoodsType goodsType, boolean add) implements LobbyEvent {
}
