package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import javafx.scene.image.Image;

import java.util.*;

public class FlightBoard extends Physical {
    private int loopLength;
    private List<Integer>  startingPositionLeft;
    private Map<Colors, Integer> playerToPlace;

    public FlightBoard() {
        this.startingPositionLeft = new ArrayList<>();
        this.playerToPlace = new HashMap<>();
    }

    public void setStartingPositionLeft(List<Integer> startingPositionLeft) {
        this.startingPositionLeft = startingPositionLeft;
    }

    public void setLoopLength(int loopLength) {
        this.loopLength = loopLength;
    }

    public void setPlayerToPlace(Map<Colors, Integer> playerToPlace) {
        this.playerToPlace = playerToPlace;
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

    public Colors getCurrentLeaderColor() {
        return playerToPlace.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
