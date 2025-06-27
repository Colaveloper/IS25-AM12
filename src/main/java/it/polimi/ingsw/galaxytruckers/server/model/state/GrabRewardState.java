package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;

/**
 * Represents the state of the game where a player can grab a reward.
 * This state is part of the adventure phase where players can collect rewards
 * after completing certain tasks or challenges.
 */
public final class GrabRewardState extends AdventureState implements GameStateInterface {
    private final Runnable rewardMethod;
    private final ShipBoard shipBoard;

    public GrabRewardState(ShipBoard shipBoard, Runnable rewardMethod) {
        this.rewardMethod = rewardMethod;
        this.shipBoard = shipBoard;
    }

    /**
     * If it's the player's turn, it skips the current state without
     * grabbing the reward.
     * @param shipBoard the ship board of the player who wants to skip
     */
    @Override
    public synchronized void skip(ShipBoard shipBoard) {
        if (!expired && this.shipBoard.equals(shipBoard)) {
            getNextState();
        }
    }

    /**
     * Grabs the reward for the current ship board.
     *
     * @param shipBoard the ship board acquiring the reward
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void grabReward(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        rewardMethod.run();
        getNextState();
    }

    /**
     * Advances to the next state of the game without grabbing the reward.
     *
     * @param shipBoard the ship board of the player that requested the action
     * @throws IllegalStateException if it's not the player's turn
     */
    @Override
    public synchronized void goNext(ShipBoard shipBoard) {
        if (!this.shipBoard.equals(shipBoard)) {
            throw new IllegalStateException("It's not your turn");
        }
        checkIfExpired();
        getNextState();
    }

    /**
     * @return the ship board of the player whose turn it is.
     */
    public synchronized ShipBoard getShipBoard() {
        return shipBoard;
    }
}
