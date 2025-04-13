package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty.Penalty;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.List;
import java.util.Optional;

//TODO: fix class implementation
// - make it coherent with other cards and game states
// - find new implementation strategy (not parsed suppliers)
public class CombatZoneCard extends AdventureCard {
    private final List<CombatZoneCheck> checks;
    private final List<Penalty> penalties;
    private int checkIndex;
    ShipBoard penalizedShipBoard;

    public CombatZoneCard(Level level, List<CombatZoneCheck> checks, List<Penalty> penalties) {
        super(level);
        this.checks = checks;
        this.penalties = penalties;
    }

    @Override
    public void initialize(FlightBoard flightBoard) {
        super.initialize(flightBoard);
        checkIndex = 0;
        penalizedShipBoard = null;
    }

    @VisibleForTesting
    public List<CombatZoneCheck> getChecks() {
        return checks;
    }

    @VisibleForTesting
    public List<Penalty> getPenalties() {
        return penalties;
    }

    @Override
    public GameState nextStep() {
        if (penalizedShipBoard == null) {
            if (currentPlayerIndex < flightBoard.getShipToPlace().size()) {
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex++);
                Optional<GameState> availableAction = checks.get(checkIndex).getAvailableAction(currentShipBoard);
                if (availableAction.isPresent()) {
                    return availableAction.get();
                } else {
                    penalizedShipBoard = checks.get(checkIndex).getWeakestPlayer(flightBoard);
                    return nextStep();
                }
            } else {
                penalizedShipBoard = checks.get(checkIndex).getWeakestPlayer(flightBoard);
                return nextStep();
            }
        } else {
            Optional<GameState> penaltyAction = penalties.get(checkIndex).givePenalty(penalizedShipBoard, flightBoard);
            if (penaltyAction.isPresent()) {
                return penaltyAction.get();
            } else {
                penalizedShipBoard = null;
                checkIndex++;
                currentPlayerIndex = 0;
                if (checkIndex >= checks.size()) {
                    return new DrawCardState();
                }
                return nextStep();
            }
        }
    }
}
