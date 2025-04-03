package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.ActivateState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.GameState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OpenSpaceCardTest {
    OpenSpaceCard openSpaceCard;
    List<ShipBoard> ships;


    @BeforeEach
    void setUp() {
        ships = new ArrayList<>();
        ships.add(new SecondShipBoard(Colors.BLUE));
        ships.add(new SecondShipBoard(Colors.RED));
        FlightBoard flightBoardStub = new FlightBoard(null) {
            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return false;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Image getImage() {
                return null;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {

            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return Map.of(ships.get(0),0,ships.get(1),0);
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }
        };
        openSpaceCard = new OpenSpaceCard(null, Level.TEST, flightBoardStub);
    }

    @Test
    void nextStepWhenThereArePlayersLeftReturnsActivate() {
        GameState testState = openSpaceCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
        testState = openSpaceCard.nextStep();
        assertInstanceOf(ActivateState.class, testState);
    }

    @Test
    void nextStepWhenPlayersAreOverReturnsDrawCard() {
        openSpaceCard.nextStep();
        openSpaceCard.nextStep();
        GameState testState = openSpaceCard.nextStep();

        assertInstanceOf(DrawCardState.class, testState);
    }
}