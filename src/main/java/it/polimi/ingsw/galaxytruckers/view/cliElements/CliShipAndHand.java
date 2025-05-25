package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipAndHand extends CliShipBoard {
    public CliShipAndHand(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);
    }

    @Override
    public List<String> getDescription(){
        List<String> description = new ArrayList<>();
        description.addAll(super.getDescription());
        description.addAll(new CliHand(shipBoard.getLastComponent().orElse(null)).getDescription());
        return description;
    }
}
