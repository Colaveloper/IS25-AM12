package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CliShipAndHand extends CliShipBoard {

    private final CliHand cliHand;
    private Point lastPosition;
    private Component lastComponent;

    public CliShipAndHand(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);
        cliHand = new CliHand(shipBoard.getLastComponent());
        this.lastPosition = null;
        this.lastComponent = null;
    }

    public void onOffer(Component component) {
        this.lastComponent = component;
        //cliHand.updateHand(component);
    }

    public void onWeld() {
        this.lastComponent = null;
        this.lastPosition = null;
    }

    public void onPlace(Point point) {
        CliComponent component;
        if (lastPosition != null) {
            component = this.cliComponentMap.remove(lastPosition);
        } else {
            component = CliComponent.of(lastComponent);
        }
        lastPosition = point;
        this.cliComponentMap.put(point, component);
    }

    public CliHand getCliHand() {
        return cliHand;
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();
        description.addAll(super.getDescription());
        description.addAll(cliHand.getDescription());
        return description;
    }
}
