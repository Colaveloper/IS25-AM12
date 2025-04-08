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
    Map<Point, Component> componentMap;
    Point point;

    @BeforeEach
    void setUp() {

        componentMap = new HashMap<>();

        componentMap.put(point = new Point(4,3), component = new Component(Arrays.asList(0, 0, 2, 0), ComponentType.CANNON));

        componentMap.put(point = new Point(4,4), component = new Component(Arrays.asList(2, 1, 2, 0), ComponentType.SHIELD));
        componentMap.put(point = new Point(5,4), component = new Component(Arrays.asList(0, 1, 0, 1), ComponentType.STORAGE));
        componentMap.put(point = new Point(6,4), component = new Component(Arrays.asList(0, 0, 0, 1), ComponentType.CANNON));

        componentMap.put(point = new Point(2,5), component = new Component(Arrays.asList(0, 2, 1, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(3,5), component = new Component(Arrays.asList(0, 3, 0, 2), ComponentType.STORAGE));
        componentMap.put(point = new Point(4,5), component = new Component(Arrays.asList(2, 0, 0, 3), ComponentType.CABIN));
        componentMap.put(point = new Point(5,5), component = new Component(Arrays.asList(0, 1, 2, 3), ComponentType.CABIN));
        componentMap.put(point = new Point(6,5), component = new Component(Arrays.asList(0, 1, 0, 1), ComponentType.CABIN));
        componentMap.put(point = new Point(7,5), component = new Component(Arrays.asList(0, 3, 2, 1), ComponentType.STORAGE));
        componentMap.put(point = new Point(8,5), component = new Component(Arrays.asList(0, 0, 0, 3), ComponentType.CANNON));

        componentMap.put(point = new Point(2,6), component = new Component(Arrays.asList(1, 2, 2, 0), ComponentType.SHIELD));
        componentMap.put(point = new Point(5,6), component = new Component(Arrays.asList(2, 0, 0, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(7,6), component = new Component(Arrays.asList(2, 0, 2, 1), ComponentType.CABIN));

        componentMap.put(point = new Point(7,7), component = new Component(Arrays.asList(2, 1, 0, 0), ComponentType.ENGINE));
        componentMap.put(point = new Point(8,7), component = new Component(Arrays.asList(0, 0, 0, 1), ComponentType.SHIELD));


        shipboard = new Shipboard(componentMap);

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
        game.getDescription().forEach(System.out::println);
        System.out.println("\u001B[31m" + "This is red text" + "\u001B[0m");

    }
}