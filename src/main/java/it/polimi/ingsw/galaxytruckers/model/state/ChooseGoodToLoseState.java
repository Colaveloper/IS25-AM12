package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: this state is deprecated, should be removed once CombatZoneCard is implemented
public class ChooseGoodToLoseState extends GameState {
    private final GoodsType mostValuableGoodType; // might be null!
    private final Set<Point> availablePositions;
    private final ShipBoard shipBoard;

    // TODO: automatize removal when there's no choice!

    public ChooseGoodToLoseState(ShipBoard shipBoard) {
        mostValuableGoodType = shipBoard.getCargoHolds().values().stream()
                .flatMap(c -> c.getGoods().keySet().stream())
                .max(Comparator.comparingInt(GoodsType::getValue))
                .orElse(null);

        this.availablePositions = shipBoard.getCargoHolds().entrySet().stream()
                .filter(e ->
                        e.getValue().getGoods().containsKey(mostValuableGoodType)
                )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        this.shipBoard = shipBoard;
    }

//    @Override
//    public void chooseGoodToLose(Point position)  {
//        if (availablePositions.contains(position)) {
//            adventureCard.loseGoods();
//            shipBoard.removeGoods(position, mostValuableGoodType, 1);
//        } else {
//            throw new IllegalArgumentException(
//                    "You are not allowed to remove goods from this position"
//            );
//        }
//    }
//
//    @Override
//    public GameState getNextState() {
//        return null;
//    }
}
