package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class CliShipHandAndStash extends CliShipBoard {

    private final CliHand cliHand;
    private final CliStash cliStash;

    public CliShipHandAndStash(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);

        if (shipBoard.getLastComponent() != null) {
            cliHand = new CliHand(shipBoard.getLastComponent());
        } else {
            cliHand = new CliHand();
        }
        cliStash = new CliStash(shipBoard.getStashedComponents());
    }

    public String getNickname() {
        return "Cli Ship Hand and Stash";
    }

    public void onStash(Component component) {
        cliStash.onStash(component);
        setDirty();
    }

    public void onGrabStashed(int index) {
        cliStash.onGrab(index);
        setDirty();
    }

    public void clearHand() {
        cliHand.clearHand();
        setDirty();
    }

    public void setHand(Component component) {
        cliHand.setHand(component);
        setDirty();
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
