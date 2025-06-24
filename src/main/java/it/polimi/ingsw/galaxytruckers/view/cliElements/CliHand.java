package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.awt.*;
import java.util.List;

public class CliHand extends CliElement {

    private CliComponent cliComponent;

    /**
     * Creates a new CLI hand element with the given component.
     * If the component is null, it will represent an empty hand.
     *
     * @param component The component to be displayed in the hand, or null for an empty hand
     */
    public CliHand(Component component) {
        this.cliComponent = CliComponent.of(component);
    }

    /**
     * Default constructor for an empty CLI hand.
     * Initializes the hand without any component.
     */
    public CliHand() {
        this.cliComponent = null;
    }

    /**
     * Sets the hand to display the given component.
     * If the component is null, it clears the hand.
     *
     * @param lastComponent The component to be displayed in the hand, or null to clear the hand
     */
    public void setHand(Component lastComponent) {
        cliComponent = CliComponent.of(lastComponent);
        setDirty();
    }

    /**
     * Clears the hand by setting the component to null.
     */
    public void clearHand() {
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
