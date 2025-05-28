package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipAndHand extends CliShipBoard {

    CliHand cliHand;

    public CliShipAndHand(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);
        cliHand = new CliHand(shipBoard.getLastComponentProperty(), shipBoard.getLastPosition());
        cliHand.addObserver(this);
    }

    @Override
    protected List<String> getNewDescription(){
        List<String> description = new ArrayList<>();
        description.addAll(super.getDescription());
        description.addAll(cliHand.getDescription());
        return description;
    }
}
