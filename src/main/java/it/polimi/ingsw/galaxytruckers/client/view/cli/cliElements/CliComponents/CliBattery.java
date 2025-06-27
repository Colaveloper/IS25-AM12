package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Battery;

import java.util.List;

/**
 * Represents a CLI component for displaying a battery on the ship board.
 */
public class CliBattery extends CliComponent {
    private final Battery battery;

    /**
     * Creates a new CLI representation of a Battery component.
     *
     * @param component the Battery component to be represented
     */
    public CliBattery(Battery component) {
        super(component);
        this.battery = component;
    }

    @Override
    protected List<String> getNewDescription() {
        return addBorders(" ⭍" + battery.getNumBatteries());
    }
}
