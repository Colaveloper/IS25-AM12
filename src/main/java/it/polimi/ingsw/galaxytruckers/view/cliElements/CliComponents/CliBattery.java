package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Battery;

import java.util.List;

public class CliBattery extends CliComponent {
    private final Battery battery;

    public CliBattery(Battery component) {
        super(component);
        this.battery = component;
    }

    @Override
    public List<String> getNewDescription() {
        return addBorders(" ⭍" + battery.getNumBatteries());
    }
}
