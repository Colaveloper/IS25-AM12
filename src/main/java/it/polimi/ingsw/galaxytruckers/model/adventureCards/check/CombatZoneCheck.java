package it.polimi.ingsw.galaxytruckers.model.adventureCards.check;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;

import java.util.Optional;

/**
 * Interface for determining the weakest player when playing a combat-zone card.
 * It also specifies what state, if any, the player must be brought to before making the evaluation.
 */
public interface CombatZoneCheck {
    /**
     * Returns the weakest player based on the criteria defined in the implementing class.
     *
     * @param flightBoard the current flight board containing all players' ship boards
     * @return the ShipBoard of the weakest player
     */
    ShipBoard getWeakestPlayer(FlightBoard flightBoard);

    /**
     * Returns an optional AdventureState that the player must be in before the weakest player can be determined.
     * If no specific state is required, it returns an empty Optional.
     *
     * @param shipBoard the ShipBoard of the player being evaluated
     * @return an Optional containing the AdventureState if required, or empty if not
     */
    Optional<AdventureState> getAvailableAction(ShipBoard shipBoard);
}
