package it.polimi.ingsw.galaxytruckers.view.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdventureCardRegistryTest {

    private AdventureCardRegistry registry;

    @BeforeEach
    void setUp() {
        registry = AdventureCardRegistry.getInstance();
    }

    @Test
    void getInstance_ReturnsSameInstance() {
        AdventureCardRegistry instance1 = AdventureCardRegistry.getInstance();
        AdventureCardRegistry instance2 = AdventureCardRegistry.getInstance();
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void getSize_ReturnsCorrectCardCount() {
        int size = registry.getSize();
        assertTrue(size > 0, "Registry should contain cards");
        assertEquals(size, registry.getIdToImagePath().size(),
            "Size should match number of card images");
    }

    @Test
    void getCard_InvalidId_ThrowsException() {
        assertThrows(NullPointerException.class, () -> registry.getCard(-1));
        assertThrows(NullPointerException.class, () -> registry.getCard(999999));
    }

    @Test
    void parseProjectiles_ValidInput() throws IOException {
        // Create test registry with custom JSON
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "shoots": [
                    ["small fire", "LEFT"],
                    ["big fire", "RIGHT"],
                    ["big meteor", "UP"],
                    ["small meteor", "DOWN"]
                ]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        List<Projectile> projectiles = testRegistry.testParseProjectiles(node.get("shoots"));
        assertEquals(4, projectiles.size());

        // Verify each projectile type and direction
        assertProjectile(projectiles.get(0), ProjectileType.SMALLFIRE, Direction.LEFT);
        assertProjectile(projectiles.get(1), ProjectileType.BIGFIRE, Direction.RIGHT);
        assertProjectile(projectiles.get(2), ProjectileType.BIGMETEOR, Direction.UP);
        assertProjectile(projectiles.get(3), ProjectileType.SMALLMETEOR, Direction.DOWN);
    }

    @Test
    void parsePlanets_ValidInput() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "planets": [
                    {
                        "red": 2,
                        "blue": 1,
                        "green": 0,
                        "yellow": 3
                    }
                ]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        List<Map<GoodsType, Integer>> planets = testRegistry.testParsePlanets(node.get("planets"));
        assertEquals(1, planets.size());

        Map<GoodsType, Integer> planet = planets.getFirst();
        assertEquals(2, planet.get(GoodsType.RED));
        assertEquals(1, planet.get(GoodsType.BLUE));
        assertEquals(0, planet.get(GoodsType.GREEN));
        assertEquals(3, planet.get(GoodsType.YELLOW));
    }

    @Test
    void parseChecks_ValidInput() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "checks": ["min cannons", "min engine", "min crew"]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        List<CombatZoneCheck> checks = testRegistry.testParseChecks(node.get("checks"));
        assertEquals(3, checks.size());
        assertEquals(CombatZoneCheck.FIREPOWER, checks.get(0));
        assertEquals(CombatZoneCheck.ENGINEPOWER, checks.get(1));
        assertEquals(CombatZoneCheck.CREWSIZE, checks.get(2));
    }

    @Test
    void parseChecks_InvalidInput() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "checks": ["invalid_check"]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        assertThrows(IllegalArgumentException.class,
            () -> testRegistry.testParseChecks(node.get("checks")));
    }

    @Test
    void parsePenalties_ValidInput() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "penalties": ["loses flight days", "loses goods", "gets shot", "loses crew"],
                "flight day loss": 2,
                "storage": 3,
                "crew loss": 1,
                "shoots": [["small fire", "LEFT"]]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        List<Penalty> penalties = testRegistry.testParsePenalties(node.get("penalties"), node);
        assertEquals(4, penalties.size());

