package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ShipInitializationState extends GameState {
    private final Map<ShipBoard, List<Point>> shipRelevantCabins;
    private final Map<ShipBoard, Point> currentCabins;

    public ShipInitializationState() {
        shipRelevantCabins = new HashMap<>();
        currentCabins = new HashMap<>();
    }

    @Override
    public void setGame(Game game) {
        super.setGame(game);
        for (ShipBoard shipBoard : game.getShipBoards()) {
            List<Point> relevantCabins = new ArrayList<>();
            for (Point p : shipBoard.getCabins().keySet()) {
                if (shipBoard.getCrewTypeOptions(p).size() > 1) {
                    relevantCabins.add(p);
                } else {
                    shipBoard.initializeCabin(p, CrewType.HUMAN);
                }
            }
            if (!relevantCabins.isEmpty()) {
                shipRelevantCabins.put(shipBoard, relevantCabins);
            }
        }
        for (ShipBoard shipBoard : shipRelevantCabins.keySet()) {
            currentCabins.put(shipBoard, shipRelevantCabins.get(shipBoard).removeLast());
        }
    }

    @Override
    public void initializeCabin(ShipBoard shipBoard, CrewType crewType) {
        if (currentCabins.containsKey(shipBoard)) {
            shipBoard.initializeCabin(currentCabins.get(shipBoard), crewType);
            if (!shipRelevantCabins.get(shipBoard).isEmpty()) {
                currentCabins.put(shipBoard, shipRelevantCabins.get(shipBoard).removeLast());
            } else {
                shipRelevantCabins.remove(shipBoard);
                currentCabins.remove(shipBoard);
                tryStateTransition();
            }
        }
    }

    private void tryStateTransition() {
        if (currentCabins.isEmpty()) {
            game.getDeck().initMasterDeck();
            game.setCurrentState(new DrawCardState());
        }
    }

}
