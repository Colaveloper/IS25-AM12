package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.network.shared.VirtualServer;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllShips extends Physical {
    private final Map<String, Shipboard> shipsMap;

    public AllShips(LinkedHashMap<String, Shipboard> shipsMap) {

        this.shipsMap = shipsMap;
        for (Shipboard shipboard : shipsMap.values()) {
            shipboard.setChangeListener(this);
            //super.registerObservables(shipboard);
        }
    }

    @Override
    public Node getNode(VirtualServer server) {
        return null;
    }

    @Override
    public List<String> getNewDescription() {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (Physical physical : shipsMap.values()) {
            descriptions.add(physical.getDescription());
        }
        for (int i = 0; i < descriptions.getFirst().size(); i++) {
            for (List<String> shipDescription : descriptions) {
                row.append(shipDescription.get(i));
            }
            sequenceDescription.add(row.toString());
            row.setLength(0);
        }

        return sequenceDescription;
    }

    public void addPlayer(Shipboard shipboard) {
        shipboard.setChangeListener(this);
    }
}
