package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import javafx.collections.FXCollections;
import javafx.scene.Node;

import java.util.*;

public class FlightBoard extends CliElement {

    public FlightBoard() {
        this.startingPositionLeft = new ArrayList<>();
        this.playerToPlace = FXCollections.observableHashMap();
        super.registerObservables(playerToPlace);
    }

    public void setStartingPositionLeft(List<Integer> startingPositionLeft) {
        this.startingPositionLeft = startingPositionLeft;
    }

    public void setLoopLength(int loopLength) {
        this.loopLength = loopLength;
    }

    public void setPlayerToPlace(Map<Colors, Integer> playerToPlace) {
        this.playerToPlace.putAll(playerToPlace);
    }

    @Override
    public List<String> getNewDescription() {
        String[] result = new String[loopLength];
        Arrays.fill(result, "_");

        // Place '□' at the starting positions
        startingPositionLeft.forEach(pos -> result[pos] = "□");

        // Place emojis from colorToPlace
        playerToPlace.forEach((color, pos) -> result[pos] = color.getDescription());

        return List.of(String.join("", result));
    }

    @Override
    public Node getNode(VirtualServer server) {
        return null;
    }

    public Colors getCurrentLeaderColor() {
        return playerToPlace.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
