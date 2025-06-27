package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.state.GrabRewardState;
import it.polimi.ingsw.galaxytruckers.server.model.state.DrawCardState;
import it.polimi.ingsw.galaxytruckers.server.model.state.RemoveCrewState;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbandonedShipCardTest {
    AbandonedShipCard card;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;
    Game game;

    // card attributes
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
        FlightBoard flightBoard = new FlightBoard(new GameEventListenerForTesting()) {
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
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }

            @Override
            protected int getLoopLength() {
                return 0;
            }

        };

        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        // creating card
        card = new AbandonedShipCard(game, Level.SECOND, creditPrize,requiredCrew,flightDaysLost, 1);
        card.initialize();
    }

    @Test
    void getNextStateReturnChoiceStateIfAtLeastOneShipHasEnoughCrew(){
        // ship 1 does not have enough crew
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };

        // ship 2 does have enough crew
        ShipBoard ship2 = new SecondShipBoardForTesting(GameColor.RED){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };
        setup(ship1, ship2);
        assertInstanceOf(GrabRewardState.class, card.getNextState());
    }

    @Test
    void getNextStateReturnsDrawCardStateIfNoPlayersHasEnoughCrew(){
        // ship 1 does not have enough crew
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };

        // ship 2 does not have enough crew
        ShipBoard ship2 = new SecondShipBoardForTesting(GameColor.RED){
            @Override
            public int getCrewSize() {return requiredCrew - 1;}
        };
        setup(ship1, ship2);
        assertInstanceOf(DrawCardState.class, card.getNextState());
    }

    @Test
    void getNextStateReturnsRemoveCrewStateIfShipAcceptsCard(){
        // ship 1 does have enough crew
        ShipBoard ship1 = new SecondShipBoardForTesting(GameColor.BLUE){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };

        // ship 2 also has enough crew
        ShipBoard ship2 = new SecondShipBoardForTesting(GameColor.RED){
            @Override
            public int getCrewSize() {return requiredCrew;}
        };
        setup(ship1, ship2);
        card.getNextState();
        card.getReward();
        assertTrue(card.getAccepted());
        assertInstanceOf(RemoveCrewState.class, card.getNextState());
        assertInstanceOf(DrawCardState.class, card.getNextState());
    }
}