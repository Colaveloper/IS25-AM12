package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.server.model.adventureCards.penalty.Penalty;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;

import java.util.List;
import java.util.Optional;

/**
 * Represents a combat zone card in the game.
 */
public class CombatZoneCard extends AdventureCard {
    private final List<CombatZoneCheck> checks;
    private final List<Penalty> penalties;
    private int checkIndex;
    ShipBoard penalizedShipBoard;

    /**
     * Constructs a CombatZoneCard.
     *
     * @param game      the game instance
     * @param level     the adventure card level
     * @param checks    the list of combat zone checks to perform
     * @param penalties the list of penalties to inflict
     * @param id        the unique card identifier
     */
    public CombatZoneCard(Game game, Level level, List<CombatZoneCheck> checks, List<Penalty> penalties, int id) {
        super(game, level, id);
        this.checks = checks;
        this.penalties = penalties;
    }

    @Override
    public void initialize() {
        super.initialize();
        checkIndex = 0;
        penalizedShipBoard = null;
    }

    /**
     * For testing purposes only.
     *
     * @return a list of combat zone checks to be performed
     */
    @VisibleForTesting
    public List<CombatZoneCheck> getChecks() {
        return checks;
    }

    /**
     * For testing purposes only.
     *
     * @return a list of penalties to be inflicted
     */
    @VisibleForTesting
    public List<Penalty> getPenalties() {
        return penalties;
    }

    @Override
    public AdventureState getNextState() {
        if (flightBoard.getShipToPlace().size() <= 1) return new DrawCardState();
        if (penalizedShipBoard == null) {
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex++);
                Optional<AdventureState> availableAction = checks.get(checkIndex).getAvailableAction(currentShipBoard);
                if (availableAction.isPresent()) {
                    return availableAction.get();
                } else {
                    penalizedShipBoard = checks.get(checkIndex).getWeakestPlayer(flightBoard);
                    return getNextState();
                }
            } else {
                penalizedShipBoard = checks.get(checkIndex).getWeakestPlayer(flightBoard);
                flightBoard.getShipToPlace().keySet().forEach(ShipBoard::deactivateAll);
                return getNextState();
            }
        } else {
            Optional<AdventureState> penaltyAction = penalties.get(checkIndex).inflictPenalty(penalizedShipBoard, flightBoard);
            if (penaltyAction.isPresent()) {
                return penaltyAction.get();
            } else {
                penalizedShipBoard = null;
                checkIndex++;
                currentPlayerIndex = 0;
                if (checkIndex >= checks.size()) {
                    return new DrawCardState();
                }
                return getNextState();
            }
        }
    }
}
