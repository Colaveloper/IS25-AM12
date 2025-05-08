package it.polimi.ingsw.galaxytruckers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AllShipsTest {

    static AllShips allShips;
    static LinkedHashMap<String, Shipboard> shipboardList;
    static Shipboard shipboard;

    //@BeforeEach
    static void setUp() {
        shipboardList = new LinkedHashMap<>();
        shipboard = new Shipboard();
        shipboard.setShipArea(Set.of(new Point(12,7),new Point(11,7),new Point(10,7),new Point(9,7), new Point(8,7), new Point(7,8), new Point(6,7), new Point(7,6)));

        shipboardList.put("player1", shipboard);
        shipboardList.put("player2", shipboard);
        shipboardList.put("player3", shipboard);
        shipboardList.put("player4", shipboard);
        allShips = new AllShips(shipboardList, 5, 3);
    }

    @Test
    void getNewDescription() {
        allShips.getNewDescription();
    }

    public static void main(String[] args) throws IOException {
        setUp();
        allShips.getNewDescription().forEach(System.out::println);

    }
}