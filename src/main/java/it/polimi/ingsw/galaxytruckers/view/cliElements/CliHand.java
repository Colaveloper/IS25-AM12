package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.awt.*;
import java.util.List;

public class CliHand extends CliElement {

    private CliComponent cliComponent;

    public CliHand(Component component) {
        this.cliComponent = CliComponent.of(component);
    }

    public CliHand() {
        this.cliComponent = null;
    }

    void setHand(Component lastComponent) {
        cliComponent = CliComponent.of(lastComponent);
        setDirty();
    }

    void clearHand() {
        cliComponent = null;
        setDirty();
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
