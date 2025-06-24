package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliStash extends CliElement {
    private static final int SIZE = 2;

    protected final List<CliComponent> cliStashedComponents;

    /**
     * Creates a new CliStash from the given list of components.
     * Each component is converted to a CliComponent for CLI representation.
     *
     * @param stashedComponents The list of components to be stashed
     */
    public CliStash(List<Component> stashedComponents) {
        this.cliStashedComponents = stashedComponents.stream()
                .map(CliComponent::of).collect(Collectors.toList());
    }

    /**
     * Adds a new component to the stash.
     * @param component
     */
    public void onStash(Component component) {
        cliStashedComponents.add(CliComponent.of(component));
        setDirty();
    }

    /**
     * Removes a component from the stash at the specified index.
     * @param index The index of the component to remove
     */
    public void onGrab(int index) {
        cliStashedComponents.remove(index);
        setDirty();
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();
        List<String> unitDescription = new ArrayList<>();
        for(int i=0 ; i<SIZE ; i++) {
            unitDescription.clear();
            if(i >= cliStashedComponents.size()) {
                unitDescription.addAll(List.of("     ", "  X  ", "     "));
            } else {
                unitDescription.addAll(cliStashedComponents.get(i).getDescription());
            }
            unitDescription.add(" "+i+" ");
            description = DescriptionUtils.sideBySide(description, unitDescription);
        }

        return DescriptionUtils.borderAndTitle(description, "stash");
    }
}
