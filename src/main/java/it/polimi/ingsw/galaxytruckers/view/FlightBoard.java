package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FlightBoard implements Physical {
    private int loopLength;
    private List<Integer>  startingPositionLeft;
    private Map<Character, Integer> colorToPlace;

    public int getLoopLength() {
        return loopLength;
    }

    public void setLoopLength(int loopLength) {
        this.loopLength = loopLength;
    }

    public List<Integer> getStartingPositionLeft() {
        return startingPositionLeft;
    }

    public void setStartingPositionLeft(List<Integer> startingPositionLeft) {
        this.startingPositionLeft = startingPositionLeft;
    }

    public Map<Character, Integer> getColorToPlace() {
        return colorToPlace;
    }

    public void setColorToPlace(Map<Character, Integer> colorToPlace) {
        this.colorToPlace = colorToPlace;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public String getDescription() {
        char[] result = new char[loopLength];
        Arrays.fill(result, '_');

        for (int pos : startingPositionLeft) {
            if (pos >= 0 && pos < loopLength)
                result[pos] = '□';
        }

        for (Map.Entry<Character, Integer> entry : colorToPlace.entrySet()) {
            int pos = entry.getValue();
            if (pos >= 0 && pos < loopLength)
                result[pos] = entry.getKey();
        }

        return new String(result);
    }
}
