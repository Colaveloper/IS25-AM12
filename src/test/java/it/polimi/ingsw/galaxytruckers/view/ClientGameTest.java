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

    @BeforeEach
    void setUp() {

        connectors = Arrays.asList(0, 1, 2, 3);
        component = new Component(connectors, " ▲ ");
        componentMap = new HashMap<>();
        for (int i = 0; i < 7; i++) {       //ship columns
            for (int j = 0; j < 5; j++) {   //ship rows
                componentMap.put(point = new Point(i, j), component);
            }
        }

        //componentMap.remove(point = new Point(2, 2));

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