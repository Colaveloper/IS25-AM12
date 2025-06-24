package it.polimi.ingsw.galaxytruckers.serverController.events.types;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.awt.*;

/**
 * Event that represents an update to goods on a player's ship.
 * This event is dispatched when goods are added to or removed from a specific location on a ship board.
 *
 * @param playerName The name of the player whose ship's goods are being updated
 * @param point The coordinates on the ship board where the goods update occurs
 * @param goodsType The type of goods being added or removed
 * @param add Whether goods are being added (true) or removed (false)
 */
public record GoodsUpdateEvent(String playerName, Point point, GoodsType goodsType, boolean add) implements LobbyEvent {
    /**
     * Creates a GoodsUpdateEvent from a ship board and goods information.
     *
     * @param shipBoard The ship board on which the goods are being updated
     * @param point The coordinates on the ship board where the goods update occurs
     * @param goodsType The type of goods being added or removed
     * @param add Whether goods are being added (true) or removed (false)
     * @return A new GoodsUpdateEvent with the player's name extracted from the ship board
     */
    public static GoodsUpdateEvent from(ShipBoard shipBoard, Point point, GoodsType goodsType, boolean add) {
        return new GoodsUpdateEvent(
                Player.getPlayer(shipBoard).getNickname(),
                point,
                goodsType,
                add
        );
    }

}
