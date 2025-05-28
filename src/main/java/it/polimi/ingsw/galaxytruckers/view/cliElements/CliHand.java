package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableGeneric;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.List;

public class CliHand extends CliElement {

    CliComponent cliComponent;

    public CliHand(ObservableGeneric<Component> componentProperty) {
        componentProperty.addObserver(newComponent -> {
            super.notifyObservers();
            if (newComponent != null) {
                cliComponent = CliComponent.of(newComponent);
                cliComponent.addObserver(this);
            } else {
                cliComponent = null;
            }
        });
    }

    @Override
    protected List<String> getNewDescription() {
        return DescriptionUtils.borderAndTitle(
                cliComponent == null
                        ? List.of("   ", "   ", "   ")
                        : cliComponent.getDescription(),
                "hand"
        );
    }
}
