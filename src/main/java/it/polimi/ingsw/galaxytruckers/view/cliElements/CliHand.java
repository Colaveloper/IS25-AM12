package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import jdk.jfr.Description;

import java.util.List;

public class CliHand extends CliElement {

    private final Component component;

    public CliHand(Component component) {
        this.component = component;
    }

    @Override
    protected List<String> getDescription() {
        return DescriptionUtils.borderAndTitle(
                component == null
                        ? List.of("   ", "   ", "   ")
                        : CliComponent.of(component).getDescription(),
                "hand"
        );
    }
}
