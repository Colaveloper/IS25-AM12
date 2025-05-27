package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.ArrayList;
import java.util.List;

public class CliStash extends CliElement{

    List<Component> stashedComponents;

    public CliStash(List<Component> stashedComponents) {
        this.stashedComponents = stashedComponents;
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();
        List<String> unitDescription = new ArrayList<>();
        for(int i=0 ; i<2 ; i++) {
            unitDescription.clear();
            unitDescription.addAll(CliComponent.of(stashedComponents.get(i)).getNewDescription());
            unitDescription.add("  "+(i+1));
            DescriptionUtils.sideBySide(description, unitDescription);
            i++;
        }

        return DescriptionUtils.borderAndTitle(description, "stash");
    }
}
