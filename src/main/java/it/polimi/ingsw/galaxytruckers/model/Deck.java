package it.polimi.ingsw.galaxytruckers.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.*;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import com.google.common.annotations.VisibleForTesting;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public abstract class Deck{
    protected final List<AdventureCard> relevantCards;
    @VisibleForTesting
    protected List<AdventureCard> masterDeck;
    private AdventureCard currentCard;
    protected static String jsonPath = "src/main/resources/cardsReference.json";
    private final FlightBoard flightBoard;

    public Deck(Set<Level> relevantLevels, FlightBoard flightBoard) throws IOException {
        this.flightBoard = flightBoard;
        this.relevantCards = loadRelevantCards(relevantLevels);
        Collections.shuffle(this.relevantCards);
    }

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
    public void initMasterDeck() {
        throw new UnsupportedOperationException("This action is unsupported at the selected level");
    }

    /**
     * Removes a card from the master deck and sets it as current
     * @return true if there are still cards, false otherwise
     */
    public boolean tryDrawCard() {
        if (masterDeck.isEmpty()) {
            return false;
        } else {
            currentCard = masterDeck.removeFirst();
            return true;
        }
    }

    @VisibleForTesting
    protected List<AdventureCard> loadRelevantCards(Set<Level> levels) throws IOException {
        //reading from json file and returning the list of components
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        List<AdventureCard> cards = new ArrayList<>();

        //iterating through nodes and adding each as a card to list
        for (JsonNode node : rootNode){
            Level level = Level.valueOf(node.get("level").asText());
            if (levels.contains(level)) {
                String type = node.get("type").asText();
                AdventureCard card;

                Image image = new Image("file:"+node.get("path").asText());

                card = switch (type) {
                    case "planets" -> new PlanetsCard(
                            image,
                            level,
                            flightBoard,
                            parsePlanets(node.get("planets")),
                            node.get("flight day loss").asInt()
                    );
                    case "pirates" -> new PiratesCard(
                            image,
                            level,
                            flightBoard,
                            node.get("firePowerThreshold").asInt(),
                            node.get("credits").asInt(),
                            node.get("flight day loss").asInt(),
                            parseProjectiles(node.get("shoots"))
                    );
                    case "smugglers" -> new SmugglersCard(
                            image,
                            level,
                            flightBoard,
                            node.get("penalty").asInt(),
                            node.get("cannons").asInt(),
                            parseGoods(node.get("storage")),
                            node.get("flight day loss").asInt()
                    );
                    case "slavers" -> new SlaversCard(
                            image,
                            level,
                            flightBoard,
                            node.get("penalty").asInt(),
                            node.get("cannons").asInt(),
                            node.get("credits").asInt(),
                            node.get("flight day loss").asInt()
                    );
                    case "meteors" -> new MeteorSwarmCard(
                            image,
                            level,
                            flightBoard,
                            parseProjectiles(node.get("meteors"))
                    );
                    case "epidemic" -> new EpidemicCard(
                            image,
                            level,
                            flightBoard
                    );
                    case "stardust" -> new StarDustCard(
                            image,
                            level,
                            flightBoard
                    );
                    case "abandonedShip" -> new AbandonedShipCard(
                            image,
                            level,
                            flightBoard,
                            node.get("credits").asInt(),
                            node.get("people").asInt(),
                            node.get("flight day loss").asInt()
                    );
                    case "abandonedStation" -> new AbandonedStationCard(
                            image,
                            level,
                            flightBoard,
                            parseGoods(node.get("storage")),
                            node.get("people").asInt(),
                            node.get("flight day loss").asInt()
                    );
                    case "warzone" -> new CombatZoneCard(
                                image,
                                level,
                                flightBoard,
                                node.path("flight day loss").asInt(0),
                                node.path("crew loss").asInt(0),
                                node.path("goods loss").asInt(0),
                                parseProjectiles(node.get("shoots")),
                                new ObjectMapper().convertValue(
                                        node.get("actions"),
                                        new TypeReference<List<String>>(){}
                                )
                        );
                    case "open space" -> new OpenSpaceCard(
                            image,
                            level,
                            flightBoard
                    );
                    default -> throw new IllegalArgumentException("Unknown card type: " + type);
                };

            cards.add(card);
            }
        }
        return cards;
    }

    private static List<Projectile> parseProjectiles(JsonNode projectilesNode) {
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

    private static  List<Map<GoodsType, Integer>> parsePlanets(JsonNode planetsNode) {
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

    private static Map<GoodsType, Integer> parseGoods(JsonNode goodsNode) {
        Map<GoodsType, Integer> goods = new HashMap<>();
        for (GoodsType type : GoodsType.values()) {
            goods.put(type, goodsNode.get(type.name().toLowerCase()).asInt());
        }
        return goods;
    }

    @VisibleForTesting
    public List<AdventureCard> getMasterDeck() {
        return masterDeck;
    }
}