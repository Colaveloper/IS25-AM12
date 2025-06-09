package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;

import java.util.*;

public class PlanetsCard extends AdventureCard {
    private final List<Map<GoodsType, Integer>> planets;
    private final int flightDaysLoss;
    private boolean planetChoiceAllowed;
    private ShipBoard currentShipBoard;
    private Map<ShipBoard, Integer> planetChoices;
    private List<ShipBoard> landedShips;

    public PlanetsCard(Game game, Level level, List<Map<GoodsType, Integer>> planets, int flightDaysLoss, int id) {
        super(game, level, id);
        this.planets = planets;
        this.flightDaysLoss = flightDaysLoss;
    }

    @Override
    public void initialize() {
        super.initialize();
        this.planetChoiceAllowed = true;
        this.planetChoices = new HashMap<>();
        this.landedShips = new ArrayList<>();
    }

    @Override
    public AdventureState getNextState() {
        if (planetChoiceAllowed) {
            AdventureState state = new ChoosePlanetState(this::choosePlanet, this.planets.size());
            planetChoiceAllowed = false;
            return state;
        } else {
            if (currentShipBoard != null) {
                flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
            }
            if (currentPlayerIndex >= landedShips.size()) {
                return new DrawCardState();
            }
            currentShipBoard = landedShips.get(currentPlayerIndex);
            currentPlayerIndex++;
            return new AddGoodsState(planets.get(planetChoices.get(currentShipBoard)), currentShipBoard);
        }
    }

    void choosePlanet(ShipBoard shipBoard, int index) {
        planetChoices.put(shipBoard, index);
        landedShips.add(shipBoard);
    }

    @VisibleForTesting
    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }

    @VisibleForTesting
    public Map<ShipBoard, Integer> getPlanetChoices() {
        return planetChoices;
    }

    @VisibleForTesting
    public List<ShipBoard> getLandedShips() {
        return landedShips;
    }

    @VisibleForTesting
    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    @VisibleForTesting
    public boolean isPlanetChoiceAllowed() {
        return planetChoiceAllowed;
    }
}
