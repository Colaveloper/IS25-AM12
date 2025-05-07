package it.polimi.ingsw.galaxytruckers.view.adventureClient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import it.polimi.ingsw.galaxytruckers.view.Physical;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static it.polimi.ingsw.galaxytruckers.model.Deck.parsePlanets;
import static it.polimi.ingsw.galaxytruckers.model.Deck.parseGoods;

public class AdventureCard extends Physical {
    // class attributes
    private List<String> description;

    /*
    * card-specific attributes
    * these can potentially be placed as local variables
    * in the constructor if not needed as class members
    * if they are to be refactored as local variables, then
    * the switch-cases will need to be cleaned up since only
    * effectively used variables get instantiated rather than
    * instantiating unused variables as 0 or empty
    * */
    private String type;
    private Level level;
    private int credits;
    private int flightDaysLost;
    private int crewLost;
    private int firepower;
    private int numGoodsLost;
    private Optional<Map<GoodsType, Integer>> goods;
    private Optional<List<Map.Entry<String, Integer>>> projectiles;
    private Optional<List<Map<GoodsType, Integer>>> planets;
    private Optional<List<String>> actions;
    private Path imagePath;

    /*
    * for card descriptions the following convention is used:
    * the first element is just the type of card with a choice given to the player
    * subsequent elements go in clockwise order on the card starting in the top left corner
    *
    * example:
    * pirates card with firepower threshold of 10, some shots fired, two flight days lost, and 12 credits received
    * the description list will be
    * element 0 - [pirates] card has been drawn! Will you fight?
    * element 1 - firepower needed to defeat pirates: 10
    * element 2 - projectiles incoming:
    *             big fire coming from the back
    *             small fire coming from the back
    * element 3 - flight days lost: 2
    * element 4 - credits reward if defeated: 12
    * */
    // constructor
    public AdventureCard(int id) throws IOException {
        // Reading array of card from JSON file
        String jsonPath = "src/main/resources/cardsReference.json";
        File jsonFile = new File(jsonPath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);

        // selecting card by id
        JsonNode cardNode = rootNode.get(id);

        // loading attributes that all cards have in common
        type = cardNode.get("type").asText();
        level = Level.valueOf(cardNode.get("level").asText());
        imagePath = Paths.get(cardNode.get("path").asText());

        // loading remaining attributes
        description = new ArrayList<>();
        switch (type) {
            case "planets":
                planets = Optional.of(parsePlanets(cardNode.get("planets")));
                flightDaysLost = cardNode.get("flight day loss").asInt();

                // everything else empty
                credits = 0;
                crewLost = 0;
                firepower = 0;
                numGoodsLost = 0;
                goods = Optional.empty();
                projectiles = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Choose where to land:");
                description.add(describePlanets());
                break;
            case "pirates":
                firepower = cardNode.get("firePowerThreshold").asInt();
                credits = cardNode.get("credits").asInt();
                flightDaysLost = cardNode.get("flight day loss").asInt();
                projectiles = Optional.of(parseProjectiles(cardNode.get("shoots")));

                // everything else is empty
                crewLost = 0;
                numGoodsLost = 0;
                goods = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Will you fight?");
                description.add("firepower needed to defeat pirates: " + firepower);
                description.add("projectiles incoming:\n" + describeProjectiles());
                description.add("flight days lost: " + flightDaysLost);
                description.add("credits reward: " + credits);

                break;
            case "smugglers":
                numGoodsLost = cardNode.get("penalty").asInt();
                firepower = cardNode.get("cannons").asInt();
                goods = Optional.of(parseGoods(cardNode.get("storage")));
                flightDaysLost = cardNode.get("flight day loss").asInt();

                // everything else is empty
                credits = 0;
                crewLost = 0;
                projectiles = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Will you fight?");
                description.add("firepower needed to defeat smugglers: " + firepower);
                description.add("goods lost if you are defeated: " + numGoodsLost);
                description.add("flight days lost: " + flightDaysLost);
                description.add("goods reward for defeating smugglers: " + describeGoods());
                break;
            case "slavers":
                crewLost = cardNode.get("penalty").asInt();
                firepower = cardNode.get("cannons").asInt();
                credits = cardNode.get("credits").asInt();
                flightDaysLost = cardNode.get("flight day loss").asInt();

                // everything else is empty
                numGoodsLost = 0;
                goods = Optional.empty();
                projectiles = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Will you fight?");
                description.add("firepower needed to defeat slavers: " + firepower);
                description.add("crew lost if you are defeated: " + crewLost);
                description.add("flight days lost: " + flightDaysLost);
                description.add("credits reward for defeating slavers: " + credits);
                break;
            case "meteors":
                projectiles = Optional.of(parseProjectiles(cardNode.get("meteors")));

                // everything else is empty
                credits = 0;
                crewLost = 0;
                firepower = 0;
                flightDaysLost = 0;
                numGoodsLost = 0;
                goods = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Meteors coming your way:");
                description.add("projectiles incoming:\n" + describeProjectiles());
                break;
            case "abandonedShip":
                credits = cardNode.get("credits").asInt();
                crewLost = cardNode.get("people").asInt();
                flightDaysLost = cardNode.get("flight day loss").asInt();

                // everything else is 0
                firepower = 0;
                numGoodsLost = 0;
                goods = Optional.empty();
                projectiles = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Want to sacrifice some crew?");
                description.add("flight days lost: " + flightDaysLost);
                description.add("credits reward for sacrificing crew: " + credits);
                description.add("crew to sacrifice: " + crewLost);
                break;
            case "abandonedStation":
                goods = Optional.of(parseGoods(cardNode.get("storage")));
                crewLost = cardNode.get("people").asInt();
                flightDaysLost = cardNode.get("flight day loss").asInt();

                // everything else is 0
                credits = 0;
                numGoodsLost = 0;
                firepower = 0;
                projectiles = Optional.empty();
                planets = Optional.empty();
                actions = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Got enough crew to take the goods?");
                description.add("crew needed: " + crewLost);
                description.add("flight days lost: " + flightDaysLost);
                description.add("goods reward: " + describeGoods());
                break;
            case "warzone":
                flightDaysLost = cardNode.get("flight day loss").asInt(0);
                crewLost = cardNode.get("crew loss").asInt(0);
                projectiles = Optional.of(parseProjectiles(cardNode.get("shoots")));
                actions = Optional.of(new ObjectMapper().convertValue(
                        cardNode.get("actions"),
                        new TypeReference<List<String>>(){}
                ));

                // everything else is empty
                credits = 0;
                firepower = 0;
                numGoodsLost = 0;
                goods = Optional.empty();
                planets = Optional.empty();

                // creating description
                description.add("["+type+"] card has been drawn! Let's hope you're ready...");
                description.add("Player with fewest crew loses " + flightDaysLost + " flight days");
                description.add("Player with lowest engine power loses " + crewLost + " crew members");
                description.add("Player with lowest firepower gets shot with:\n" + describeProjectiles());
                break;
            case "open space":
                emptyAttributes();
                // creating description
                description.add("["+type+"] card has been drawn! Fire up those engines!");
                break;
            case "stardust":
                emptyAttributes();
                // creating description
                description.add("["+type+"] card has been drawn! Watch out for those exposed connectors!");
                break;
            case "epidemic":
                emptyAttributes();
                // creating description
                description.add("["+type+"] card has been drawn! Disease strikes!");
                break;
            default:
                emptyAttributes();
                description.add("Error loading card.");
                throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }

    // public methods
    @Override
    public List<String> getNewDescription(){return description;}

    public String getCardName() {
        return type;
    }

    public Optional<List<Map<GoodsType, Integer>>> getPlanets(){ return planets;}

    public Optional<Map<GoodsType, Integer>> getGoods(){ return goods; }

    // helper methods
    public static List<Map.Entry<String, Integer>> parseProjectiles(JsonNode projectilesNode) {
        List<Map.Entry<String, Integer>> projectiles = new ArrayList<>();
        for (JsonNode node : projectilesNode) {
            projectiles.add(new AbstractMap.SimpleEntry<>(node.get(0).asText(), node.get(1).asInt()));
        }
        return projectiles;
    }

    private void emptyAttributes(){
        // everything is empty
        credits = 0;
        crewLost = 0;
        firepower = 0;
        flightDaysLost = 0;
        numGoodsLost = 0;
        goods = Optional.empty();
        projectiles = Optional.empty();
        planets = Optional.empty();
        actions = Optional.empty();
    }

    public String describePlanets() {
        /*
        * entrySet().stream() — loop over all good types per planet
        * Collections.nCopies() — repeat goods names based on count
        * flatMap() — flatten the repeated names into one stream
        * joining(", ") — turn them into the nice comma-separated string
        * */
        if (planets.isEmpty()) return "No planets.";

        StringBuilder sb = new StringBuilder();

        List<Map<GoodsType, Integer>> planetList = planets.get();
        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        for (int i = 0; i < planetList.size(); i++) {
            sb.append("planet ").append(i + 1).append(": ");

            int finalI = i;
            String goods = orderedGoods.stream()
                    .flatMap(type -> {
                        int count = planetList.get(finalI).getOrDefault(type, 0);
                        return Collections.nCopies(count, type.name().toLowerCase()).stream();
                    })
                    .collect(Collectors.joining(", "));

            sb.append(goods).append("\n");
        }

        return sb.toString().trim();
    }

    public String describeGoods() {
        if (goods.isEmpty()) return "No goods.";

        Map<GoodsType, Integer> goodsMap = goods.get();
        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);

        return orderedGoods.stream()
                .flatMap(type -> Collections.nCopies(goodsMap.getOrDefault(type, 0), type.name().toLowerCase()).stream())
                .collect(Collectors.joining(", "));
    }

    public String describeProjectiles() {
        Map<Integer, String> directionMap = Map.of(
                0, "front",
                1, "left",
                2, "back",
                3, "right"
        );

        return projectiles.orElse(Collections.emptyList()).stream()
                .map(entry -> entry.getKey() + " coming from the " +
                        directionMap.getOrDefault(entry.getValue(), "unknown"))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public Node getNode(VirtualServer server) {
        ImageView imageView = new ImageView(new Image("file:"+imagePath));
        imageView.setFitWidth(100);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        node.getChildren().add(imageView);
        return node;
    }
}