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

    public CliStash(List<Component> stashedComponents) {
        this.cliStashedComponents = stashedComponents.stream()
                .map(CliComponent::of).collect(Collectors.toList());
    }

    public void onStash(int index, Component component) {
        cliStashedComponents.add(index, CliComponent.of(component));
    }

    public void onGrab(int index) {
        cliStashedComponents.remove(index);
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
