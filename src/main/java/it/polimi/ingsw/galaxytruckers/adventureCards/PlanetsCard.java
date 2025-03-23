package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.AddGoodsState;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import it.polimi.ingsw.galaxytruckers.state.GeneralChoiceState;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

public class PlanetsCard extends AdventureCard {
    ShipBoard currentShipBoard;
    List<Map<GoodsType, Integer>> planets;
    Map<ShipBoard, Integer> planetChoices;
    List<ShipBoard> landedShips;
    int flightDaysLoss;
    boolean choosePlanet;

    public PlanetsCard(Image image, Level level, List<Map<GoodsType, Integer>> planets, int flightDaysLoss) {
        super(image, level);
        this.planets = planets;
        this.flightDaysLoss = flightDaysLoss;
        this.choosePlanet = true;
    }

    @Override
    public GameState nextStep() {
        if (choosePlanet) {
            if (currentShipBoard == null) {
                currentShipBoard = flightBoard.getOrderedShips().getFirst();
            } else {
                currentPlayerIndex++;
                if (currentPlayerIndex >= flightBoard.getShipToPlace().size()) {
                    choosePlanet = false;
                    currentPlayerIndex = 0;
                    currentShipBoard = null;
                    return nextStep();
                }
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            }
            return new GeneralChoiceState(choice -> {
                if (choice > 0 && choice <= planetChoices.size()) {
                    choosePlanet(choice-1);
                }
            }, planetChoices.size()+1);
        } else {
            if (currentShipBoard == null) {
                currentShipBoard = landedShips.getFirst();
            } else {
                flightBoard.displaceShip(currentShipBoard, -flightDaysLoss);
                currentPlayerIndex++;
                if (currentPlayerIndex >= landedShips.size()) {
                    return new DrawCardState();
                }
                currentShipBoard = landedShips.get(currentPlayerIndex);
            }
            return new AddGoodsState(planets.get(planetChoices.get(currentShipBoard)), currentShipBoard);
        }
    }

    public void choosePlanet(int index) {
        planetChoices.put(currentShipBoard, index);
        landedShips.add(currentShipBoard);
    }
}
