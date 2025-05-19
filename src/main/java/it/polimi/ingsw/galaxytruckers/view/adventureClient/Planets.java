//package it.polimi.ingsw.galaxytruckers.view.adventureClient;
//
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
//import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
//import it.polimi.ingsw.galaxytruckers.view.cli.CliElement;
//import javafx.scene.Node;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class Planets extends CliElement {
//
//
//    public Planets(AdventureCard currentCard){
//        if(currentCard.getCardName().equals("planets")){
//            this.planets = currentCard.getPlanets();
//        }
//        else{
//            this.planets = Optional.empty();
//        }
//        // init landing spots
//        this.landedPlayers = new ArrayList<>();
//        for(int i = 0; i < planets.get().size(); i++){
//            this.landedPlayers.add(Optional.empty());
//        }
//    }
//
//    /**
//     * Attempts to land the given player on the specified planet.
//     * <p>
//     * Planet indices start at 1 (not 0), so passing 1 refers to the first planet.
//     *
//     * @param planetIndex the 1-based index of the planet to land on
//     * @param nickname the player attempting to land
//     * @return true if the landing succeeded, false if the planet was already claimed
//     */
//    public boolean landPlayerOnPlanet(String nickname, int planetIndex){
//        int index = planetIndex - 1;
//        Optional<String> spot = landedPlayers.get(index - 1);
//        // if spot is empty, add the player to this planet
//        if(spot.isEmpty()){
//            landedPlayers.set(index, Optional.of(nickname));
//            return true; // success
//        }
//        return false; // failure, spot already taken
//    }
//
//    public Map<GoodsType, Integer> getPlanetGoods(int planetIndex){
//        return planets.get().get(planetIndex);
//    }
//
//    @Override
//    public List<String> getNewDescription() {
//        return describePlanets();
//    }
//
//    @Override
//    public Node getNode(VirtualServer server) {
//        return null;
//    }
//
//    // method currently public for testing TODO: fix this
//    public List<String> describePlanets() {
//        if (planets.isEmpty()) throw new IllegalStateException("No planet data available.");
//
//        List<Map<GoodsType, Integer>> planetList = planets.get();
//        List<GoodsType> orderedGoods = List.of(GoodsType.BLUE, GoodsType.GREEN, GoodsType.YELLOW, GoodsType.RED);
//        List<String> descriptions = new ArrayList<>();
//
//        for (int i = 0; i < planetList.size(); i++) {
//            StringBuilder sb = new StringBuilder();
//            sb.append("planet ").append(i + 1).append(": ");
//
//            int finalI = i;
//            String goods = orderedGoods.stream()
//                    .flatMap(type -> {
//                        int count = planetList.get(finalI).getOrDefault(type, 0);
//                        return Collections.nCopies(count, type.name().toLowerCase()).stream();
//                    })
//                    .collect(Collectors.joining(", "));
//
//            sb.append(goods.isEmpty() ? "no goods" : goods);
//
//            if (i < landedPlayers.size() && landedPlayers.get(i).isPresent()) {
//                sb.append(" — claimed by ").append(landedPlayers.get(i).get());
//            } else {
//                sb.append(" — free");
//            }
//
//            descriptions.add(sb.toString());
//        }
//
//        return descriptions;
//    }
//
//}
