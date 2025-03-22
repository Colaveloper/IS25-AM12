package it.polimi.ingsw.galaxytruckers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.shipBuilding.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Deck{
    private List<AdventureCard> masterDeck;
    private AdventureCard currentCard;

    abstract public void initMasterDeck();

    abstract public AdventureCard getCurrentCard();

    abstract public AdventureCard drawCard();

    abstract public List<AdventureCard> peekForecastDeck(int id);

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

            switch(type){
                case "planets":
                    card = null;
                    break;
                case "pirates":
                    card = null;
//                    card = new PiratesCard();
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

}
