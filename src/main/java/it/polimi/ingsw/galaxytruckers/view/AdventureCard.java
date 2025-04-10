package it.polimi.ingsw.galaxytruckers.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.image.Image;
import javax.swing.text.html.Option;
import static it.polimi.ingsw.galaxytruckers.model.Deck.parsePlanets;
import static it.polimi.ingsw.galaxytruckers.model.Deck.parseProjectiles;
import static it.polimi.ingsw.galaxytruckers.model.Deck.parseGoods;

public class AdventureCard {
    // class attributes
    private String description;
    private String losses;
    private String earnings;

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
    private Optional<List<Projectile>> projectiles;
    private Optional<List<Map<GoodsType, Integer>>> planets;
    private Optional<List<String>> actions;
    private Image image;

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
        //image = new Image("file:"+cardNode.get("path").asText());
        image = null;

        // loading remaining attributes
        // TODO: define description, earnings, and losses for each card
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

                description = "sono diponibili" + planets.get().size() + " planets";
                //earnings = "sul pianeta sono disponibili" + "tot rossi tot gialli tot" + " loot";

                // TODO: define here
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

                description = "l'avverisario ha firepower = " + firepower + ", attivare cannoni?";
                earnings = "vuoi prendere crediti = " + credits + "?";
                losses = "colpi di cannone in coordinate" + "direzioni";
                // TODO: define projectile directions
                // TODO: find a way to get rolls from server
                // TODO: define here

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

                // TODO: define here

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

                // TODO: define here

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

                // TODO: define here

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

                // TODO: define here

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

                // TODO: define here

                break;
            case "warzone":
                flightDaysLost = cardNode.get("flight day loss").asInt(0);
                crewLost = cardNode.get("crew loss").asInt(0);
                numGoodsLost = cardNode.get("goods loss").asInt(0);
                projectiles = Optional.of(parseProjectiles(cardNode.get("shoots")));
                actions = Optional.of(new ObjectMapper().convertValue(
                        cardNode.get("actions"),
                        new TypeReference<List<String>>(){}
                ));

                // everything else is empty
                credits = 0;
                firepower = 0;
                goods = Optional.empty();
                planets = Optional.empty();

                // TODO: define here

                break;
            case "open space", "stardust", "epidemic":
                emptyAttributes();

                // TODO: define here

                break;
            default:
                emptyAttributes();
                // TODO: description, earnings, and losses are empty
                throw new IllegalArgumentException("Unknown card type: " + type);
        }
    }

    // public methods
    public String getDescription(){return description;}

    public String getEarnings(){return earnings;}

    public String getLosses(){return losses;}

    public String getCardName() {
        return type;
    }

    // private helper methods
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
}