package it.polimi.ingsw.galaxytruckers.server.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.server.model.*;
import it.polimi.ingsw.galaxytruckers.server.model.state.*;
import it.polimi.ingsw.galaxytruckers.shared.enums.GameColor;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.shared.enums.Level;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SmugglersCardTest {

    Game game;
    SmugglersCard smugglersCard;
    FlightBoard flightBoard;
    FlightBoard flightBoardOfLosers;

    SecondShipBoard ship1;
    SecondShipBoard ship2;
    SecondShipBoard ship3;
    List<ShipBoard> ships;
    List<ShipBoard> loserShips;
    Map<ShipBoard, Integer> shipPlaces;
    Map<ShipBoard, Integer> loserShipPlaces;
    GameState testState;
    Map<GoodsType, Integer> goodsWon;


    List<ShipBoard> displacedShips;
//    boolean creditGained;


    @BeforeEach
    void setUp() {

        displacedShips = new ArrayList<>();

        ships = new ArrayList<>();
        loserShips = new ArrayList<>();

        goodsWon = new HashMap<>();

        ship1 = new SecondShipBoardForTesting(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 0;
            }

        };

        ship2 = new SecondShipBoardForTesting(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 4;
            }

        };

        ship3 = new SecondShipBoardForTesting(GameColor.RED) {
            @Override
            public int getFirePower() {
                return 2;
            }

        };

        ships.add(ship1);
        ships.add(ship2);

        loserShips.add(ship3);
        loserShips.add(ship1);

        shipPlaces = new HashMap<>();
        for (int i = 0; i < ships.size(); i++) {
            shipPlaces.put(ships.get(i), 10-i);
        }

        loserShipPlaces = new HashMap<>();
        for (int i = 0; i < loserShips.size(); i++) {
            loserShipPlaces.put(loserShips.get(i), 10-i);
        }

        flightBoard = new FlightBoard(new GameEventListenerForTesting()) {
            @Override
            protected int getLoopLength() {
                return 0;
            }

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
                displacedShips.add(shipBoard);
            }

            @Override
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }
        };

        flightBoardOfLosers = new FlightBoard(new GameEventListenerForTesting()) {
            @Override
            protected int getLoopLength() {
                return 0;
            }

            @Override
            public Map<ShipBoard, Integer> getShipToPlace() {
                return loserShipPlaces;
            }

            @Override
            public List<ShipBoard> getOrderedShips() {
                return loserShips;
            }

            @Override
            public void displaceShip(ShipBoard shipBoard, int displacement) {
                displacedShips.add(shipBoard);
            }

            @Override
            public void placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
            }
        };
        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoard;
            }
        };

        smugglersCard = new SmugglersCard(game, Level.SECOND, 1, 1, goodsWon, 1,1);
        smugglersCard.initialize();
    }




    @Test
    void secondPlayerHasChooseState() {
        smugglersCard.getNextState();
        smugglersCard.getNextState();
        smugglersCard.getNextState();
        testState = smugglersCard.getNextState();
        assertInstanceOf(GrabRewardState.class, testState);
    }

    @Test
    void getNextStateIsRemoveGoodsState() {
        smugglersCard.getNextState();
        testState = smugglersCard.getNextState();
        assertInstanceOf(RemoveGoodsState.class, testState);
    }

    @Test
    void eachPlayerHasDeclareFirePowerState() {
        testState = smugglersCard.getNextState();
        assertInstanceOf(DeclareFirePowerState.class, testState);
        smugglersCard.getNextState();//removeGoodsState
        testState = smugglersCard.getNextState();
        assertInstanceOf(DeclareFirePowerState.class, testState);
    }

    @Test
    void winnerAddsGoodsAndCardEnds() {
        smugglersCard.getNextState();//activate
        smugglersCard.getNextState();//lose goods
        smugglersCard.getNextState();//activate
        smugglersCard.getNextState();//choose to get goods = true
        testState = smugglersCard.getNextState();//add goods
        assertInstanceOf(AddGoodsState.class, testState);
        testState = smugglersCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void noneWins() {
        game = new GameStub(Level.SECOND) {
            @Override public FlightBoard getFlightBoard() {
                return flightBoardOfLosers;
            }
        };
        smugglersCard = new SmugglersCard(game, Level.SECOND, 1, 1, goodsWon, 1,1);
        smugglersCard.initialize();
        smugglersCard.getNextState();//activate (but doesn't lose)
        smugglersCard.getNextState();//activate
        smugglersCard.getNextState();//lose goods
        testState = smugglersCard.getNextState();
        assertInstanceOf(DrawCardState.class, testState);
    }

    @Test
    void chooseToGetGoods() {
        smugglersCard.getNextState();//when state is GrabRewardState
        smugglersCard.getNextState();
        smugglersCard.getNextState();
        testState = smugglersCard.getNextState();
        assertInstanceOf(GrabRewardState.class, testState);

        smugglersCard.getReward();
        assertTrue(displacedShips.contains(ship2));
        assertFalse(displacedShips.contains(ship1));
    }
}