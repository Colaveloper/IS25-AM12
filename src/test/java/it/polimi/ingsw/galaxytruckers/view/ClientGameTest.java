package it.polimi.ingsw.galaxytruckers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

class ClientGameTest {
    ClientGameModel game;
    FlightBoard flightBoard;
    Shipboard shipboard;


    Component component;
    List<Integer> connectors;
    Map<Point, Component> componentMap;
    Point point;
    int rows = 5;
    int columns = 7;

    @BeforeEach
    void setUp() {

        connectors = Arrays.asList(0, 1, 2, 3);
//        component = new Component(connectors, ComponentType.CANNON);
        componentMap = new HashMap<>();
//        for (int i = 0; i < columns; i++) {       //ship columns
//            for (int j = 0; j < rows; j++) {   //ship rows
//                componentMap.put(point = new Point(i, j), component);
//            }
//        }


        componentMap.put(point = new Point(2,0), component = new Component(connectors = Arrays.asList(0, 0, 2, 0), ComponentType.CANNON));


        componentMap.put(point = new Point(2,1), component = new Component(connectors = Arrays.asList(2, 1, 2, 0), ComponentType.SHIELD));
        componentMap.put(point = new Point(3,1), component = new Component(connectors = Arrays.asList(0, 1, 0, 1), ComponentType.STORAGE));
        componentMap.put(point = new Point(4,1), component = new Component(connectors = Arrays.asList(0, 0, 0, 1), ComponentType.CANNON));

        componentMap.put(point = new Point(0,2), component = new Component(connectors = Arrays.asList(0, 2, 1, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(1,2), component = new Component(connectors = Arrays.asList(0, 3, 0, 2), ComponentType.STORAGE));
        componentMap.put(point = new Point(2,2), component = new Component(connectors = Arrays.asList(2, 0, 0, 3), ComponentType.CABIN));
        componentMap.put(point = new Point(3,2), component = new Component(connectors = Arrays.asList(0, 1, 2, 3), ComponentType.CABIN));
        componentMap.put(point = new Point(4,2), component = new Component(connectors = Arrays.asList(0, 1, 0, 1), ComponentType.CABIN));
        componentMap.put(point = new Point(5,2), component = new Component(connectors = Arrays.asList(0, 3, 2, 1), ComponentType.STORAGE));
        componentMap.put(point = new Point(6,2), component = new Component(connectors = Arrays.asList(0, 0, 0, 3), ComponentType.CANNON));


        componentMap.put(point = new Point(0,3), component = new Component(connectors = Arrays.asList(1, 2, 2, 0), ComponentType.SHIELD));
        componentMap.put(point = new Point(3,3), component = new Component(connectors = Arrays.asList(2, 0, 0, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(5,3), component = new Component(connectors = Arrays.asList(2, 0, 2, 1), ComponentType.CABIN));

        componentMap.put(point = new Point(5,4), component = new Component(connectors = Arrays.asList(2, 1, 0, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(6,4), component = new Component(connectors = Arrays.asList(0, 0, 0, 1), ComponentType.SHIELD));


        shipboard = new Shipboard();
        //shipboard.setComponentMap(componentMap);



        flightBoard = new FlightBoard();
        flightBoard.setLoopLength(10);
        flightBoard.setColorToPlace(Map.of('♠', 2, '♥', 5, '♣', 8));
        flightBoard.setStartingPositionLeft(List.of(6));

        game = new ClientGameModel();
        game.setCurrentPlayerNickname("Gino");
        game.setFlightBoard(flightBoard);
        game.setPlayerToShip(Map.of("Paola", shipboard));
    }

    @Test
    public void printGame() {
        shipboard.setComponentMap(componentMap);
        System.out.println(game.getDescription());
    }
}