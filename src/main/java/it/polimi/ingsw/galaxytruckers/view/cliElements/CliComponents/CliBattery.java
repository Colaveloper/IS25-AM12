package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Battery;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.List;

public class CliBattery extends CliComponent {
    private final Battery battery;

    public CliBattery(ClientModel model, Battery component) {
        super(model, component);
        this.battery = component;
    }

    @Override
    public List<String> getDescription() {
        return generateborders(" ⭍" + battery.getNumBatteries());
    }
}
