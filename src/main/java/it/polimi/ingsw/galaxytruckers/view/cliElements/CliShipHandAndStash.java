package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipHandAndStash extends CliShipBoard {

    CliHand cliHand;
    CliStash cliStash;

    public CliShipHandAndStash(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);

        cliHand = new CliHand(shipBoard.getLastComponentProperty());
        cliHand.addObserver(this);

        cliStash = new CliStash(shipBoard.getStashedComponentsProperty());
        cliStash.addObserver(this);
    }

    @Override
    public List<String> getNewDescription(){
        List<String> description = new ArrayList<>();
        description.addAll(super.getNewDescription());
        description.addAll(
                DescriptionUtils.sideBySide(
                        cliHand.getDescription(),
                        cliStash.getDescription()
                )
        );
        return description;
    }
}
