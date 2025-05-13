package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.view.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CliAllShips extends CliElement {

    private final LinkedHashMap<Colors, CliShipBoard> ships;

    public CliAllShips(ClientModel model) {
        super(model);
        this.ships = new LinkedHashMap<>();
        model.getShips().entrySet().forEach(
                e ->
                ships.put(e.getKey(), new CliShipBoard(model, e.getKey()))
        );
    }

    @Override
    public List<String> getNewDescription() throws IOException {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (CliElement e : ships.values()) {
            descriptions.add(e.getDescription());
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
}
