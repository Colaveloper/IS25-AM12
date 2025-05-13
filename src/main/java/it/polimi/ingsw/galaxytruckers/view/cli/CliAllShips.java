package it.polimi.ingsw.galaxytruckers.view.cli;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;

import java.util.ArrayList;
import java.util.List;

public class CliAllShips extends CliElement {

    public CliAllShips(ClientModel model) {
        super(model);
//        for (CliShipBoard shipboard : shipsMap.values()) {
//            shipboard.setChangeListener(this);
//            //super.registerObservables(shipboard);
//        }
    }

    @Override
    public List<String> getNewDescription() {
        List<List<String>> descriptions = new ArrayList<>();
        List<String> sequenceDescription = new ArrayList<>();
        StringBuilder row = new StringBuilder();

        for (CliElement cliElement : model.getShips().values()) {
            descriptions.add(cliElement.getDescription());
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