//        assertInstanceOf(FlightDaysLoss.class, penalties.get(0));
//        assertEquals(2, ((FlightDaysLoss)penalties.get(0)).getDays());
//
//        assertInstanceOf(GoodsLoss.class, penalties.get(1));
//        assertEquals(3, ((GoodsLoss)penalties.get(1)).getAmount());
//
//        assertInstanceOf(ProjectileThreat.class, penalties.get(2));
//        assertFalse(((ProjectileThreat)penalties.get(2)).projectiles);

        assertInstanceOf(CrewLoss.class, penalties.get(3));
        assertEquals(1, ((CrewLoss)penalties.get(3)).getCrew());
    }

    @Test
    void parsePenalties_InvalidPenaltyName() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "penalties": ["invalid_penalty"]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        assertThrows(IllegalArgumentException.class,
            () -> testRegistry.testParsePenalties(node.get("penalties"), node));
    }

    @Test
    void parseAllCardTypes() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        ObjectMapper mapper = new ObjectMapper();

        // Test each card type
        String planetCard = """
            {
                "type": "planets",
                "level": "FIRST",
                "planets": [{"red": 1, "blue": 1, "green": 1, "yellow": 1}],
                "flight day loss": 1,
                "id": 1
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(planetCard)));

        String piratesCard = """
            {
                "type": "pirates",
                "level": "FIRST",
                "firePowerThreshold": 2,
                "credits": 3,
                "flight day loss": 1,
                "shoots": [["small fire", "LEFT"]],
                "id": 2
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(piratesCard)));

        String smugglersCard = """
            {
                "type": "smugglers",
                "level": "FIRST",
                "penalty": 2,
                "cannons": 3,
                "storage": {"red": 1, "blue": 1, "green": 1, "yellow": 1},
                "flight day loss": 1,
                "id": 3
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(smugglersCard)));

        String slaversCard = """
            {
                "type": "slavers",
                "level": "FIRST",
                "penalty": 2,
                "cannons": 3,
                "credits": 4,
                "flight day loss": 1,
                "id": 4
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(slaversCard)));

        String meteorsCard = """
            {
                "type": "meteors",
                "level": "FIRST",
                "meteors": [["small meteor", "LEFT"]],
                "id": 5
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(meteorsCard)));

        String epidemicCard = """
            {
                "type": "epidemic",
                "level": "FIRST",
                "id": 6
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(epidemicCard)));

        String stardustCard = """
            {
                "type": "stardust",
                "level": "FIRST",
                "id": 7
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(stardustCard)));

        String abandonedShipCard = """
            {
                "type": "abandonedShip",
                "level": "FIRST",
                "credits": 3,
                "people": 2,
                "flight day loss": 1,
                "id": 8
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(abandonedShipCard)));

        String abandonedStationCard = """
            {
                "type": "abandonedStation",
                "level": "FIRST",
                "storage": {"red": 1, "blue": 1, "green": 1, "yellow": 1},
                "people": 2,
                "flight day loss": 1,
                "id": 9
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(abandonedStationCard)));

        String warzoneCard = """
            {
                "type": "warzone",
                "level": "FIRST",
                "checks": ["min cannons"],
                "penalties": ["loses flight days"],
                "flight day loss": 1,
                "id": 10
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(warzoneCard)));

        String openSpaceCard = """
            {
                "type": "open space",
                "level": "FIRST",
                "id": 11
            }""";
        assertNotNull(testRegistry.parseAdventureCard(mapper.readTree(openSpaceCard)));
    }

    @Test
    void parseAdventureCard_InvalidCardType() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        ObjectMapper mapper = new ObjectMapper();

        String invalidCard = """
            {
                "type": "invalid_type",
                "level": "FIRST",
                "id": 999
            }""";

        assertThrows(IllegalArgumentException.class,
            () -> testRegistry.parseAdventureCard(mapper.readTree(invalidCard)));
    }

    @Test
    void parseAdventureCard_MissingRequiredFields() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        ObjectMapper mapper = new ObjectMapper();

        String invalidCard = """
            {
                "type": "pirates",
                "level": "FIRST",
                "id": 999
            }""";

        assertThrows(NullPointerException.class,
            () -> testRegistry.parseAdventureCard(mapper.readTree(invalidCard)));
    }

    @Test
    void loadRelevantCards_FileNotFound() {
        assertThrows(RuntimeException.class, () -> {
            new TestableAdventureCardRegistry() {
                @Override
                protected void loadRelevantCards() throws IOException {
                    throw new IOException("Test exception");
                }
            };
        });
    }

    @Test
    void parseProjectiles_InvalidProjectileType() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "shoots": [
                    ["invalid_projectile", "LEFT"]
                ]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        List<Projectile> projectiles = testRegistry.testParseProjectiles(node.get("shoots"));
        assertTrue(projectiles.isEmpty(), "Invalid projectile type should be skipped");
    }

    @Test
    void parsePlanets_MissingGoodsType() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "planets": [
                    {
                        "red": 2,
                        "blue": 1
                    }
                ]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        assertThrows(NullPointerException.class,
            () -> testRegistry.testParsePlanets(node.get("planets")));
    }

    @Test
    void parseProjectiles_InvalidDirection() throws IOException {
        TestableAdventureCardRegistry testRegistry = new TestableAdventureCardRegistry();
        String json = """
            {
                "shoots": [
                    ["small fire", "INVALID_DIRECTION"]
                ]
            }""";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        assertThrows(IllegalArgumentException.class,
            () -> testRegistry.testParseProjectiles(node.get("shoots")));
    }

    private void assertProjectile(Projectile projectile, ProjectileType expectedType, Direction expectedDirection) {
        assertEquals(expectedType, projectile.type());
        assertEquals(expectedDirection, projectile.direction());
    }

    // Test helper class to expose protected methods for testing
    private static class TestableAdventureCardRegistry extends AdventureCardRegistry {

        public List<Projectile> testParseProjectiles(JsonNode node) {
            return parseProjectiles(node);
        }

        public List<Map<GoodsType, Integer>> testParsePlanets(JsonNode node) {
            return parsePlanets(node);
        }

        public List<CombatZoneCheck> testParseChecks(JsonNode node) {
            return parseChecks(node);
        }

        public List<Penalty> testParsePenalties(JsonNode penaltiesNode, JsonNode cardNode) {
            return parsePenalties(penaltiesNode, cardNode);
        }
    }
}
