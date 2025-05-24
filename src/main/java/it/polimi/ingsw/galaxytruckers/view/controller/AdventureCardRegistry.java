package it.polimi.ingsw.galaxytruckers.view.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AdventureCardRegistry {
    private static AdventureCardRegistry instance;
    private static final String jsonPath = "src/main/resources/cardsReference.json";

    private final Map<Integer, JsonNode> adventureCards = new HashMap<>();

    public static AdventureCardRegistry getInstance() {
        if (instance == null) {
            instance = new AdventureCardRegistry();
        }
        return instance;
    }

    private AdventureCardRegistry() {
        try {
            loadRelevantCards();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void loadRelevantCards() throws IOException {
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        for(JsonNode node : rootNode) {
            adventureCards.put(node.get("id").asInt(), node);
        }
    }

    public AdventureCard getCard(int id) {
        return parseAdventureCard(adventureCards.get(id));
    }

    /**
     * Loads projectile information from a JSON file into a list.
     * @param projectilesNode the JsonNode from which to load the list of projectiles
     * @return a {@link List} of the loaded projectiles
     * */
    private List<Projectile> parseProjectiles(JsonNode projectilesNode) {
        List<Projectile> projectiles = new ArrayList<>();
        int direction = projectilesNode.get(1).asInt();
        for (JsonNode node : projectilesNode) {
            switch (node.get(0).asText()) {
                case "small fire":
                    projectiles.add(new Projectile(0,direction, ProjectileType.SMALLFIRE));
                    break;
                case "big fire":
                    projectiles.add(new Projectile(0,direction, ProjectileType.BIGFIRE));
                    break;
                case  "big meteor":
                    projectiles.add(new Projectile(0,direction, ProjectileType.BIGMETEOR));
                    break;
                case "small meteor":
                    projectiles.add(new Projectile(0,direction, ProjectileType.SMALLMETEOR));
                    break;
            }
        }
        return projectiles;
    }

    private AdventureCard parseAdventureCard(JsonNode cardNode) {
        String type = cardNode.get("type").asText();
        Level level = Level.valueOf(cardNode.get("level").asText().toUpperCase());
        int id = cardNode.get("id").asInt();
        AdventureCard card;

        card = switch (type) {
            case "planets" -> new PlanetsCard(
                    level,
                    parsePlanets(cardNode.get("planets")),
                    cardNode.get("flight day loss").asInt(),
                    id
            );
            case "pirates" -> new PiratesCard(
                    level,
                    cardNode.get("firePowerThreshold").asInt(),
                    cardNode.get("credits").asInt(),
                    cardNode.get("flight day loss").asInt(),
                    parseProjectiles(cardNode.get("shoots")),
                    id
            );
            case "smugglers" -> new SmugglersCard(
                    level,
                    cardNode.get("penalty").asInt(),
                    cardNode.get("cannons").asInt(),
                    parseGoods(cardNode.get("storage")),
                    cardNode.get("flight day loss").asInt(),
                    id
            );
            case "slavers" -> new SlaversCard(
                    level,
                    cardNode.get("penalty").asInt(),
                    cardNode.get("cannons").asInt(),
                    cardNode.get("credits").asInt(),
                    cardNode.get("flight day loss").asInt(),
                    id
            );
            case "meteors" -> new MeteorSwarmCard(
                    level,
                    parseProjectiles(cardNode.get("meteors")),
                    id
            );
            case "epidemic" -> new EpidemicCard(
                    level,
                    id
            );
            case "stardust" -> new StarDustCard(
                    level,
                    id
            );
            case "abandonedShip" -> new AbandonedShipCard(
                    level,
                    cardNode.get("credits").asInt(),
                    cardNode.get("people").asInt(),
                    cardNode.get("flight day loss").asInt(),
                    id
            );
            case "abandonedStation" -> new AbandonedStationCard(
                    level,
                    parseGoods(cardNode.get("storage")),
                    cardNode.get("people").asInt(),
                    cardNode.get("flight day loss").asInt(),
                    id
            );
            case "warzone" -> new CombatZoneCard(
                    level,
                    parseChecks(cardNode.path("checks")),
                    parsePenalties(cardNode.path("penalties"), cardNode),
                    id
            );
            case "open space" -> new OpenSpaceCard(
                    level,
                    id
            );
            default -> throw new IllegalArgumentException("Unknown card type: " + type);
        };
        return card;
    }

    /**
     * Loads planet information from a JSON file into a list.
     * @param planetsNode the JsonNode from which to load the list of planets
     * @return a {@link List} of the loaded planets
     * */
    private List<Map<GoodsType, Integer>> parsePlanets(JsonNode planetsNode) {
        List<Map<GoodsType, Integer>> planets = new ArrayList<>();
        for (JsonNode node : planetsNode) {
            Map<GoodsType, Integer> map = parseGoods(node);
            planets.add(map);
        }
        return planets;
    }

    /**
     * Loads projectile information from a JSON file into a map.
     * @param goodsNode the JsonNode from which to load the map of goods
     * @return a {@link Map} of the loaded goods
     * */
    private Map<GoodsType, Integer> parseGoods(JsonNode goodsNode) {
        Map<GoodsType, Integer> goods = new HashMap<>();
        for (GoodsType type : GoodsType.values()) {
            goods.put(type, goodsNode.get(type.name().toLowerCase()).asInt());
        }
        return goods;
    }

    private List<CombatZoneCheck> parseChecks(JsonNode checkNode) {
        List<String> checkNames = new ObjectMapper().convertValue(checkNode, new TypeReference<>(){});
        List<CombatZoneCheck> checks = new ArrayList<>();
        for (String name : checkNames) {
            checks.add(switch (name) {
                case "min cannons" -> CombatZoneCheck.FIREPOWER;
                case "min engine" -> CombatZoneCheck.ENGINEPOWER;
                case "min crew" -> CombatZoneCheck.CREWSIZE;
                default -> throw new IllegalArgumentException("Unknown check name: " + name);
            });
        }
        return checks;
    }

    private List<Penalty> parsePenalties(JsonNode penaltiesNode, JsonNode cardNode) {
        List<Penalty> penalties = new ArrayList<>();
        List<String> penaltiesNames = new ObjectMapper().convertValue(penaltiesNode, new TypeReference<>(){});
        for (String name : penaltiesNames) {
            penalties.add(switch (name) {
                case "loses flight days" -> new FlightDaysLoss(cardNode.path("flight day loss").asInt());
                case "loses goods" -> new GoodsLoss(cardNode.path("storage").asInt());
                case "gets shot" -> new ProjectileThreat(parseProjectiles(cardNode.path("shoots")));
                case "loses crew" -> new CrewLoss(cardNode.path("crew loss").asInt());
                default -> throw new IllegalArgumentException("Unknown penalty name: " + name);
            });
        }
        return penalties;
    }
}
