package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.Dice;
import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.state.GameState;
import javafx.scene.image.Image;

import java.awt.*;

public class SabotageCard extends AdventureCard {

    private static final Dice dice = new Dice() {};


    protected SabotageCard(Image image, Level cardLevel, FlightBoard flightBoard) {
        super(image, cardLevel, flightBoard);
    }

    @Override
    public GameState nextStep() {
        currentShipBoard = flightBoard.getOrderedShips().getFirst();
        for (ShipBoard shipBoard : flightBoard.getOrderedShips()) {
            if (shipBoard.getCrewSize() < currentShipBoard.getCrewSize()) {
                currentShipBoard = shipBoard;
            }
        }

        currentShipBoard.removeComponent(new Point(dice.getAsInt(),dice.getAsInt()));

        return new DrawCardState();
    }
}
