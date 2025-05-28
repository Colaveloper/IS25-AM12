package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;

import java.util.ArrayList;
import java.util.List;

public class CliStash extends CliElement {

    protected final List<CliComponent> cliStashedComponents = new ArrayList<>();

    public CliStash(ObservableList<Component> stashedComponents) {
        stashedComponents.addListener(new ObservableList.Listener<>() {
            @Override
            public void onAdd(int index, Component element) {
                cliStashedComponents.add(index, element!=null ? CliComponent.of(element) : null);
                CliStash.super.notifyObservers();
            }

            @Override
            public void onRemove(int index, Component element) {
                cliStashedComponents.add(index, null);
                CliStash.super.notifyObservers();
            }
        });
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();
        List<String> unitDescription = new ArrayList<>();
        for(int i=0 ; i<cliStashedComponents.size() ; i++) {
            unitDescription.clear();
            if(cliStashedComponents.get(i) == null) {
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
