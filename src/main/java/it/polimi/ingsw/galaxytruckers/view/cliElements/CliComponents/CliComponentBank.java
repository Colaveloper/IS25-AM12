package it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.observables.ObservableList;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliElement;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ComponentBank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CliComponentBank extends CliElement {

    private final ComponentBank componentBank;
    private final Map<Component, CliComponent> revealedComponents = new HashMap<>();

    public CliComponentBank(ComponentBank componentBank) {

        this.componentBank = componentBank;

        componentBank.getCoveredComponentsNProperty().addObserver(_ -> {
            super.notifyObservers();
        });

        componentBank.getUncoveredComponentsProperty().getUnmodifiableView().forEach(component -> {
            revealedComponents.put(component, CliComponent.of(component));
        });
        componentBank.getUncoveredComponentsProperty().addListener(new ObservableList.Listener<>() {
            @Override
            public void onAdd(int index, Component component) {
                revealedComponents.put(component, CliComponent.of(component));
            }

            @Override
            public void onRemove(int index, Component component) {
                revealedComponents.remove(component);
            }
        });
    }

    @Override
    protected List<String> getNewDescription() {
        StringBuilder row = new StringBuilder();

        int nCovered = componentBank.getCoveredComponentsNProperty().getValue();
        List<String> coveredDescription = new ArrayList<>(DescriptionUtils.borderAndTitle(List.of("", String.valueOf(nCovered), ""), "down"));

        List<String> revealedDescription = new ArrayList<>();
        List<String> revealedDescriptionUnit = new ArrayList<>();
        int i = 0;
        for (CliComponent cliComponent : revealedComponents.values()) {
            revealedDescriptionUnit.addAll(cliComponent.getNewDescription());
            revealedDescriptionUnit.add("  "+i+"  ");
            revealedDescription = DescriptionUtils.sideBySide(revealedDescription, revealedDescriptionUnit);
            revealedDescriptionUnit.clear();
        }
        revealedDescription = DescriptionUtils.borderAndTitle(revealedDescription, "up");

        return DescriptionUtils.sideBySide(coveredDescription, revealedDescription);
    }
}
