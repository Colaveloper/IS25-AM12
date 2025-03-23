package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import it.polimi.ingsw.galaxytruckers.state.GeneralChoiceState;
import javafx.scene.image.Image;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlanetsCard extends AdventureCard {
    private ShipBoard currentShipBoard;
    private final List<Map<GoodsType, Integer>> planets;
    private final Set<Integer> remainingChoices;
    private final Map<ShipBoard, Integer> planetChoices;
    private final List<ShipBoard> landedShips;
    private final int flightDaysLoss;
    private boolean planetChoiceAllowed;

    public PlanetsCard(Image image, Level level, FlightBoard flightBoard, List<Map<GoodsType, Integer>> planets, int flightDaysLoss) {
        super(image, level, flightBoard);
        this.planets = List.copyOf(planets);
        this.flightDaysLoss = flightDaysLoss;
        this.planetChoiceAllowed = true;
        this.planetChoices = new HashMap<>();
        this.landedShips = new ArrayList<>();
        this.remainingChoices = IntStream.range(0, planets.size())
                .boxed()
                .collect(Collectors.toSet());
    }

    @Override
    public GameState nextStep() {
        if (planetChoiceAllowed) {
            if (currentPlayerIndex >= flightBoard.getShipToPlace().size()) {
                planetChoiceAllowed = false;
                currentPlayerIndex = 0;
                currentShipBoard = null;
                return nextStep();
            }
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            return new GeneralChoiceState(this::choosePlanet, remainingChoices, true);
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

    public void choosePlanet(int index) {
        planetChoices.put(currentShipBoard, index);
        landedShips.add(currentShipBoard);
        remainingChoices.remove(index);
    }

    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }

    public List<Map<GoodsType, Integer>> getPlanets() {
        return planets;
    }

    public Set<Integer> getRemainingChoices() {
        return remainingChoices;
    }

    public Map<ShipBoard, Integer> getPlanetChoices() {
        return planetChoices;
    }

    public List<ShipBoard> getLandedShips() {
        return landedShips;
    }

    public int getFlightDaysLoss() {
        return flightDaysLoss;
    }

    public boolean isPlanetChoiceAllowed() {
        return planetChoiceAllowed;
    }
}
