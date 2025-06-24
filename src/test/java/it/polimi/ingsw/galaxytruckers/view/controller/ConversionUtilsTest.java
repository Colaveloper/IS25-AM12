package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.view.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConversionUtilsTest {
    private PlayerRegistry playerRegistry;
    private Player player;
    private ConversionUtils utils;

    @BeforeEach
    void setUp() {
        playerRegistry = new PlayerRegistry();
        player = playerRegistry.addPlayer("testPlayer");
        utils = new ConversionUtils(playerRegistry);
    }

    @Test
    void convertPlayerMap() {
        Map<String, Integer> scores = Map.of("testPlayer", 10);
        Map<Player, Integer> convertedMap = utils.convertPlayerMap(scores);

        assertNotNull(convertedMap);
        assertTrue(convertedMap.containsKey(player));
        assertEquals(10, convertedMap.get(player));
    }
}
