package it.polimi.ingsw.galaxytruckers.network.client.rmi;

import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.Component;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RmiClientTest {
    RmiClient game;
    VirtualServerRmi server;
    FlightBoard flightBoard;
    Shipboard shipboard;


    Component component;
    Map<Point, Component> componentMap;
    Point point;

    Map<String, Shipboard> playerToShip;

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

        playerToShip = new HashMap<>();
        playerToShip.put("roborbio", shipboard);

        flightBoard = new FlightBoard();
        flightBoard.setLoopLength(10);
        flightBoard.setColorToPlace(Map.of('♠', 2, '♥', 5, '♣', 8));
        flightBoard.setStartingPositionLeft(List.of(6));

        try {
            game = new RmiClient(server);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        game.setModelPersonalNickname("roborbio");
        game.setModelShipboards(playerToShip);


    }


    @Test
    public void tryToPrint() throws IOException {
        game.showNewCard(1);
    }
}