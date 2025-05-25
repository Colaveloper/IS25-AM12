package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.*;

public class CliComponentFactory {
    ClientModel model;

    public CliComponentFactory(ClientModel model) {
        this.model = model;
    }


    public CliComponent createCliComponent(Component component) {
        return switch (component) {
            case Battery battery -> new CliBattery(model, battery);
            case Cabin cabin -> new CliCabin(model, cabin);
            case Cannon cannon -> new CliCannon(model, cannon);
            case CargoHold cargoHold -> new CliCargoHold(model, cargoHold);
            case Engine engine -> new CliEngine(model, engine);
            case LifeSupport lifeSupport -> new CliLifeSupport(model, lifeSupport);
            case Shield shield -> new CliShield(model, shield);
            default -> throw new IllegalStateException("Unexpected value: " + component);
        };
    }
}