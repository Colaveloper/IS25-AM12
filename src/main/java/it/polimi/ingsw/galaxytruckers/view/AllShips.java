package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllShips extends Physical {
    private final Map<String, Shipboard> shipsMap;
    private List<List<String>> descriptions;
    private final int componentWidth;
    private final int componentHeight;

    public AllShips(LinkedHashMap<String, Shipboard> shipsMap, int componentWidth, int componentHeight) {
        this.componentWidth = componentWidth;
        this.componentHeight = componentHeight;
        this.shipsMap = shipsMap;
    }

    @Override
    public StackPane getNode() {
        return null;
    }

    @Override
    public List<String> getNewDescription() {
        descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();
        for (Physical physical : shipsMap.values()) {
            descriptions.add(physical.getNewDescription());
        }
        for (int i = 0; i < descriptions.getFirst().size() - 1; i++) {
            if ((i + 2)%3 == 0) {
                row.append(i/componentHeight + 5).append(" ");
            }
            else {
                row.append("  ");
            }
            for (List<String> shipDescription : descriptions) {
                row.append(shipDescription.get(i).substring(5)); //padding for numbers in original description
            }
            sequenceDescription.add(row.toString());
            row.setLength(0);
        }

        // remove to keep bottom coordinates for all ships (also remove -1 from for above)
        row.append("  ");
        for (int i = 0; i < 7; i++) {
            row.append(" ".repeat((componentWidth-1) / 2)).append(i + 4).append(" ".repeat((componentWidth-1) / 2));
        }
        sequenceDescription.add(row.toString());
        return sequenceDescription;
    }
}
