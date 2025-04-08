package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.GrabRewardState;
import it.polimi.ingsw.galaxytruckers.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.model.state.RemoveCrewState;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCardTest {
    AbandonedShipCard card;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;

    // card attributes
    Image image = null;
    Level level = Level.SECOND;
    int creditPrize = 4;
    int requiredCrew = 3;
    int flightDaysLost = 1;

    void setup(ShipBoard ship1, ShipBoard ship2) {
        // setting up ships and flightboard
        ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }
        FlightBoard flightBoard = new FlightBoard(null) {
            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return shipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return ships;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                testShip = shipBoard;
                testDisplacement = displacement;
            }

            @Override
            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
                return true;
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Image getImage() {
                return null;
            }
        };

        // creating card
        card = new AbandonedShipCard(image, level, creditPrize,requiredCrew,flightDaysLost);
        card.initialize(flightBoard);
    }

    @Test
    void nextStepReturnChoiceStateIfAtLeastOneShipHasEnoughCrew(){
        // ship 1 does not have enough crew
        ShipBoard ship1 = new SecondShipBoard(Colors.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };

        // ship 2 does have enough crew
        ShipBoard ship2 = new SecondShipBoard(Colors.RED){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };
        setup(ship1, ship2);
        assertInstanceOf(GrabRewardState.class, card.nextStep());
    }

    @Test
    void nextStepReturnsDrawCardStateIfNoPlayersHasEnoughCrew(){
        // ship 1 does not have enough crew
        ShipBoard ship1 = new SecondShipBoard(Colors.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };

        // ship 2 does not have enough crew
        ShipBoard ship2 = new SecondShipBoard(Colors.RED){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };
        setup(ship1, ship2);
        assertInstanceOf(DrawCardState.class, card.nextStep());
    }

    @Test
    void nextStepReturnsRemoveCrewStateIfShipAcceptsCard(){
        // ship 1 does have enough crew
        ShipBoard ship1 = new SecondShipBoard(Colors.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };

        // ship 2 also has enough crew
        ShipBoard ship2 = new SecondShipBoard(Colors.RED){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };
        setup(ship1, ship2);
        card.nextStep();
        card.getReward();
        assertTrue(card.getAccepted());
        assertInstanceOf(RemoveCrewState.class, card.nextStep());
        assertInstanceOf(DrawCardState.class, card.nextStep());
    }

//    @Test
//    void chooseFalse(){
//        // ship 1 does have enough crew
//        ShipBoard ship1 = new SecondShipBoard(Colors.BLUE){
//            @Override
//            public int getCrewSize() {return requiredCrew;}
//        };
//
//        // ship 2 also has enough crew
//        ShipBoard ship2 = new SecondShipBoard(Colors.RED){
//            @Override
//            public int getCrewSize() {return requiredCrew;}
//        };
//        setup(ship1,ship2);
//        card.nextStep();
//        card.getReward();
//        assertFalse(card.getAccepted());
//    }
}