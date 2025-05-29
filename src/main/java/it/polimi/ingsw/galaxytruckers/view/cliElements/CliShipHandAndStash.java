package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipHandAndStash extends CliShipBoard {

    private final CliHand cliHand;
    private final CliStash cliStash;

    public CliShipHandAndStash(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);

        cliHand = new CliHand(shipBoard.getLastComponent());
        cliStash = new CliStash(shipBoard.getStashedComponents());
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
