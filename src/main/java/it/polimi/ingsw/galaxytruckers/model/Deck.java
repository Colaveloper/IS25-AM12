package it.polimi.ingsw.galaxytruckers.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CombatZoneCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.CrewSizeCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.EnginePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.check.FirePowerCheck;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.penalty.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.model.factory.SecondFactory;
import it.polimi.ingsw.galaxytruckers.model.factory.TestFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class Deck {
    protected final List<AdventureCard> relevantCards;
    protected List<AdventureCard> masterDeck;
    private AdventureCard currentCard;
    protected static String jsonPath = "src/main/resources/cardsReference.json";

    /**
     * Constructs a new deck by instantiating cards of the correct level.
     * Loaded cards are shuffled for randomized order.
     * @param game the game instance from which configuration is used to load the relevant cards
     * @throws IOException if there is an error loading card data from the JSON file
     * */
    public Deck(Game game) throws IOException {
        this.relevantCards = loadRelevantCards(game);
        Collections.shuffle(this.relevantCards);
        this.masterDeck = new ArrayList<>();
    }

    /**
     * Returns the current card to be played.
     * @return the deck's current card*/
    public AdventureCard getCurrentCard() {
        return currentCard;
    }

    /**
     * @param id the id of the forecast deck
     * @return the requested forecast deck
     */
    public List<AdventureCard> getForecastDeck(int id) {
        throw new UnsupportedOperationException("This action is unsupported at the selected level");
    }

    /**
     * Mixes the forecast and hidden decks  into the master deck
     */
    public void initMasterDeck() {}

    /**
     * Removes a card from the master deck and sets it as current
     * @return true if there are still cards, false otherwise
     */
    public boolean tryDrawCard() {
        if (masterDeck.isEmpty()) {
            return false;
        } else {
            currentCard = masterDeck.removeLast();
            currentCard.initialize();
            return true;
        }
    }

    /**
     * Loads the cards of the relevant game level from a JSON file into a list.
     * @param game the game instance from which configuration is used to load the relevant cards
     * @throws IOException if there is an error loading card data from a JSON file
     * @return a {@link List} of loaded cards
     * */
    @VisibleForTesting
    protected static List<AdventureCard> loadRelevantCards(Game game) throws IOException {
        //reading from json file and returning the list of components
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        List<AdventureCard> cards = new ArrayList<>();
        Set<Level> levels = new HashSet<>();
        switch (game.getLevel()) {
            case TEST -> levels.add(Level.TEST);
            case SECOND -> levels.addAll(Set.of(Level.TEST, Level.FIRST, Level.SECOND));
            default -> throw new IllegalArgumentException("Unexpected level in game: " + game.getLevel());
        }

        //iterating through nodes and adding each as a card to list
        for(int i = 0; i < rootNode.size(); i++){
            Level level = Level.valueOf(rootNode.get(i).get("level").asText());
            if (levels.contains(level)) {
                String type = rootNode.get(i).get("type").asText();
                AdventureCard card;

                card = switch (type) {
                    case "planets" -> new PlanetsCard(
                            game,
                            level,
                            parsePlanets(rootNode.get(i).get("planets")),
                            rootNode.get(i).get("flight day loss").asInt(),
                            i
                    );
                    case "pirates" -> new PiratesCard(
                            game,
                            level,
                            rootNode.get(i).get("firePowerThreshold").asInt(),
                            rootNode.get(i).get("credits").asInt(),
                            rootNode.get(i).get("flight day loss").asInt(),
                            parseProjectiles(rootNode.get(i).get("shoots")),
                            i
                    );
                    case "smugglers" -> new SmugglersCard(
                            game,
                            level,
                            rootNode.get(i).get("penalty").asInt(),
                            rootNode.get(i).get("cannons").asInt(),
                            parseGoods(rootNode.get(i).get("storage")),
                            rootNode.get(i).get("flight day loss").asInt(),
                            i
                    );
                    case "slavers" -> new SlaversCard(
                            game,
                            level,
                            rootNode.get(i).get("penalty").asInt(),
                            rootNode.get(i).get("cannons").asInt(),
                            rootNode.get(i).get("credits").asInt(),
                            rootNode.get(i).get("flight day loss").asInt(),
                            i
                    );
                    case "meteors" -> new MeteorSwarmCard(
                            game,
                            level,
                            parseProjectiles(rootNode.get(i).get("meteors")),
                            i
                    );
                    case "epidemic" -> new EpidemicCard(
                            game,
                            level,
                            i
                    );
                    case "stardust" -> new StarDustCard(
                            game,
                            level,
                            i
                    );
                    case "abandonedShip" -> new AbandonedShipCard(
                            game,
                            level,
                            rootNode.get(i).get("credits").asInt(),
                            rootNode.get(i).get("people").asInt(),
                            rootNode.get(i).get("flight day loss").asInt(),
                            i
                    );
                    case "abandonedStation" -> new AbandonedStationCard(
                            game,
                            level,
                            parseGoods(rootNode.get(i).get("storage")),
                            rootNode.get(i).get("people").asInt(),
                            rootNode.get(i).get("flight day loss").asInt(),
                            i
                    );
                    case "warzone" -> new CombatZoneCard(
                            game,
                            level,
                            parseChecks(rootNode.get(i).path("checks")),
                            parsePenalties(rootNode.get(i).path("penalties"), rootNode.get(i)),
                            i
                    );
                    case "open space" -> new OpenSpaceCard(
                            game,
                            level,
                            i
                    );
                    default -> throw new IllegalArgumentException("Unknown card type: " + type);
                };

                cards.add(card);
            }
        }
//        for (JsonNode node : rootNode){
//            Level level = Level.valueOf(node.get("level").asText());
//            if (levels.contains(level)) {
//                String type = node.get("type").asText();
//                AdventureCard card;
//
//                card = switch (type) {
//                    case "planets" -> new PlanetsCard(
//                            game,
//                            level,
//                            parsePlanets(node.get("planets")),
//                            node.get("flight day loss").asInt()
//                    );
//                    case "pirates" -> new PiratesCard(
//                            game,
//                            level,
//                            node.get("firePowerThreshold").asInt(),
//                            node.get("credits").asInt(),
//                            node.get("flight day loss").asInt(),
//                            parseProjectiles(node.get("shoots"))
//                    );
//                    case "smugglers" -> new SmugglersCard(
//                            game,
//                            level,
//                            node.get("penalty").asInt(),
//                            node.get("cannons").asInt(),
//                            parseGoods(node.get("storage")),
//                            node.get("flight day loss").asInt()
//                    );
//                    case "slavers" -> new SlaversCard(
//                            game,
//                            level,
//                            node.get("penalty").asInt(),
//                            node.get("cannons").asInt(),
//                            node.get("credits").asInt(),
//                            node.get("flight day loss").asInt()
//                    );
//                    case "meteors" -> new MeteorSwarmCard(
//                            game,
//                            level,
//                            parseProjectiles(node.get("meteors"))
//                    );
//                    case "epidemic" -> new EpidemicCard(
//                            game,
//                            level
//                    );
//                    case "stardust" -> new StarDustCard(
//                            game,
//                            level
//                    );
//                    case "abandonedShip" -> new AbandonedShipCard(
//                            game,
//                            level,
//                            node.get("credits").asInt(),
//                            node.get("people").asInt(),
//                            node.get("flight day loss").asInt()
//                    );
//                    case "abandonedStation" -> new AbandonedStationCard(
//                            game,
//                            level,
//                            parseGoods(node.get("storage")),
//                            node.get("people").asInt(),
//                            node.get("flight day loss").asInt()
//                    );
//                    case "warzone" -> new CombatZoneCard(
//                            game,
//                            level,
//                            parseChecks(node.path("checks")),
//                            parsePenalties(node.path("penalties"), node));
//                    case "open space" -> new OpenSpaceCard(
//                            game,
//                            level
//                    );
//                    default -> throw new IllegalArgumentException("Unknown card type: " + type);
//                };
//
//            cards.add(card);
//            }
//        }
        return cards;
    }

    /**
     * Loads projectile information from a JSON file into a list.
     * @param projectilesNode the JsonNode from which to load the list of projectiles
     * @return a {@link List} of the loaded projectiles
     * */
    public static List<Projectile> parseProjectiles(JsonNode projectilesNode) {
        List<Projectile> projectiles = new ArrayList<>();
        int direction = projectilesNode.get(1).asInt();
        for (JsonNode node : projectilesNode) {
            switch (node.get(0).asText()) {
                case "small fire":
                    projectiles.add(new SmallFire(direction));
                    break;
                case "big fire":
                    projectiles.add(new BigFire(direction));
                    break;
                case  "big meteor":
                    projectiles.add(new BigMeteor(direction));
                    break;
                case "small meteor":
                    projectiles.add(new SmallMeteor(direction));
                    break;
            }
        }
        return projectiles;
    }

    /**
     * Loads planet information from a JSON file into a list.
     * @param planetsNode the JsonNode from which to load the list of planets
     * @return a {@link List} of the loaded planets
     * */
    public static  List<Map<GoodsType, Integer>> parsePlanets(JsonNode planetsNode) {
        List<Map<GoodsType, Integer>> planets = new ArrayList<>();
        int direction = planetsNode.get(1).asInt();
        for (JsonNode node : planetsNode) {
            Map<GoodsType, Integer> map = new HashMap<>();
            for (GoodsType type : GoodsType.values()) {
                map.put(type, node.get(type.name().toLowerCase()).asInt());
            }
            planets.add(map);
        }
        return planets;
    }

    /**
     * Loads projectile information from a JSON file into a map.
     * @param goodsNode the JsonNode from which to load the map of goods
     * @return a {@link Map} of the loaded goods
     * */
    public static Map<GoodsType, Integer> parseGoods(JsonNode goodsNode) {
        Map<GoodsType, Integer> goods = new HashMap<>();
        for (GoodsType type : GoodsType.values()) {
            goods.put(type, goodsNode.get(type.name().toLowerCase()).asInt());
        }
        return goods;
    }

    private static List<CombatZoneCheck> parseChecks(JsonNode checkNode) {
        List<String> checkNames = new ObjectMapper().convertValue(checkNode, new TypeReference<>(){});
        List<CombatZoneCheck> checks = new ArrayList<>();
        for (String name : checkNames) {
            checks.add(switch (name) {
                case "min cannons" -> FirePowerCheck.getInstance();
                case "min engine" -> EnginePowerCheck.getInstance();
                case "min crew" -> CrewSizeCheck.getInstance();
                default -> throw new IllegalArgumentException("Unknown check name: " + name);
            });
        }
        return checks;
    }

    private static List<Penalty> parsePenalties(JsonNode penaltiesNode, JsonNode cardNode) {
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

    @VisibleForTesting
    public List<AdventureCard> getMasterDeck() {
        return masterDeck;
    }
}