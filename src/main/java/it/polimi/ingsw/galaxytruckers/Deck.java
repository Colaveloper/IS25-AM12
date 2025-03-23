package it.polimi.ingsw.galaxytruckers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.*;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;
import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Deck{
    protected List<AdventureCard> masterDeck;
    private AdventureCard currentCard;

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

    public static List<AdventureCard> loadCards(File jsonFile) throws IOException {
        //reading from json file and returning the list of components
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonFile);
        FlightBoard flightBoard = null;

        List<AdventureCard> cards = new ArrayList<>();

        //iterating through nodes and adding each as a card to list
        for (JsonNode node : rootNode){
            String type = node.get("type").asText();
            AdventureCard card;
            Level level = Level.valueOf(node.get("level").asText());
            Image image = new Image(node.get("path").asText());

            switch(type){
                case "planets":
                    card = null;
                    break;
                case "pirates":
                    card = new PiratesCard(
                            image,
                            level,
                            node.get("firePowerThreshold").asInt(),
                            node.get("creditPrize").asInt(),
                            node.get("flightDaysLoss").asInt(),
                            parseProjectiles(node.get("projectiles"))
                            );
                    break;
                case "smugglers":
                    card = null;
//                    card = new SmugglersCard();
                    break;
                case "slavers":
                    card = null;
//                    card = new SlaversCard();
                    break;
                case "meteors":
                    card = null;
//                    card = new MeteorSwarmCard();
                    break;
                case "epidemic":
                    card = null;
//                    card = new EpidemicCard();
                    break;
                case "stardust":
                    card = null;
//                    card = new StarDustCard();
                    break;
                case "abandoned Ship":
                    card = null;
//                    card = new AbandonedShipCard();
                    break;
                case "abandoned station":
                    card = null;
//                    card = new AbandonedShipCard();
                    break;
                case "combat zone":
                    card = null;
//                    card = new CombatZoneCard();
                    break;
                case "open space":
                    card = null;
//                    card = new OpenSpaceCard();
                    break;
                case "sabotage":
                    card = null;
//                    card = new SabotageCard();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown card type: " + type);
            }
            cards.add(card);
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

}
