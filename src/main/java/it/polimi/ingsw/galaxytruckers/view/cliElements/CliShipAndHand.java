package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipAndHand extends CliShipBoard {
    public CliShipAndHand(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);
    }

    @Override
    public List<String> getNewDescription(){
        List<String> description = new ArrayList<>();
        description.addAll(super.getNewDescription());
        description.addAll(new CliHand(shipBoard.getLastComponent().orElse(null)).getNewDescription());
        return description;
    }
}
