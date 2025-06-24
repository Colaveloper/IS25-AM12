package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CliShipAndHand extends CliShipBoard {

    private final CliHand cliHand;
    public CliShipAndHand(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);
        if (shipBoard.getLastComponent() != null && shipBoard.getLastPosition() == null) {
            cliHand = new CliHand(shipBoard.getLastComponent());
        } else {
            cliHand = new CliHand();
        }
    }

    /**
     * Clears the hand of the ship.
     */
    public void clearHand() {
        cliHand.clearHand();
        setDirty();
    }

    /**
     * Sets the hand of the ship to a specific component.
     *
     * @param component The component to set as the hand
     */
    public void setHand(Component component) {
        cliHand.setHand(component);
        setDirty();
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();
        description.addAll(super.getNewDescription());
        description.addAll(cliHand.getDescription());
        return description;
    }
}
