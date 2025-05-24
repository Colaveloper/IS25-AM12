package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty.Penalty;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.List;
import java.util.Optional;

public class CombatZoneCard extends AdventureCard {
    private final List<CombatZoneCheck> checks;
    private final List<Penalty> penalties;
    private int checkIndex;
    ShipBoard penalizedShipBoard;

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

    @VisibleForTesting
    public List<CombatZoneCheck> getChecks() {
        return checks;
    }

    @VisibleForTesting
    public List<Penalty> getPenalties() {
        return penalties;
    }

    @Override
    public AdventureState getNextState() {
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
            Optional<AdventureState> penaltyAction = penalties.get(checkIndex).givePenalty(penalizedShipBoard, flightBoard);
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
