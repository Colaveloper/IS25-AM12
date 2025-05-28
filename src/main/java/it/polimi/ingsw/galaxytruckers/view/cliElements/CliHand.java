package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableGeneric;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.awt.*;
import java.util.List;

public class CliHand extends CliElement {

    CliComponent cliComponent;

    public CliHand(ObservableGeneric<Component> componentProperty, ObservableGeneric<Point> lastPosition) {
        componentProperty.addObserver(_ -> {
            updateHand(componentProperty.getValue(), lastPosition.getValue());
        });
        lastPosition.addObserver(_ -> {
            updateHand(componentProperty.getValue(), lastPosition.getValue());
        });
    }

    void updateHand(Component lastComponent, Point lastPosition) {
        super.notifyObservers();
        if (lastComponent != null && lastPosition == null) {
            cliComponent = CliComponent.of(lastComponent);
            cliComponent.addObserver(this);
        } else {
            cliComponent = null;
        }
    }

    @Override
    protected List<String> getNewDescription() {
        return DescriptionUtils.borderAndTitle(
                cliComponent == null
                        ? List.of("    ", "     ", "   ")
                        : cliComponent.getDescription(),
                "hand"
        );
    }
}
