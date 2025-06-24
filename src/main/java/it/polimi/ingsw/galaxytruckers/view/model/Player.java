package it.polimi.ingsw.galaxytruckers.view.model;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

/**
 * Represents a player in the Galaxy Truckers game.
 * This class maintains the player's identity through their nickname
 * and their associated ship board for gameplay.
 */
public class Player {
    /** The unique nickname that identifies this player */
    private final String nickname;

    /** The ship board associated with this player */
    private ShipBoard shipBoard;

    /**
     * Creates a new player with the specified nickname.
     * The ship board is initially null and must be set separately.
     *
     * @param nickname The unique nickname for this player
     */
    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the player's nickname.
     *
     * @return The player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Gets the ship board associated with this player.
     *
     * @return The player's ship board
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    /**
     * Sets the ship board for this player.
     * This associates the player with a specific ship during gameplay.
     *
     * @param shipBoard The ship board to assign to this player
     */
    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }
}
