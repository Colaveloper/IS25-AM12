package it.polimi.ingsw.galaxytruckers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import it.polimi.ingsw.galaxytruckers.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.*;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

                switch(type){
                    case "planets":
                        card = new PlanetsCard(
                                image,
                                level,
                                flightBoard,
                                parsePlanets(node.get("planets")),
                                node.get("flightDaysLoss").asInt()
                        );
                        break;
                    case "pirates":
                        card = new PiratesCard(
                                image,
                                level,
                                flightBoard,
                                node.get("firePowerThreshold").asInt(),
                                node.get("credits").asInt(),
                                node.get("flightDaysLoss").asInt(),
                                parseProjectiles(node.get("shoots"))
                        );
                        break;
                    case "smugglers":
                        card = new SmugglersCard(
                                image,
                                level,
                                flightBoard,
                                node.get("penalty").asInt(),
                                node.get("cannons").asInt(),
                                parseGoods(node.get("storage")),
                                node.get("flightDaysLoss").asInt()
                        );
                        break;
                    case "slavers":
                        card = new SlaversCard(
                                image,
                                level,
                                flightBoard,
                                node.get("penalty").asInt(),
                                node.get("cannons").asInt(),
                                node.get("credits").asInt(),
                                node.get("flightDaysLoss").asInt()
                        );
                        break;
                    case "meteors":
                        card = new MeteorSwarmCard(
                                image,
                                level,
                                flightBoard,
                                parseProjectiles(node.get("meteors"))
                        );
                        break;
                    case "epidemic":
                        card = new EpidemicCard(
                                image,
                                level,
                                flightBoard
                        );
                        break;
                    case "stardust":
                        card = new StarDustCard(
                                image,
                                level,
                                flightBoard
                        );
                        break;
                    case "abandonedShip":
                        card = new AbandonedShipCard(
                                image,
                                level,
                                flightBoard,
                                node.get("credits").asInt(),
                                node.get("people").asInt(),
                                node.get("flightDaysLoss").asInt()
                        );
                        break;
                    case "abandonedStation":
                        card = new AbandonedStationCard(
                                image,
                                level,
                                flightBoard,
                                parseGoods(node.get("storage")),
                                node.get("people").asInt(),
                                node.get("flightDaysLoss").asInt()
                        );
                        break;
                    case "warzone":
                        card = new OpenSpaceCard(
                                image,
                                level,
                                flightBoard
                                ); // TODO: Update
//                        card = new CombatZoneCard(
//                                image,
//                                level,
//                                flightBoard,
//                                node.get("flightDaysLoss").asInt(),
//                                node.get("humans").asInt(),
//                                parseProjectiles(node.get("shoots"))
//                        );
                        break;
                    case "open space":
                        card = new OpenSpaceCard(
                                image,
                                level,
                                flightBoard
                        );
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown card type: " + type);
                }

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
}
