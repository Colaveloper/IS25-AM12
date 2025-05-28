//package it.polimi.ingsw.galaxytruckers.view;
//
//import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
//import it.polimi.ingsw.galaxytruckers.view.adventureClient.AdventureCard;
//import javafx.scene.Node;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class Forecast extends Physical{
//
//    //private final List<List<Integer>> decks;
//    private final List<Boolean> deckAvailable;
//    private final List<AdventureCard> deckInHand;
//
//    public Forecast() {
//        //this.decks = new ArrayList<>(List.of(null, null, null));
//        this.deckAvailable = new ArrayList<>(List.of(true, true, true));
//        this.deckInHand = new ArrayList<>();
//    }
//
//    public void setMyDeck(List<Integer> deckInHand) throws IOException {
//        for (Integer i : deckInHand) {
//            this.deckInHand.add(new AdventureCard(i));
//        }
//    }
//
//    public void blockDeck(int deckIndex) throws IOException {
//        deckAvailable.set(deckIndex, false);
//    }
//
//    public void freeDeck(int deckIndex) throws IOException {
//        deckAvailable.set(deckIndex, true);
//    }
//
//    public boolean isDeckAvailable(int deckIndex) throws IOException {
//        return deckAvailable.get(deckIndex);
//    }
//
//    @Override
//    public List<String> getNewDescription() {
//        List<String> newDescription = new ArrayList<String>();
//
//        //TODO refine
//        for(AdventureCard card : deckInHand) {
//            newDescription.addAll(card.getNewDescription());
//            newDescription.add("----------------------------");
//        }
//
//        return newDescription;
//    }
//
//    @Override
//    public Node getNode(VirtualServer server) throws IOException {
//        return null;
//    }
//}
